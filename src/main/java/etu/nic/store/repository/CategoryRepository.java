package etu.nic.store.repository;

import etu.nic.store.model.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParent_Id(Long id);
}
