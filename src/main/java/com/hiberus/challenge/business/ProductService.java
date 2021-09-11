package com.hiberus.challenge.business;

import com.hiberus.challenge.domain.AttributeType;
import com.hiberus.challenge.domain.Product;
import java.util.List;

public interface ProductService {

  Product addProduct(Product product);

  List<Product> findBy(AttributeType type, String attribute);

  Product remove(String id);


  List<Product> findAll();

  Product update(Product product);
}
