package com.shopflow.shpflow.service;

import com.shopflow.shpflow.dto.response.CategoryResponse;
import com.shopflow.shpflow.entity.Category;
import com.shopflow.shpflow.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;

    // 🔹 GET ALL
    public List<CategoryResponse> getAll() {
        return categoryRepo.findAll()
                .stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    // 🔹 GET BY ID
    public CategoryResponse getById(Long id) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        return CategoryResponse.fromEntity(c);
    }

    // 🔹 CREATE
    @Transactional
    public CategoryResponse create(String nom, String description, Long parentId) {

        Category parent = parentId != null
                ? categoryRepo.findById(parentId).orElse(null)
                : null;

        Category c = categoryRepo.save(Category.builder()
                .nom(nom)
                .description(description)
                .parent(parent)
                .actif(true)
                .build());

        return CategoryResponse.fromEntity(c);
    }

    // 🔹 UPDATE
    @Transactional
    public CategoryResponse update(Long id, String nom, String description) {

        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        c.setNom(nom);
        c.setDescription(description);

        return CategoryResponse.fromEntity(categoryRepo.save(c));
    }

    // 🔹 DELETE (clean version using repository)
    @Transactional
    public void delete(Long id) {

        // supprimer relations produit-catégorie
        categoryRepo.deleteProductCategoryLinks(id);

        // détacher sous-catégories
        categoryRepo.detachChildren(id);

        // supprimer catégorie
        categoryRepo.deleteCategory(id);
    }

    // 🔹 TOGGLE ACTIF
    @Transactional
    public CategoryResponse toggleActif(Long id) {

        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        c.setActif(!c.getActif());

        return CategoryResponse.fromEntity(categoryRepo.save(c));
    }
}