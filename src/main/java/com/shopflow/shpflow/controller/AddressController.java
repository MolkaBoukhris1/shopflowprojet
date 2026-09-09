// src/main/java/com/shopflow/shpflow/controller/AddressController.java
// REMPLACE l'ancien — utilise AddressRequest, retourne AddressResponse
package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.dto.request.AddressRequest;
import com.shopflow.shpflow.dto.response.AddressResponse;
import com.shopflow.shpflow.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getMyAddresses(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
            addressService.getMyAddresses(user.getUsername())
                .stream().map(AddressResponse::fromEntity).toList());
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(
            @Valid @RequestBody AddressRequest req,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            AddressResponse.fromEntity(addressService.create(
                user.getUsername(), req.getRue(), req.getVille(),
                req.getCodePostal(), req.getPays(), req.getPrincipal())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
