package com.smartstore.api.v1.application.admin.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductFilterRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPatchRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPostRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPutRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductResponseDTO;
import com.smartstore.api.v1.application.admin.product.service.AdminProductAppService;
import com.smartstore.api.v1.common.dto.ResponseWrapper;

//TODO: 커버리지 늘릴 것(개개 필드에 대한 검증 추가할 것)
@SpringBootTest
@AutoConfigureMockMvc
class AdminProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @InjectMocks
  private AdminProductController adminProductController;

  @MockitoBean
  private AdminProductAppService adminProductAppService;

  private ObjectMapper mapper;

  void assertResponse(AdminProductResponseDTO mock, MvcResult response)
      throws JsonProcessingException, UnsupportedEncodingException {
    ResponseWrapper<AdminProductResponseDTO> responseContent = mapper.readValue(
        response.getResponse().getContentAsString(),
        new TypeReference<ResponseWrapper<AdminProductResponseDTO>>() {
        });

    assertEquals(new ResponseWrapper<AdminProductResponseDTO>(mock), responseContent);
    assertEquals(mock, responseContent.getData());
  }

  void assertResponse(Page<AdminProductResponseDTO> mock, MvcResult response)
      throws JsonProcessingException, UnsupportedEncodingException {
    ResponseWrapper<PageImpl<AdminProductResponseDTO>> responseContent = mapper.readValue(
        response.getResponse().getContentAsString(),
        new TypeReference<ResponseWrapper<PageImpl<AdminProductResponseDTO>>>() {
        });

    assertEquals(new ResponseWrapper<Page<AdminProductResponseDTO>>(mock), responseContent);
    assertEquals(mock, responseContent.getData());
  }

  // TODO: 예외 처리 테스트 케이스 작성.
  void assertNotResponse(AdminProductResponseDTO mock, MvcResult response)
      throws JsonProcessingException, UnsupportedEncodingException {
    ResponseWrapper<AdminProductResponseDTO> responseContent = mapper.readValue(
        response.getResponse().getContentAsString(),
        new TypeReference<ResponseWrapper<AdminProductResponseDTO>>() {
        });

    assertEquals(new ResponseWrapper<AdminProductResponseDTO>(mock), responseContent);
    assertNotEquals(mock, responseContent.getData());
  }

  void assertNotResponse(Page<AdminProductResponseDTO> mock, MvcResult response)
      throws JsonProcessingException, UnsupportedEncodingException {
    ResponseWrapper<PageImpl<AdminProductResponseDTO>> responseContent = mapper.readValue(
        response.getResponse().getContentAsString(),
        new TypeReference<ResponseWrapper<PageImpl<AdminProductResponseDTO>>>() {
        });

    assertEquals(new ResponseWrapper<Page<AdminProductResponseDTO>>(mock), responseContent);
    assertNotEquals(mock, responseContent.getData());
  }

  @BeforeEach
  void setup() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(PageImpl.class, new PageDeserializer());
    mapper.registerModule(module);

    this.mapper = mapper;
  }

  class PageDeserializer extends JsonDeserializer<PageImpl<?>> {
    @Override
    public PageImpl<?> deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
      ObjectMapper mapper = (ObjectMapper) p.getCodec();
      JsonNode node = mapper.readTree(p);

      // 🔥 "size", "number", "totalElements"가 없는 경우 기본값 설정
      int page = node.has("number") ? node.get("number").asInt() : 0;
      int size = node.has("size") ? node.get("size").asInt() : 10;
      long total = node.has("totalElements") ? node.get("totalElements").asLong() : 0L;

      // content가 존재하는지 확인 후 변환
      List<?> content = node.has("content") && node.get("content").isArray()
          ? mapper.convertValue(node.get("content"), new TypeReference<List<?>>() {
          })
          : List.of();

      return new PageImpl<>(content, PageRequest.of(page, size), total);
    }
  }

  @Test
  void testSearchProducts() throws Exception {
    var mock = AdminProductResponseDTO.builder()
        .id("550e8400-e29b-41d4-a716-446655440000")
        .categoryId("550e8400-e29b-41d4-a716-446655440001")
        .name("Get Product")
        .price(1000)
        .createdAt("2025-03-16 14:30:12")
        .updatedAt("2025-03-16 14:30:12")
        .deletedAt(null)
        .isDeleted(false)
        .build();

    var req = AdminProductFilterRequestDTO.builder().keyword("Test Prodct").build();

    when(adminProductAppService.getList(eq(req), any()))
        .thenReturn(new PageImpl<AdminProductResponseDTO>(List.of(mock)));

    var responseOk = mockMvc.perform(get(Config.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .param("keyword", req.getKeyword()))
        .andExpect(status().isOk())
        .andReturn();

    assertResponse(new PageImpl<AdminProductResponseDTO>(List.of(mock)), responseOk);

    var responseFail = mockMvc.perform(get(Config.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .param("keyword", "It's Not" + req.getKeyword()))
        .andExpect(status().isOk())
        .andReturn();

    assertTrue(mapper.readValue(responseFail.getResponse().getContentAsString(),
        new TypeReference<ResponseWrapper<Page<AdminProductResponseDTO>>>() {
        }).getData().isEmpty());

    assertNotResponse(new PageImpl<AdminProductResponseDTO>(List.of(mock)), responseFail);
  }

  @Test
  void testPostProduct() throws Exception {
    var mock = AdminProductResponseDTO.builder()
        .id("550e8400-e29b-41d4-a716-446655440000")
        .categoryId("550e8400-e29b-41d4-a716-446655440001")
        .name("Get Product")
        .price(1000)
        .createdAt("2025-03-16 14:30:12")
        .updatedAt("2025-03-16 14:30:12")
        .deletedAt(null)
        .isDeleted(false)
        .build();

    var req = AdminProductPostRequestDTO.builder()
        .name(mock.getName())
        .price(mock.getPrice())
        .categoryId(mock.getCategoryId())
        .build();

    when(adminProductAppService.post(req))
        .thenReturn(mock);

    var response = mockMvc.perform(post(Config.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", Config.BASE_URL + "/550e8400-e29b-41d4-a716-446655440000"))
        .andReturn();

    assertResponse(mock, response);
  }

  @Test
  void testGetProduct() throws Exception {
    var mock = AdminProductResponseDTO.builder()
        .id("550e8400-e29b-41d4-a716-446655440000")
        .categoryId("550e8400-e29b-41d4-a716-446655440001")
        .name("Get Product")
        .price(2000)
        .createdAt("2025-03-16 14:30:12")
        .updatedAt("2025-03-16 17:01:54")
        .deletedAt(null)
        .isDeleted(false)
        .build();

    when(adminProductAppService.get(mock.getId()))
        .thenReturn(mock);

    var response = mockMvc.perform(get(Config.BASE_URL + "/{id}", mock.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertResponse(mock, response);
  }

  @Test
  void testPutProduct() throws Exception {
    var mock = AdminProductResponseDTO.builder()
        .id("550e8400-e29b-41d4-a716-446655440000")
        .categoryId("550e8400-e29b-41d4-a716-446655440001")
        .name("Updated Product")
        .price(2000)
        .createdAt("2025-03-16 14:30:12")
        .updatedAt("2025-03-16 17:01:54")
        .deletedAt(null)
        .isDeleted(false)
        .build();

    var req = AdminProductPutRequestDTO.builder()
        .name(mock.getName())
        .price(mock.getPrice())
        .categoryId(mock.getCategoryId())
        .build();

    when(adminProductAppService.put(eq(mock.getId()), eq(req)))
        .thenReturn(mock);

    var response = mockMvc.perform(put(Config.BASE_URL + "/{id}", mock.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andReturn();

    assertResponse(mock, response);
  }

  @Test
  void testPatchProduct() throws Exception {
    var mock = AdminProductResponseDTO.builder()
        .id("550e8400-e29b-41d4-a716-446655440000")
        .categoryId("550e8400-e29b-41d4-a716-446655440001")
        .name("Patched Product")
        .price(1000)
        .createdAt("2025-03-16 14:30:12")
        .updatedAt("2025-03-16 17:01:54")
        .deletedAt(null)
        .isDeleted(false)
        .build();

    var req = AdminProductPatchRequestDTO.builder()
        .name(mock.getName())
        .build();

    when(adminProductAppService.patch(eq(mock.getId()), eq(req)))
        .thenReturn(mock);

    var response = mockMvc.perform(patch(Config.BASE_URL + "/{id}", mock.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andReturn();

    assertResponse(mock, response);

  }

  @Test
  void testDeleteProduct() throws Exception {

    var mock = AdminProductResponseDTO.builder()
        .id("550e8400-e29b-41d4-a716-446655440000")
        .categoryId("550e8400-e29b-41d4-a716-446655440001")
        .name("Test Product")
        .price(1000)
        .createdAt("2025-03-16 14:30:12")
        .updatedAt("2025-03-16 17:01:54")
        .deletedAt("2025-03-16 17:01:54")
        .isDeleted(true)
        .build();

    when(adminProductAppService.delete(mock.getId()))
        .thenReturn(mock);

    var response = mockMvc.perform(delete(Config.BASE_URL + "/{id}", mock.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertResponse(mock, response);
  }
}
