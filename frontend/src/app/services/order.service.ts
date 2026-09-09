// src/app/services/order.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { OrderRequest, OrderResponse, DashboardStats } from '../models/order';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private apiUrl = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  placeOrder(request: OrderRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(this.apiUrl, request);
  }

  getMyOrders(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(`${this.apiUrl}/my`);
  }

  getAllOrders(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(this.apiUrl);
  }

  getById(id: number): Observable<OrderResponse> {
    return this.http.get<OrderResponse>(`${this.apiUrl}/${id}`);
  }

  updateStatus(id: number, statut: string): Observable<OrderResponse> {
    return this.http.put<OrderResponse>(`${this.apiUrl}/${id}/status`, { statut });
  }

  cancelOrder(id: number): Observable<OrderResponse> {
    return this.http.put<OrderResponse>(`${this.apiUrl}/${id}/cancel`, {});
  }

  getStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${environment.apiUrl}/dashboard/admin`);
  }
}