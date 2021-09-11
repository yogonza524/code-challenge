package com.hiberus.challenge.entrypoint;

import static org.junit.jupiter.api.Assertions.*;

import com.hiberus.challenge.business.ProductService;
import com.hiberus.challenge.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class InMemoryProductControllerTest {

  @Mock private ProductService productService;
  @InjectMocks private InMemoryProductController inMemoryProductController;

  private final String ID = "PR-00001";

  @Test
  public void shouldPassWhenProductExists() {

    Mockito.when(productService.findById(Mockito.anyString()))
        .thenReturn(
            Product.builder()
                .id(ID)
                .description("Hiberus Mouse")
                .name("Mouse Hiberus XZ3")
                .build());

    ResponseEntity<Product> found = inMemoryProductController.findById(ID);

    Assertions.assertNotNull(found);
    Assertions.assertEquals(HttpStatus.OK, found.getStatusCode());
    Assertions.assertEquals(ID, found.getBody().getId());
    Assertions.assertEquals("Hiberus Mouse", found.getBody().getDescription());
    Assertions.assertEquals("Mouse Hiberus XZ3", found.getBody().getName());
  }

  @Test
  public void shouldPassWhenProductIdNotFound() {

    Mockito.when(productService.findById(Mockito.anyString())).thenReturn(null);

    ResponseEntity<Product> notFound = inMemoryProductController.findById(ID);

    Assertions.assertNotNull(notFound);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
  }

  @Test
  public void shouldPassWhenProductIsCreated() {

    Product newProduct = Product.builder().id(ID).name("Hiberus CPU").description("Just a CPU").build();

    Mockito.when(productService.addProduct(
            Mockito.any())).thenReturn(newProduct);

    ResponseEntity<Product> created = inMemoryProductController.createProduct(newProduct);

    Assertions.assertNotNull(created);
    Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
  }
}
