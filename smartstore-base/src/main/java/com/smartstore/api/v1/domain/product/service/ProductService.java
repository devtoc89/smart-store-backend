package com.smartstore.api.v1.domain.product.service;

import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.domain.product.entity.Product;
import com.smartstore.api.v1.domain.product.repository.ProductRepository;
import com.smartstore.api.v1.domain.product.vo.ProductFilterConditionVO;
import com.smartstore.api.v1.domain.product.vo.ProductVO;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  private Product getExistingProductById(UUID id) {
    return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 상품은 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ProductVO findProductById(UUID id) {
    return ProductVO.fromEntity(getExistingProductById(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<ProductVO> findProductsByCondition(ProductFilterConditionVO condition, Pageable pageable) {
    return ProductVO.fromEntityWithPage(productRepository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductVO createProduct(ProductVO productVO) {
    Product newProduct = Product.builder()
        .name(productVO.getName())
        .price(productVO.getPrice())
        .categoryId(productVO.getCategoryId())
        .build();

    return ProductVO.fromEntity(productRepository.saveAndFlush(newProduct));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductVO replaceProduct(UUID id, ProductVO productVO) {
    Product product = getExistingProductById(id);
    product.applyUpdate(productVO);

    return ProductVO.fromEntity(productRepository.save(product));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductVO modifyProduct(UUID id, ProductVO productVO) {
    Product product = getExistingProductById(id);
    product.applyPartialUpdate(productVO);

    return ProductVO.fromEntity(productRepository.save(product));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductVO deleteProduct(UUID id) {
    Product product = getExistingProductById(id);
    product.markDelete();

    return ProductVO.fromEntity(productRepository.save(product));
  }
}