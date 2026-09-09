// src/app/pages/checkout/checkout.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { CartResponse, Address } from '../../models/order';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  addressForm: FormGroup;
  cart: CartResponse | null = null;
  addresses: Address[] = [];
  selectedAddressId: number | null = null;
  loading   = false;
  addingNew = false;

  constructor(
    private fb:       FormBuilder,
    private cartSvc:  CartService,
    private orderSvc: OrderService,
    private http:     HttpClient,
    private router:   Router,
    private snack:    MatSnackBar
  ) {
    this.addressForm = this.fb.group({
      rue:        ['', Validators.required],
      ville:      ['', Validators.required],
      codePostal: ['', Validators.required],
      pays:       ['Tunisie', Validators.required],
      principal:  [false]
    });
  }

  ngOnInit(): void {
    this.loadCart();
    this.loadAddresses();
  }

  loadCart(): void {
    this.cartSvc.getCart().subscribe(cart => this.cart = cart);
  }

  loadAddresses(): void {
    this.http.get<Address[]>(`${environment.apiUrl}/addresses`).subscribe({
      next: addrs => {
        this.addresses = addrs;
        // Sélectionner l'adresse principale par défaut
        const principal = addrs.find(a => a.principal);
        if (principal) this.selectedAddressId = principal.id!;
        else if (addrs.length > 0) this.selectedAddressId = addrs[0].id!;
        else this.addingNew = true;
      },
      error: () => { this.addingNew = true; }
    });
  }

  saveAddress(): void {
    if (this.addressForm.invalid) return;
    this.http.post<Address>(
      `${environment.apiUrl}/addresses`,
      this.addressForm.value
    ).subscribe({
      next: addr => {
        this.addresses.push(addr);
        this.selectedAddressId = addr.id!;
        this.addingNew = false;
        this.snack.open('Adresse enregistrée', 'OK', { duration: 2000 });
      },
      error: err => this.snack.open(err.message, 'Fermer', { duration: 4000 })
    });
  }

  get sousTotal(): number {
    if (!this.cart) return 0;
    return this.cart.lignes.reduce((sum, item) => {
      const prix = item.product.prixPromo ?? item.product.prix;
      return sum + prix * item.quantite;
    }, 0);
  }

  get remise(): number { return this.cart?.remise ?? 0; }
  get total(): number  { return this.sousTotal - this.remise + 5; }

  placeOrder(): void {
    if (!this.selectedAddressId) {
      this.snack.open('Sélectionnez une adresse', 'Fermer', { duration: 3000 });
      return;
    }

    this.loading = true;
    this.orderSvc.placeOrder({ addressId: this.selectedAddressId }).subscribe({
      next: order => {
        this.cartSvc.resetCount();
        this.snack.open(
          `Commande ${order.numeroCommande} passée avec succès !`,
          'OK', { duration: 5000 }
        );
        this.router.navigate(['/produits']);
      },
      error: err => {
        this.snack.open(err.message, 'Fermer', { duration: 4000 });
        this.loading = false;
      },
      complete: () => { this.loading = false; }
    });
  }
}