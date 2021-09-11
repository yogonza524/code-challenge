package com.hiberus.challenge.entrypoint;

import com.hiberus.challenge.business.ProductService;
import com.hiberus.challenge.domain.AttributeType;
import com.hiberus.challenge.domain.Product;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
public class InMemoryProductController {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final ProductService inMemoryProductService;

  @GetMapping(value = "/find/{attribute}", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<Product>> findById(@PathVariable(value = "attribute") String attribute
  , AttributeType type) {
    return Optional.ofNullable(inMemoryProductService.findBy(type, attribute))
        .map(ResponseEntity::ok)
        .orElseGet(
            () -> {
              log.error(String.format("action=findById, message=ID %s not found", attribute));
              return ResponseEntity.notFound().build();
            });
  }

  @PostMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity createProduct(@Valid @RequestBody Product product) {

    Product newProduct = inMemoryProductService.addProduct(product);

    return Optional.ofNullable(newProduct)
        .map(p -> new ResponseEntity(HttpStatus.CREATED))
        .orElse(ResponseEntity.badRequest().build());
  }

  @DeleteMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity deleteProduct(@PathVariable(value = "id") String id) {
    return Optional.ofNullable(inMemoryProductService.remove(id))
        .map(
            product -> {
              log.info(
                  String.format("action=deleteProduct, message=ID %s deleted", product.getId()));

              return ResponseEntity.ok(product);
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity updateProduct(@Valid @RequestBody Product product) {
    return Optional.ofNullable(inMemoryProductService.update(product))
        .map(
            p -> {
              log.info(String.format("action=deleteProduct, message=ID %s updated", p.getId()));

              return ResponseEntity.ok(product);
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
