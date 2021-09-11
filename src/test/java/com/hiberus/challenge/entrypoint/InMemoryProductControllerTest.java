package com.hiberus.challenge.entrypoint;

import com.hiberus.challenge.business.ProductService;
import com.hiberus.challenge.domain.AttributeType;
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

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class InMemoryProductControllerTest {

  @Mock private ProductService productService;
  @InjectMocks private InMemoryProductController inMemoryProductController;

  private final String ID = "PR-00001";
  private final Product product = Product.builder().id(ID).name("Hiberus CPU").description("Just a CPU").build();

  @Test
  public void shouldPassWhenProductExists() {

    Mockito.when(productService.findBy(Mockito.any(), Mockito.anyString()))
        .thenReturn(
                Arrays.asList(Product.builder()
                        .id(ID)
                        .description("Hiberus Mouse")
                        .name("Mouse Hiberus XZ3")
                        .build()));

    ResponseEntity<List<Product>> found = inMemoryProductController.findById(ID, AttributeType.ID);

    Assertions.assertNotNull(found);
    Product product = found.getBody().get(0);
    Assertions.assertEquals(HttpStatus.OK, found.getStatusCode());
    Assertions.assertEquals(ID, product.getId());
    Assertions.assertEquals("Hiberus Mouse", product.getDescription());
    Assertions.assertEquals("Mouse Hiberus XZ3", product.getName());
  }

  @Test
  public void shouldPassWhenProductIdNotFound() {

    Mockito.when(productService.findBy(Mockito.any(), Mockito.anyString())).thenReturn(null);

    ResponseEntity<List<Product>> notFound = inMemoryProductController.findById(ID, AttributeType.DESCRIPTION);

    Assertions.assertNotNull(notFound);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
  }

  @Test
  public void shouldPassWhenProductIsCreated() {

    Mockito.when(productService.addProduct(
            Mockito.any())).thenReturn(product);

    ResponseEntity<Product> created = inMemoryProductController.createProduct(product);

    Assertions.assertNotNull(created);
    Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
  }

  @Test
  public void shouldPassWhenDeleteIsSuccess() {

    Mockito.when(productService.remove(Mockito.anyString()))
            .thenReturn(product);

    ResponseEntity deleted = inMemoryProductController.deleteProduct(ID);

    Assertions.assertNotNull(deleted);
    Assertions.assertEquals(HttpStatus.OK, deleted.getStatusCode());
  }

  @Test
  public void shouldPassWhenDeleteIsNotFound() {

    Mockito.when(productService.remove(Mockito.anyString()))
            .thenReturn(null);

    ResponseEntity deleted = inMemoryProductController.deleteProduct(ID);

    Assertions.assertNotNull(deleted);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, deleted.getStatusCode());
  }

  @Test
  public void shouldPassWhenUpdateIsSuccess() {

    Mockito.when(productService.update(Mockito.any()))
            .thenReturn(product);

    ResponseEntity updated = inMemoryProductController.updateProduct(product);

    Assertions.assertNotNull(updated);
    Assertions.assertEquals(HttpStatus.OK, updated.getStatusCode());
  }

  @Test
  public void shouldPassWhenUpdateFailWithNotFound() {

    Mockito.when(productService.update(Mockito.any()))
            .thenReturn(null);

    ResponseEntity updated = inMemoryProductController.updateProduct(product);

    Assertions.assertNotNull(updated);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, updated.getStatusCode());
  }
}
