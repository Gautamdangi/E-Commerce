package com.gautam.product.service;


import com.gautam.product.dao.ProductDAO;
import com.gautam.product.dto.CreateProductRequest;
import com.gautam.product.dto.ProductResponseDTO;
import com.gautam.product.dto.UpdateProductRequest;
import com.gautam.product.event.ProductCreatedEvent;
import com.gautam.product.event.ProductEventPublisher;
import com.gautam.product.exceptions.IllegalStateException;
import com.gautam.product.exceptions.ResourceNotFoundException;
import com.gautam.product.model.ProductDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ProductService {
    @Autowired
    private ProductDAO productDAO;

@Autowired private ProductEventPublisher productEventPublisher;



    //create product to store
    @CachePut(cacheNames = "products",key = "#result.id")
    @Transactional
    public ProductResponseDTO create(CreateProductRequest productDTO){

        var product = new ProductDetails();
        product.setProductName(productDTO.getProductName());
        product.setCategory(productDTO.getCategory());
        product.setBrand(productDTO.getBrand());
        product.setQuantity(productDTO.getQuantity());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setIsActive(productDTO.getIsActive());

//save the product
        ProductDetails saved = productDAO.save(product);
        //event publish
        ProductCreatedEvent event =
                new ProductCreatedEvent(product.getId(), product.getProductName(), product.getQuantity());
        productEventPublisher.publishProductCreated(event);
        return mapToProductResponseDTO(saved);

    }



// update product
@CacheEvict(cacheNames = "products",key = "#id")
@Transactional
public ProductResponseDTO update(UpdateProductRequest update, Long Id){
    //check the product available or not

        ProductDetails product = productDAO.findById(Id).orElseThrow(()->new ResourceNotFoundException("Product with this productId is not found"));
        //what if product is not active
        if(!product.getIsActive()){
            throw new IllegalStateException("You cannot update inactive product");
        }
       if(update.getPrice() != null)
           product.setPrice(update.getPrice());

       if(update.getDescription() != null)
           product.setDescription(update.getDescription());
       ProductDetails saved = productDAO.save(product);

       // Publish event
        // eventPublisher.publish(new ProductUpdatedEvent(saved.getId()));

        return mapToProductResponseDTO(saved);

    }




    // Soft delete method
    @CacheEvict(cacheNames = "products",key = "#id")
    public void deleteProduct(Long id) {
        ProductDetails product = productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setIsActive(false);  // Set the flag
        productDAO.save(product);  // Triggers UPDATE SQL
    }


    //  Restore (undelete)
    @CachePut(cacheNames = "products",key = "#id")
    @Transactional
    public ProductResponseDTO restoreProduct(Long id) {
        ProductDetails product = productDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsActive(true);
        ProductDetails restore =productDAO.save(product);
        return mapToProductResponseDTO(restore);
    }



    //permanently remove product from db
    @CacheEvict(cacheNames = "products",key = "#id")
    @Transactional
    public void deletePermanently(Long id) {
        ProductDetails product = productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productDAO.delete(product);  //permanently  delete from db
    }





    // getAll products


    @Cacheable(cacheNames = "products")
    public List<ProductDetails> getAllProduct(){
    return productDAO.findByIsActiveTrue();
    }


    @Cacheable(cacheNames = "products",key = "#id")
    public ProductResponseDTO getProductById(Long id){
        ProductDetails productDetails = productDAO.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found : "+ id));

    return mapToProductResponseDTO(productDetails);
    }


private ProductResponseDTO mapToProductResponseDTO(ProductDetails productDetails){

    return new ProductResponseDTO(
            productDetails.getId(),
            productDetails.getProductName(),
            productDetails.getCategory(),
            productDetails.getBrand(),
            productDetails.getQuantity(),
            productDetails.getDescription(),
            productDetails.getPrice()
    );
}
}
