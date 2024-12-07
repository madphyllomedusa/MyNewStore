package etu.nic.store.specification;

import org.springframework.data.jpa.domain.Specification;
import etu.nic.store.model.entity.Product;
import etu.nic.store.model.entity.Category;

import javax.persistence.criteria.Join;
import java.util.List;

public class ProductSpecifications {
    private ProductSpecifications() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Product> belongsToCategories(List<Long> categoryIds) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> categoryJoin = root.join("categories");
            return categoryJoin.get("id").in(categoryIds);
        };
    }

    public static Specification<Product> sortByPrice(String sortBy) {
        return (root, query, criteriaBuilder) -> {
            if ("priceAsc".equalsIgnoreCase(sortBy)) {
                query.orderBy(criteriaBuilder.asc(root.get("price")));
            } else if ("priceDesc".equalsIgnoreCase(sortBy)) {
                query.orderBy(criteriaBuilder.desc(root.get("price")));
            }
            return null;
        };
    }
}

