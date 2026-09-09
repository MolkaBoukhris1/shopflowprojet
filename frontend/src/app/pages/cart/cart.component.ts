// src/app/pages/cart/cart.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CartService } from '../../services/cart.service';
import { CartResponse } from '../../models/order';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: CartResponse | null = null;
  loading = false;
  couponCode = '';
  applyingCoupon = false;

  constructor(
    public cartSvc: CartService,
    private router: Router,
    private snack:  MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.loading = true;
    this.cartSvc.getCart().subscribe({
      next: cart => { this.cart = cart; this.loading = false; },
      error: ()   => { this.loading = false; }
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
  get fraisLivraison(): number { return 5; }
  get total(): number { return this.sousTotal - this.remise + this.fraisLivraison; }

  updateQty(itemId: number, qty: number): void {
    this.cartSvc.updateItem(itemId, qty).subscribe({
      next: cart => this.cart = cart,
      error: err => this.snack.open(err.message, 'Fermer', { duration: 4000 })
    });
  }

  removeItem(itemId: number): void {
    this.cartSvc.removeItem(itemId).subscribe({
      next: cart => this.cart = cart,
      error: err => this.snack.open(err.message, 'Fermer', { duration: 4000 })
    });
  }

  applyCoupon(): void {
    if (!this.couponCode.trim()) return;
    this.applyingCoupon = true;
    this.cartSvc.applyCoupon(this.couponCode.trim().toUpperCase()).subscribe({
      next: cart => {
        this.cart = cart;
        this.snack.open('Code promo appliqué !', 'OK', { duration: 3000 });
        this.applyingCoupon = false;
      },
      error: err => {
        this.snack.open(err.message, 'Fermer', { duration: 4000 });
        this.applyingCoupon = false;
      }
    });
  }

  removeCoupon(): void {
    this.cartSvc.removeCoupon().subscribe({
      next: cart => {
        this.cart = cart;
        this.couponCode = '';
        this.snack.open('Code promo retiré', 'OK', { duration: 2000 });
      }
    });
  }

  goToCheckout(): void {
    this.router.navigate(['/checkout']);
  }
}