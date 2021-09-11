package com.hiberus.challenge.business;

import com.hiberus.challenge.domain.Product;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InMemoryProductService implements ProductService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final ConcurrentHashMap<String, Product> productDatabase;

  @Override
  public Product addProduct(Product product) {

    if (productDatabase.containsKey(product.getId())) {
      log.warn(
          String.format(
              "action=addProduct, message=Product ID %s already exists", product.getId()));
      return null;
    }
    return productDatabase.computeIfAbsent(product.getId(), id -> product);
  }

  @Override
  public Product findById(String id) {
    return productDatabase.get(id);
  }

  @Override
  public List<Product> findByName(String name) {
    return productDatabase.values().parallelStream()
        .filter(
            product ->
                Objects.nonNull(product.getName())
                    && product
                        .getName()
                        .toLowerCase(Locale.ROOT)
                        .contains(name.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }

  @Override
  public List<Product> findLikeDescription(String description) {
    return productDatabase.values().parallelStream()
        .filter(
            product ->
                Objects.nonNull(product.getDescription())
                    && product
                        .getDescription()
                        .toLowerCase(Locale.ROOT)
                        .contains(description.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }

  @Override
  public Product remove(String id) {
    return productDatabase.remove(id);
  }

  @Override
  public Boolean remove(Product product) {
    return productDatabase.remove(product.getId(), product);
  }

  @Override
  public List<Product> findAll() {
    return productDatabase.values().stream().collect(Collectors.toList());
  }

  @Override
  public Product update(Product product) {
    return productDatabase.computeIfPresent(product.getId(), (id, prod) -> product);
  }
}
