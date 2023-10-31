package tech.ailef.dbadmin.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.ailef.dbadmin.test.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
