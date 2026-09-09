package com.shopflow.shpflow.service;

import com.shopflow.shpflow.entity.*;
import com.shopflow.shpflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final AddressRepository addressRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CartService cartService;

    @Transactional
    public Order placeOrder(String email, Long addressId) {
        User customer = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Cart cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));
        if (cart.getLignes().isEmpty())
            throw new RuntimeException("Le panier est vide");

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée"));

        for (CartItem item : cart.getLignes()) {
            Product p = item.getProduct();
            if (p.getStock() < item.getQuantite())
                throw new RuntimeException("Stock insuffisant pour : " + p.getNom());
            p.setStock(p.getStock() - item.getQuantite());
            productRepo.save(p);
        }

        double sousTotal = cartService.calculateSubtotal(cart);
        double remise    = cart.getRemise() != null ? cart.getRemise() : 0;
        double frais     = 5.0;
        double total     = sousTotal - remise + frais;

        Order order = Order.builder()
                .customer(customer).adresseLivraison(address)
                .sousTotal(sousTotal).fraisLivraison(frais).totalTTC(total)
                .statut(Order.Statut.PENDING).build();

        List<OrderItem> items = cart.getLignes().stream().map(ci -> {
            double prix = ci.getProduct().getPrixPromo() != null
                    ? ci.getProduct().getPrixPromo() : ci.getProduct().getPrix();
            return OrderItem.builder()
                    .order(order).product(ci.getProduct())
                    .variant(ci.getVariant())
                    .quantite(ci.getQuantite()).prixUnitaire(prix).build();
        }).collect(Collectors.toList());

        order.setLignes(items);
        Order saved = orderRepo.save(order);
        cartService.clearCart(cart);

        log.info("Commande {} créée pour {}", saved.getNumeroCommande(), email);
        return saved;
    }

    public Order getById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    public List<Order> getMyOrders(String email) {
        User customer = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return orderRepo.findByCustomerIdOrderByDateCommandeDesc(customer.getId());
    }

    public List<Order> getAll() {
        return orderRepo.findAll();
    }

    // SELLER — commandes contenant ses produits
    public List<Order> getSellerOrders(String sellerEmail) {
        User seller = userRepo.findByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Vendeur non trouvé"));
        return orderRepo.findOrdersBySeller(seller.getId());
    }

    @Transactional
    public Order updateStatut(Long id, String statut) {
        Order order = getById(id);
        order.setStatut(Order.Statut.valueOf(statut));
        Order saved = orderRepo.save(order);
        log.info("Commande {} → statut {}", saved.getNumeroCommande(), statut);
        return saved;
    }

    @Transactional
    public Order cancel(Long id, String email) {
        Order order = getById(id);
        if (!order.getCustomer().getEmail().equals(email))
            throw new RuntimeException("Non autorisé");
        if (!order.isAnnulable())
            throw new RuntimeException("Commande non annulable — statut : " + order.getStatut());

        boolean etaitPaye = order.getStatut() == Order.Statut.PAID;

        order.getLignes().forEach(item -> {
            Product p = item.getProduct();
            p.setStock(p.getStock() + item.getQuantite());
            productRepo.save(p);
        });

        order.setStatut(Order.Statut.CANCELLED);
        Order saved = orderRepo.save(order);

        if (etaitPaye) {
            log.info("REMBOURSEMENT SIMULE — Commande {} — {}€ — {}",
                    saved.getNumeroCommande(), saved.getTotalTTC(), email);
        }

        return saved;
    }
}