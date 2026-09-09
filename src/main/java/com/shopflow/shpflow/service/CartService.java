package com.shopflow.shpflow.service;

import com.shopflow.shpflow.dto.response.CartResponse;
import com.shopflow.shpflow.entity.*;
import com.shopflow.shpflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CouponRepository couponRepo;

    public Cart getOrCreateEntity(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return cartRepo.findByCustomerId(user.getId())
                .orElseGet(() -> cartRepo.save(Cart.builder().customer(user).build()));
    }

    public CartResponse getCart(String email) {
        return CartResponse.fromEntity(getOrCreateEntity(email));
    }

    @Transactional
    public CartResponse addItem(String email, Long productId, Integer quantite) {

        Cart cart = getOrCreateEntity(email);

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (product.getStock() < quantite)
            throw new RuntimeException("Stock insuffisant");

        cart.getLignes().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        existing -> existing.setQuantite(existing.getQuantite() + quantite),
                        () -> cart.getLignes().add(
                                CartItem.builder()
                                        .cart(cart)
                                        .product(product)
                                        .quantite(quantite)
                                        .build()
                        )
                );

        cartRepo.save(cart);

        return CartResponse.fromEntity(cart);
    }

    @Transactional
    public CartResponse updateItem(String email, Long itemId, Integer quantite) {

        Cart cart = getOrCreateEntity(email);

        cart.getLignes().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    if (quantite <= 0)
                        cart.getLignes().remove(item);
                    else
                        item.setQuantite(quantite);
                });

        cartRepo.save(cart);

        return CartResponse.fromEntity(cart);
    }

    @Transactional
    public CartResponse removeItem(String email, Long itemId) {

        Cart cart = getOrCreateEntity(email);

        cart.getLignes().removeIf(i -> i.getId().equals(itemId));

        cartRepo.save(cart);

        return CartResponse.fromEntity(cart);
    }

    @Transactional
    public CartResponse applyCoupon(String email, String code) {

        Cart cart = getOrCreateEntity(email);

        Coupon coupon = couponRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Code invalide"));

        if (!coupon.isValide())
            throw new RuntimeException("Coupon expiré");

        double sousTotal = calculateSubtotal(cart);

        cart.setCouponCode(code);
        cart.setRemise(coupon.calculerRemise(sousTotal));

        coupon.setUsagesActuels(coupon.getUsagesActuels() + 1);
        couponRepo.save(coupon);

        cartRepo.save(cart);

        return CartResponse.fromEntity(cart);
    }

    public double calculateSubtotal(Cart cart) {
        return cart.getLignes().stream().mapToDouble(i -> {
            double prix = i.getProduct().getPrixPromo() != null
                    ? i.getProduct().getPrixPromo()
                    : i.getProduct().getPrix();
            return prix * i.getQuantite();
        }).sum();
    }
    @Transactional
    public void clearCart(Cart cart) {
        cart.getLignes().clear();
        cart.setCouponCode(null);
        cart.setRemise(null);
        cartRepo.save(cart);
    }
}