package com.smartstore.cmd;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.config.spi.StandardConverters;
import org.hibernate.tool.schema.SourceType;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.internal.ExceptionHandlerHaltImpl;
import org.hibernate.tool.schema.internal.ExceptionHandlerLoggedImpl;
import org.hibernate.tool.schema.internal.Helper;
import org.hibernate.tool.schema.spi.ExecutionOptions;
import org.hibernate.tool.schema.spi.JpaTargetAndSourceDescriptor;
import org.hibernate.tool.schema.spi.SchemaManagementTool;
import org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator;
import org.hibernate.tool.schema.spi.ScriptSourceInput;
import org.hibernate.tool.schema.spi.ScriptTargetOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

//TODO: 저장 장소, 리팩토링
@Log4j2
@RequiredArgsConstructor
@Component
public class HibernateMigrationGenerator implements CommandLineRunner {

  @Value("${spring.datasource.username}")
  private String user;
  @Value("${spring.datasource.password}")
  private String pass;
  @Value("${spring.datasource.url}")
  private String url;
  @Value("${spring.datasource.driver-class-name}")
  private String driver;

  private final EntityManagerFactory emf;

  @Override
  public void run(String... args) throws SQLException {
    if (args.length == 0 || !args[0].equals("generateMigrationSql"))
      return;

    log.info(" Hibernate migration SQL 추출 (ScriptAction.UPDATE)...");

    String outputFile = "src/main/resources/db/migration/V" +
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        + "__auto_generated.sql";

    Map<String, Object> config = new HashMap<>();
    config.put(AvailableSettings.JAKARTA_JDBC_DRIVER, driver);
    config.put(AvailableSettings.JAKARTA_JDBC_USER, user);
    config.put(AvailableSettings.JAKARTA_JDBC_PASSWORD, pass);
    config.put(AvailableSettings.JAKARTA_JDBC_URL, url);

    config.put(AvailableSettings.JAKARTA_HBM2DDL_SCRIPTS_CREATE_TARGET, outputFile);

    // 추가 설정 (필요 시)
    config.put(AvailableSettings.HBM2DDL_CHARSET_NAME, "UTF-8");
    config.put(AvailableSettings.HBM2DDL_SCRIPTS_CREATE_APPEND, true);
    config.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY,
        "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
    config.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY,
        "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .applySettings(config)
        .build();

    // // Hibernate 내부 구현처럼 MigrateTargetDescriptor 구성
    ScriptTargetOutput scriptTargetOutput = Helper.interpretScriptTargetSetting(
        outputFile,
        registry.getService(ClassLoaderService.class),
        "UTF-8",
        false);

    JpaTargetAndSourceDescriptor migrateDescriptor = new JpaTargetAndSourceDescriptor() {
      @Override
      public EnumSet<TargetType> getTargetTypes() {
        return EnumSet.of(TargetType.SCRIPT);
      }

      @Override
      public ScriptTargetOutput getScriptTargetOutput() {
        return scriptTargetOutput;
      }

      @Override
      public SourceType getSourceType() {
        return SourceType.METADATA;
      }

      @Override
      public ScriptSourceInput getScriptSourceInput() {
        return null;
      }
    };

    // 핵심: SchemaMigrator의 doMigration 호출 (Script 대상만)
    SchemaManagementTool tool = registry.getService(SchemaManagementTool.class);
    ConfigurationService configService = registry.getService(ConfigurationService.class);

    ExecutionOptions executionOptions = SchemaManagementToolCoordinator.buildExecutionOptions(
        config,
        configService.getSetting(AvailableSettings.HBM2DDL_HALT_ON_ERROR,
            StandardConverters.BOOLEAN, false)
                ? ExceptionHandlerHaltImpl.INSTANCE
                : ExceptionHandlerLoggedImpl.INSTANCE);

    var metadataSources = new MetadataSources(registry);

    emf.getMetamodel().getEntities().forEach(entityType -> {
      Class<?> javaType = entityType.getJavaType();
      log.info("등록된 엔티티: " + javaType.getSimpleName());
      metadataSources.addAnnotatedClass(javaType);
    });

    tool.getSchemaMigrator(executionOptions.getConfigurationValues()).doMigration(
        metadataSources.buildMetadata(),
        executionOptions,
        (contributed) -> true,
        migrateDescriptor);

    log.info("Hibernate SQL 생성 완료: " + outputFile);

    System.exit(0);

  }
}
