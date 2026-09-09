package com.shopflow.shpflow.service;

import com.shopflow.shpflow.entity.*;
import com.shopflow.shpflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public Map<String, Object> getAdminStats() {

        List<Map<String, Object>> recent = orderRepo
            .findTop5ByOrderByDateCommandeDesc().stream()
            .map(o -> Map.<String, Object>of(
                "id",             o.getId(),
                "numeroCommande", o.getNumeroCommande(),
                "statut",         o.getStatut().name(),
                "totalTTC",       o.getTotalTTC(),
                "dateCommande",   o.getDateCommande().toString(),
                "customer",       o.getCustomer().getPrenom() + " " + o.getCustomer().getNom()
            )).collect(Collectors.toList());

        List<Map<String, Object>> topProduits = productRepo
            .getTopProducts(PageRequest.of(0, 5)).stream()
            .map(row -> Map.<String, Object>of(
                "nom",            row[0],
                "quantiteVendue", ((Number) row[1]).longValue()))
            .collect(Collectors.toList());

        List<Map<String, Object>> parStatut = orderRepo
            .countByStatut().stream()
            .map(row -> Map.<String, Object>of(
                "statut", row[0].toString(),
                "count",  ((Number) row[1]).longValue()))
            .collect(Collectors.toList());

        Double revenue = orderRepo.getTotalRevenue();
        long nbVendeurs = userRepo.countByRole(User.Role.SELLER);
        long nbClients  = userRepo.countByRole(User.Role.CUSTOMER);

        return Map.of(
            "totalProduits",      productRepo.count(),
            "totalCategories",    categoryRepo.count(),
            "totalCommandes",     orderRepo.count(),
            "totalUtilisateurs",  userRepo.count(),
            "totalVendeurs",      nbVendeurs,
            "totalClients",       nbClients,
            "chiffreAffaires",    revenue != null ? revenue : 0.0,
            "commandesRecentes",  recent,
            "topProduits",        topProduits,
            "commandesParStatut", parStatut
        );
    }

    public Map<String, Object> getSellerStats(String sellerEmail) {

        User seller = userRepo.findByEmail(sellerEmail)
            .orElseThrow(() -> new RuntimeException("Vendeur non trouvé"));

        List<Product> mesProds = productRepo.findBySellerId(seller.getId());
        List<Order> mesCommandes = orderRepo.findOrdersBySeller(seller.getId());

        long commandesEnAttente = mesCommandes.stream()
            .filter(o -> o.getStatut() == Order.Statut.PENDING
                      || o.getStatut() == Order.Statut.PAID)
            .count();

        double revenuTotal = mesCommandes.stream()
            .filter(o -> o.getStatut() == Order.Statut.DELIVERED)
            .mapToDouble(Order::getTotalTTC).sum();

        List<Map<String, Object>> alertesStock = mesProds.stream()
            .filter(p -> p.getStock() < 10 && p.getActif())
            .map(p -> Map.<String, Object>of(
                "id", p.getId(), "nom", p.getNom(), "stock", p.getStock()))
            .collect(Collectors.toList());

        List<Map<String, Object>> dernieres = mesCommandes.stream()
            .limit(5)
            .map(o -> Map.<String, Object>of(
                "id",             o.getId(),
                "numeroCommande", o.getNumeroCommande(),
                "statut",         o.getStatut().name(),
                "totalTTC",       o.getTotalTTC(),
                "dateCommande",   o.getDateCommande().toString(),
                "customer",       o.getCustomer().getPrenom() + " " + o.getCustomer().getNom()
            )).collect(Collectors.toList());

        return Map.of(
            "totalProduits",      mesProds.size(),
            "produitsActifs",     mesProds.stream().filter(Product::getActif).count(),
            "commandesEnAttente", commandesEnAttente,
            "totalCommandes",     mesCommandes.size(),
            "revenuTotal",        revenuTotal,
            "alertesStockFaible", alertesStock,
            "dernieresCommandes", dernieres
        );
    }
}