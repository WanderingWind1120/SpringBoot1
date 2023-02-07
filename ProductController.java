package com.example.practice.controllers.controllers;

import com.example.practice.models.Product;
import com.example.practice.models.ResponseObject;
import com.example.practice.repositories.ProductRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    @Autowired
    private ProductRepository repository; // Tự tạo bean trong các class implement rôi inject vào class này từ Interface

    @GetMapping("")

    public List<Product> getAllProducts(){
        return repository.findAll(); // trong JPA đã tích hợp các method này
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById (@PathVariable Long id){
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent() ?
            ResponseEntity.status(HttpStatus.OK).
                    body(new ResponseObject("ok", "Query Product Successfully", foundProduct));
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("Failed", "Cannot find product with id =" + id, ""));
    }
    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertProduct (@RequestBody Product newProduct){
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if (foundProducts.size() > 0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new ResponseObject("Failed", "Product Name alreade taken", ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok"
                ,"Insert Product successfully", repository.save(newProduct)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateProduct (@RequestBody Product newProduct, @PathVariable Long id){
        Product updatedProduct = repository.findById().map(product -> {
            product.setProductName(newProduct.getProductName());
            product.setYear(newProduct.getYear());
            product.setPrice(newProduct.getPrice());
            return repository.save(newProduct);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return repository.save(newProduct);
        });
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Update Product successfully", updatedProduct)

        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exist = repository.existsById(id);
        if(exist){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Delete product successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Failed","Cannot find product to delete",""));
    }




}
