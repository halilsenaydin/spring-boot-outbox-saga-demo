package io.github.halilsenaydin.inventory.product_service.business.concretes;

import io.github.halilsenaydin.shared.business.exceptions.BusinessException;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;
import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.business.constants.ErrorMessage;
import io.github.halilsenaydin.inventory.product_service.business.mapper.ProductMapper;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.DeleteProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductRepository;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductImage;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductManager implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OperationResult<Product, ProductResponse, ProductEvent> create(CreateProductRequest request) {
        Product product = new Product(
                request.getName(),
                request.getDescription(),
                request.getUnitPrice(),
                new ArrayList<ProductImage>());

        if (request.getImageUrls() != null) {
            request.getImageUrls().forEach(url -> product.addImage(new ProductImage(url, product)));
        }

        productRepository.save(product);

        return new OperationResult<Product, ProductResponse, ProductEvent>(product, ProductMapper.toResponse(product), ProductMapper.toEvent(product));
    }

    @Override
    @Transactional
    public OperationResult<Product, ProductResponse, ProductEvent> delete(DeleteProductRequest request) {
        Long productId = request.getProductId();
        Product product = productRepository.findWithImages(productId).orElseThrow(
            () -> new BusinessException(ErrorMessage.PRODUCT_NOT_FOUND)
        );

        product.setActive(false);

        productRepository.save(product);

        return new OperationResult<Product, ProductResponse, ProductEvent>(product, ProductMapper.toResponse(product), ProductMapper.toEvent(product));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product product = productRepository.findWithImages(id).orElseThrow(
            () -> new BusinessException(ErrorMessage.PRODUCT_NOT_FOUND)
        );

        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void addImage(Long productId, String imageUrl) {
        Product product = productRepository.findWithImages(productId).orElseThrow(
            () -> new BusinessException(ErrorMessage.PRODUCT_NOT_FOUND)
        );

        product.addImage(new ProductImage(imageUrl, product));

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void removeImage(Long productId, Long imageId) {
        Product product = productRepository.findWithImages(productId).orElseThrow(
            () -> new BusinessException(ErrorMessage.PRODUCT_NOT_FOUND)
        );

        product.getProductImages()
                .removeIf(img -> img.getId().equals(imageId));

        productRepository.save(product);
    }
}