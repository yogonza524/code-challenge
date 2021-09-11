package com.hiberus.challenge.business;

import com.hiberus.challenge.domain.AttributeType;
import com.hiberus.challenge.domain.Product;

import java.util.*;
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
  public List<Product> findBy(AttributeType type, String attribute) {
    List<Product> products = null;
    switch(type) {
      case ID: products = findById(attribute); break;
      case NAME: products = findByName(attribute); break;
      case DESCRIPTION: products = findLikeDescription(attribute); break;
    }
    return products;
  }

  private List<Product> findById(String attribute) {
    return Optional.ofNullable(productDatabase.get(attribute)).map(Arrays::asList).orElse(null);
  }

  private List<Product> findByName(String name) {
    return Optional.ofNullable(
            productDatabase.values().parallelStream()
                    .filter(
                            product ->
                                    Objects.nonNull(product.getName())
                                            && product
                                            .getName()
                                            .toLowerCase(Locale.ROOT)
                                            .contains(name.toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList())
    ).orElse(null);
  }

  private List<Product> findLikeDescription(String description) {
    return Optional.ofNullable(
            productDatabase.values().parallelStream()
                    .filter(
                            product ->
                                    Objects.nonNull(product.getDescription())
                                            && product
                                            .getDescription()
                                            .toLowerCase(Locale.ROOT)
                                            .contains(description.toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList())
    ).orElse(null);
  }

  @Override
  public Product remove(String id) {
    return productDatabase.remove(id);
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
