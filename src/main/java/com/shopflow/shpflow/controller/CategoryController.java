package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.dto.response.CategoryResponse;
import com.shopflow.shpflow.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(@RequestBody Map<String, Object> body) {

        Long parentId = body.get("parentId") != null
                ? Long.valueOf(body.get("parentId").toString())
                : null;

        return ResponseEntity.status(HttpStatus.CREATED).body(
                categoryService.create(
                        (String) body.get("nom"),
                        (String) body.get("description"),
                        parentId
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(
                categoryService.update(id, body.get("nom"), body.get("description"))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> toggleActif(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.toggleActif(id));
    }
}