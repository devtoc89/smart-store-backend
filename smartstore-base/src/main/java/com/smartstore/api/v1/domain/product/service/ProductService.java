package com.smartstore.api.v1.domain.product.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.domain.product.entity.Product;
import com.smartstore.api.v1.domain.product.entity.ProductImage;
import com.smartstore.api.v1.domain.product.repository.ProductRepository;
import com.smartstore.api.v1.domain.product.vo.ProductFilterConditionVO;
import com.smartstore.api.v1.domain.product.vo.ProductImageVO;
import com.smartstore.api.v1.domain.product.vo.ProductVO;
import com.smartstore.api.v1.domain.storedfile.entity.StoredFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

// mapper layer 고민...

@Service
@RequiredArgsConstructor
public class ProductService {

  @PersistenceContext
  private EntityManager entityManager;
  private final ProductRepository productRepository;

  private ProductImage makeImageEntity(Product product, ProductImageVO iamgeVO) {
    return ProductImage
        .builder()
        .file(entityManager.getReference(StoredFile.class, iamgeVO.getFile().getBase().getId()))
        .product(product)
        .productId(product.getId())
        .isMain(iamgeVO.getIsMain())
        .orderBy(iamgeVO.getOrderBy())
        .build();
  }

  public Product applyCreate(UUID id, ProductVO vo) {

    var item = Product.builder()
        .id(id)
        .name(vo.getName())
        .price(vo.getPrice())
        .stock(vo.getStock())
        .categoryId(vo.getCategoryId())
        .build();

    var images = Optional.ofNullable(vo.getImages())
        .map(imageList -> imageList.stream()
            .map(v -> makeImageEntity(item, v))
            .toList())
        .orElse(List.<ProductImage>of());

    item.setImages(images);

    return item;
  }

  public void applyUpdate(Product entity, ProductVO vo) {
    entity.setName(vo.getName());
    entity.setPrice(vo.getPrice());
    entity.setStock(vo.getStock());
    entity.setCategoryId(vo.getCategoryId());
  }

  public void applyPartialUpdate(Product entity, ProductVO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      entity.setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getPrice())) {
      entity.setPrice(vo.getPrice());
    }
    if (!ObjectUtils.isEmpty(vo.getStock())) {
      entity.setPrice(vo.getStock());
    }
    if (!ObjectUtils.isEmpty(vo.getCategoryId())) {
      entity.setCategoryId(vo.getCategoryId());
    }
  }

  private Product findByIdOrExcept(UUID id) {
    return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 상품은 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExist(UUID id) {
    return productRepository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ProductVO findById(UUID id) {
    return ProductVO.fromEntity(findByIdOrExcept(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<ProductVO> findManyByCondition(ProductFilterConditionVO condition, Pageable pageable) {
    return ProductVO.fromEntityWithPage(productRepository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductVO create(UUID id, ProductVO vo) {
    var newProduct = applyCreate(id, vo);

    return ProductVO.fromEntity(productRepository.save(newProduct));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductVO replace(UUID id, ProductVO vo) {
    Product product = findByIdOrExcept(id);
    applyUpdate(product, vo);

    return ProductVO.fromEntity(productRepository.save(product));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductVO modify(UUID id, ProductVO vo) {
    Product product = findByIdOrExcept(id);
    applyPartialUpdate(product, vo);

    return ProductVO.fromEntity(productRepository.save(product));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ProductVO delete(UUID id) {
    Product product = findByIdOrExcept(id);
    product.markDelete();

    return ProductVO.fromEntity(productRepository.save(product));
  }
}