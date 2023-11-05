package tech.ailef.snapadmin.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.ailef.snapadmin.test.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
