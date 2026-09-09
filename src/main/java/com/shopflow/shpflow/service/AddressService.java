// src/main/java/com/shopflow/shpflow/service/AddressService.java
package com.shopflow.shpflow.service;

import com.shopflow.shpflow.entity.Address;
import com.shopflow.shpflow.entity.User;
import com.shopflow.shpflow.repository.AddressRepository;
import com.shopflow.shpflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepo;
    private final UserRepository userRepo;

    public List<Address> getMyAddresses(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return addressRepo.findByUserId(user.getId());
    }

    @Transactional
    public Address create(String email, String rue, String ville,
                          String codePostal, String pays, Boolean principal) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (Boolean.TRUE.equals(principal)) {
            addressRepo.findByUserIdAndPrincipalTrue(user.getId())
                    .ifPresent(a -> { a.setPrincipal(false); addressRepo.save(a); });
        }

        return addressRepo.save(Address.builder()
                .user(user).rue(rue).ville(ville).codePostal(codePostal)
                .pays(pays).principal(principal != null ? principal : false).build());
    }

    @Transactional
    public void delete(Long id) { addressRepo.deleteById(id); }
}