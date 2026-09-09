package com.shopflow.shpflow.service;

import com.shopflow.shpflow.dto.response.ProductResponse;
import com.shopflow.shpflow.entity.*;
import com.shopflow.shpflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;

    public Page<ProductResponse> getAll(String search, Long categorieId,
                                        Double prixMin, Double prixMax,
                                        int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("dateCreation").descending());

        return productRepo.searchProducts(search, categorieId, prixMin, prixMax, pageable)
                .map(ProductResponse::fromEntity);
    }

    public List<ProductResponse> getMyProducts(String sellerEmail) {
        User seller = userRepo.findByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Vendeur non trouvé"));

        return productRepo.findBySellerId(seller.getId())
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    public ProductResponse getById(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + id));

        return ProductResponse.fromEntity(p);
    }

    @Transactional
    public ProductResponse create(String nom, String description, Double prix,
                                 Double prixPromo, Integer stock, String images,
                                 Set<Long> categoryIds, String sellerEmail) {

        User seller = userRepo.findByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Vendeur non trouvé"));

        Set<Category> cats = new HashSet<>(categoryRepo.findAllById(categoryIds));

        Product p = productRepo.save(Product.builder()
                .seller(seller)
                .nom(nom)
                .description(description)
                .prix(prix)
                .prixPromo(prixPromo)
                .stock(stock)
                .images(images)
                .categories(cats)
                .build());

        return ProductResponse.fromEntity(p);
    }

    @Transactional
    public ProductResponse update(Long id, String nom, String description, Double prix,
                                 Double prixPromo, Integer stock, String images,
                                 Set<Long> categoryIds, String requestingEmail) {

        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        User requester = userRepo.findByEmail(requestingEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        boolean isOwner = p.getSeller().getEmail().equals(requestingEmail);
        boolean isAdmin = requester.getRole() == User.Role.ADMIN;

        if (!isOwner && !isAdmin)
            throw new RuntimeException("Non autorisé");

        Set<Category> cats = new HashSet<>(categoryRepo.findAllById(categoryIds));

        p.setNom(nom);
        p.setDescription(description);
        p.setPrix(prix);
        p.setPrixPromo(prixPromo);
        p.setStock(stock);
        p.setImages(images);
        p.setCategories(cats);

        return ProductResponse.fromEntity(productRepo.save(p));
    }

    @Transactional
    public void softDelete(Long id, String requestingEmail) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        User requester = userRepo.findByEmail(requestingEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        boolean isOwner = p.getSeller().getEmail().equals(requestingEmail);
        boolean isAdmin = requester.getRole() == User.Role.ADMIN;

        if (!isOwner && !isAdmin)
            throw new RuntimeException("Non autorisé");

        p.setActif(false);
        productRepo.save(p);
    }

    @Transactional
    public ProductResponse toggleActif(Long id, String requestingEmail) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        User requester = userRepo.findByEmail(requestingEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        boolean isOwner = p.getSeller().getEmail().equals(requestingEmail);
        boolean isAdmin = requester.getRole() == User.Role.ADMIN;

        if (!isOwner && !isAdmin)
            throw new RuntimeException("Non autorisé");

        p.setActif(!p.getActif());

        return ProductResponse.fromEntity(productRepo.save(p));
    }

    @Transactional
    public ProductResponse adjustStock(Long id, Integer delta, String sellerEmail) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        User requester = userRepo.findByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        boolean isOwner = p.getSeller().getEmail().equals(sellerEmail);
        boolean isAdmin = requester.getRole() == User.Role.ADMIN;

        if (!isOwner && !isAdmin)
            throw new RuntimeException("Non autorisé");

        int newStock = p.getStock() + delta;

        if (newStock < 0)
            throw new RuntimeException("Stock négatif interdit");

        p.setStock(newStock);

        return ProductResponse.fromEntity(productRepo.save(p));
    }

    public List<ProductResponse> getTopSelling() {
        return productRepo.findTopSelling(PageRequest.of(0, 10))
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    public List<ProductResponse> getPromo() {
        return productRepo.findPromoProducts()
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }
}