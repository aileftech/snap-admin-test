package tech.ailef.snapadmin.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.ailef.snapadmin.auth.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
