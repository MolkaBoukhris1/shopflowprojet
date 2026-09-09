// src/app/app.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { CartService } from './services/cart.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  cartCount$: Observable<number>;

  constructor(
    public auth: AuthService,
    public cartSvc: CartService
  ) {
    this.cartCount$ = this.cartSvc.itemCount$;
  }

  ngOnInit(): void {
    // Charger le nombre d'articles du panier au démarrage si connecté
    if (this.auth.isLoggedIn && this.auth.isCustomer) {
      this.cartSvc.getCart().subscribe();
    }
  }

  logout(): void {
    this.cartSvc.resetCount();
    this.auth.logout();
  }
}