package com.gautam.product.controller;

import com.gautam.product.dto.CreateProductRequest;
import com.gautam.product.dto.ProductResponseDTO;
import com.gautam.product.dto.UpdateProductRequest;
import com.gautam.product.model.ProductDetails;
import com.gautam.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productdetails")
@RequiredArgsConstructor
@Tag(name = "Products",description = "Operation related to products")//group name amd description swagger ui
public class ProductController {
    @Autowired
    private final ProductService productService;

    @Operation(summary = "Create new product")//short summary nes=xt to endpoints also we can add api response
    @ApiResponse(responseCode = "201",description = "product created")
    @ApiResponse(responseCode = "400",description = "invalid payload")
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody CreateProductRequest createProductDTO){

        ProductResponseDTO productResponseDTO =productService.create(createProductDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }
//    @GetMapping("/hello")
//    public String hello(){
//        return "Hello welcome to product service";
//    }
//
    @Operation(summary = "get products")
    @GetMapping("/getAll/products")
    public List<ProductDetails> getListOfProduct(){
        return productService.getAllProduct();
    }


    @Operation(summary = "Get product by id")
    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id){

        return ResponseEntity.ok(productService.getProductById(id));
    }



//page response get product




    @Operation(summary = "Update product by id")
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @RequestBody UpdateProductRequest updateProduct){
        ProductResponseDTO productResponseDTO = productService.update(updateProduct, id);
                return ResponseEntity.ok(productResponseDTO);


     }



// soft delete -> set flag false
@Operation(summary = "Delete product by id")
@DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
     }

     //permanent delete from db
     @Operation(summary = "Permanently remove product by id")
     @DeleteMapping("/deletepermanently/{id}")
    public ResponseEntity<Void> deletePermanent(@PathVariable Long id){
        productService.deletePermanently(id);
        return ResponseEntity.noContent().build();
     }


    @Operation(summary = "Restore product by id")
    @PutMapping("/restore/{id}")
    public ResponseEntity<ProductResponseDTO> restoreProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.restoreProduct(id));

     }
}
