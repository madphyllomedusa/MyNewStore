package etu.nic.store.repository;

import etu.nic.store.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParent_Id(Long id);

    @Query(value = "WITH RECURSIVE subcategories AS (" +
            "SELECT * FROM categories WHERE id = :categoryId " +
            "UNION ALL " +
            "SELECT c.* FROM categories c " +
            "INNER JOIN subcategories sc ON c.parent_id = sc.id " +
            ") " +
            "SELECT * FROM subcategories",
            nativeQuery = true)
    List<Category> findAllSubcategories(@Param("categoryId") Long id);
}
