package com.shopflow.shpflow.dto.response;

import com.shopflow.shpflow.entity.Category;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String nom;
    private String description;
    private Boolean actif;
    private Long parentId;

    public static CategoryResponse fromEntity(Category c) {
        if (c == null) return null;

        return CategoryResponse.builder()
                .id(c.getId())
                .nom(c.getNom())
                .description(c.getDescription())
                .actif(c.getActif())
                .parentId(c.getParent() != null ? c.getParent().getId() : null)
                .build();
    }

    public static List<CategoryResponse> fromList(List<Category> list) {
        return list.stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}