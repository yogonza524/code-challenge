package com.hiberus.challenge.business;

import com.hiberus.challenge.domain.Product;
import java.util.List;

public interface ProductService {

  Product addProduct(Product product);

  Product findById(String id);

  List<Product> findByName(String name);

  List<Product> findLikeDescription(String description);

  Product remove(String id);

  Boolean remove(Product product);

  List<Product> findAll();

  Product update(Product product);
}
