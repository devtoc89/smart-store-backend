package com.smartstore.api.v1.domain.product.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.domain.product.entity.Product;
import com.smartstore.api.v1.domain.product.entity.ProductImage;
import com.smartstore.api.v1.domain.product.repository.ProductImageRepository;
import com.smartstore.api.v1.domain.product.vo.ProductImageFilterConditionVO;
import com.smartstore.api.v1.domain.product.vo.ProductImageVO;
import com.smartstore.api.v1.domain.storedfile.entity.StoredFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImageService {

  @PersistenceContext
  private EntityManager entityManager;

  private final ProductImageRepository productImageRepository;

  private ProductImage findByIdOrExcept(UUID id) {
    return productImageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 상품 이미지가 존재하지 않습니다."));
  }

  public void applyUpdate(ProductImage entity, ProductImageVO vo) {
    entity.setFile(entityManager.getReference(StoredFile.class, vo.getFile().getBase().getId()));
    entity.setMain(vo.getIsMain());
    entity.setOrderBy(vo.getOrderBy());
  }

  public void applyPartialUpdate(ProductImage entity, ProductImageVO vo) {
    if (!ObjectUtils.isEmpty(vo.getFile().getBase().getId())) {
      entity.setFile(entityManager.getReference(StoredFile.class, vo.getFile().getBase().getId()));
    }
    if (!ObjectUtils.isEmpty(vo.getIsMain())) {
      entity.setMain(vo.getIsMain());
    }
    if (!ObjectUtils.isEmpty(vo.getOrderBy())) {
      entity.setOrderBy(vo.getOrderBy());
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExist(UUID id) {
    return productImageRepository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ProductImageVO findById(UUID id) {
    return ProductImageVO.fromEntity(findByIdOrExcept(id));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductImageVO updateMain(UUID productId, UUID id) {
    var mainImageOp = productImageRepository.findMain(productId);
    if (mainImageOp.isPresent() && !mainImageOp.get().getId().equals(id)) {
      var mainImage = mainImageOp.get();
      mainImage.setMain(false);
      productImageRepository.save(mainImage);
    }
    var tgtImage = findByIdOrExcept(id);
    tgtImage.setMain(true);
    productImageRepository.save(tgtImage);
    return ProductImageVO.fromEntity(tgtImage);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<ProductImageVO> findManyByCondition(ProductImageFilterConditionVO condition, Pageable pageable) {
    return ProductImageVO.fromEntityWithPage(productImageRepository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductImageVO create(ProductImageVO vo) {
    var newItem = ProductImage.builder()
        .file(entityManager.getReference(StoredFile.class, vo.getFile().getBase().getId()))
        .product(entityManager.getReference(Product.class, vo.getProductId()))
        .isMain(vo.getIsMain())
        .orderBy(vo.getOrderBy())
        .build();
    return ProductImageVO.fromEntity(productImageRepository.save(productImageRepository.save(newItem)));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public List<ProductImageVO> deleteByProductId(UUID id) {
    var entityList = productImageRepository.deleteByProductId(id);

    return entityList.stream()
        .map(entity -> {
          entity.markDelete();
          return productImageRepository.save(entity);
        })
        .map(ProductImageVO::fromEntity)
        .toList();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductImageVO modify(UUID id, ProductImageVO vo) {
    var entity = findByIdOrExcept(id);
    applyPartialUpdate(entity, vo);

    return ProductImageVO.fromEntity(productImageRepository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductImageVO delete(UUID id) {
    var entity = findByIdOrExcept(id);
    entity.markDelete();

    return ProductImageVO.fromEntity(productImageRepository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public List<ProductImageVO> updateOrder(UUID productId, List<UUID> sortedImageIds) {
    List<ProductImage> images = productImageRepository.findByProductId(productId);

    Map<UUID, ProductImage> imageMap = images.stream()
        .collect(Collectors.toMap(ProductImage::getId, Function.identity()));

    for (int i = 0; i < sortedImageIds.size(); i++) {
      UUID id = sortedImageIds.get(i);
      ProductImage image = imageMap.get(id);
      if (image != null) {
        image.setOrderBy(i); // 0부터 시작
        productImageRepository.save(image);
      }
    }

    return images.stream().map(ProductImageVO::fromEntity).toList();
  }
}