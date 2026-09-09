// src/app/services/cart.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { CartResponse, CartItemRequest } from '../models/order';

@Injectable({ providedIn: 'root' })
export class CartService {
  private apiUrl = `${environment.apiUrl}/cart`;

  // State local pour le badge panier
  private itemCountSubject = new BehaviorSubject<number>(0);
  public itemCount$ = this.itemCountSubject.asObservable();

  constructor(private http: HttpClient) {}

  // Récupérer le panier depuis le backend
  getCart(): Observable<CartResponse> {
    return this.http.get<CartResponse>(this.apiUrl).pipe(
      tap(cart => this.updateCount(cart))
    );
  }

  // Ajouter un article
  addItem(request: CartItemRequest): Observable<CartResponse> {
    return this.http.post<CartResponse>(`${this.apiUrl}/items`, request).pipe(
      tap(cart => this.updateCount(cart))
    );
  }

  // Modifier la quantité
  updateItem(itemId: number, quantite: number): Observable<CartResponse> {
    return this.http.put<CartResponse>(
      `${this.apiUrl}/items/${itemId}`, { quantite }).pipe(
      tap(cart => this.updateCount(cart))
    );
  }

  // Supprimer un article
  removeItem(itemId: number): Observable<CartResponse> {
    return this.http.delete<CartResponse>(`${this.apiUrl}/items/${itemId}`).pipe(
      tap(cart => this.updateCount(cart))
    );
  }

  // Appliquer un coupon
  applyCoupon(code: string): Observable<CartResponse> {
    return this.http.post<CartResponse>(`${this.apiUrl}/coupon`, { code }).pipe(
      tap(cart => this.updateCount(cart))
    );
  }

  // Retirer le coupon
  removeCoupon(): Observable<CartResponse> {
    return this.http.delete<CartResponse>(`${this.apiUrl}/coupon`).pipe(
      tap(cart => this.updateCount(cart))
    );
  }

  // Calcul local du total
  calculateTotal(cart: CartResponse): number {
    const sousTotal = cart.lignes.reduce((sum, item) => {
      const prix = item.product.prixPromo ?? item.product.prix;
      return sum + prix * item.quantite;
    }, 0);
    const remise = cart.remise ?? 0;
    return sousTotal - remise + 5; // 5 TND frais livraison
  }

  private updateCount(cart: CartResponse): void {
    const count = cart.lignes.reduce((sum, item) => sum + item.quantite, 0);
    this.itemCountSubject.next(count);
  }

  resetCount(): void {
    this.itemCountSubject.next(0);
  }
}