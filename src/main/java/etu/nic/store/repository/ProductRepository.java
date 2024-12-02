package etu.nic.store.repository;

import etu.nic.store.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategories_Id(Long categoriesId, Pageable pageable);
}
