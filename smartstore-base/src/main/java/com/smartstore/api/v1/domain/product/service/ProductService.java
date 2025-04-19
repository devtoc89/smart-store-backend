package com.smartstore.api.v1.domain.product.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

  private List<ProductImage> applyProductImages(Product product, ProductVO imageVOs) {
    if (imageVOs.getImages() == null) {
      return new ArrayList<>();
    }

    List<ProductImage> currentImages = Optional.ofNullable(product.getImages())
        .orElseGet(ArrayList::new);

    // 기존 이미지 맵
    Map<UUID, ProductImage> existingMap = currentImages.stream()
        .collect(Collectors.toMap(img -> img.getFile().getId(), Function.identity()));

    // 입력된 이미지 맵
    Map<UUID, ProductImageVO> inputMap = imageVOs.getImages().stream()
        .collect(Collectors.toMap(vo -> vo.getFile().getBase().getId(), Function.identity()));

    // 1. 삭제 대상: 기존에는 있었지만 입력에는 없는 fileId
    Set<UUID> toDelete = new HashSet<>(existingMap.keySet());
    toDelete.removeAll(inputMap.keySet());

    // 2. 신규 대상: 입력에는 있지만 기존에는 없는 fileId
    Set<UUID> toInsert = new HashSet<>(inputMap.keySet());
    toInsert.removeAll(existingMap.keySet());

    // 3. 수정 대상: 둘 다 존재하지만 속성이 다른 경우
    List<ProductImage> toUpdate = new ArrayList<>();
    for (Map.Entry<UUID, ProductImageVO> entry : inputMap.entrySet()) {
      UUID fileId = entry.getKey();
      ProductImageVO vo = entry.getValue();

      if (existingMap.containsKey(fileId)) {
        ProductImage existing = existingMap.get(fileId);
        if (!Objects.equals(existing.getOrderBy(), vo.getOrderBy())
            || !Objects.equals(existing.isMain(), vo.getIsMain())) {
          existing.setOrderBy(vo.getOrderBy());
          existing.setMain(vo.getIsMain());
          toUpdate.add(existing);
        }
      }
    }

    // 실제 처리
    List<ProductImage> finalImages = currentImages.stream()
        .filter(img -> !toDelete.contains(img.getFile().getId())) // 삭제 제외
        .collect(Collectors.toCollection(ArrayList::new));

    for (UUID fileId : toInsert) {
      finalImages.add(makeImageEntity(product, inputMap.get(fileId)));
    }

    return finalImages;

  }

  public Product applyCreate(UUID id, ProductVO vo) {

    var item = Product.builder()
        .id(id)
        .name(vo.getName())
        .price(vo.getPrice())
        .stock(vo.getStock())
        .categoryId(vo.getCategoryId())
        .build();

    item.setImages(applyProductImages(item, vo));

    return item;
  }

  public void applyUpdate(Product entity, ProductVO vo) {
    entity.setName(vo.getName());
    entity.setPrice(vo.getPrice());
    entity.setStock(vo.getStock());
    entity.setCategoryId(vo.getCategoryId());

    var newImages = applyProductImages(entity, vo);
    entity.getImages().clear();
    entity.getImages().addAll(newImages);
  }

  public void applyPartialUpdate(Product entity, ProductVO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      entity.setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getPrice())) {
      entity.setPrice(vo.getPrice());
    }
    if (!ObjectUtils.isEmpty(vo.getStock())) {
      entity.setStock(vo.getStock());
    }
    if (!ObjectUtils.isEmpty(vo.getCategoryId())) {
      entity.setCategoryId(vo.getCategoryId());
    }

    var newImages = applyProductImages(entity, vo);
    entity.getImages().clear();
    entity.getImages().addAll(newImages);
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