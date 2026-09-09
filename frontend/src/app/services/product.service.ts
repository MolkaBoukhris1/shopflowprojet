// src/app/services/product.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Product, ProductRequest, PageResponse } from '../models/product';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private apiUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}

  // GET avec filtres + pagination (correspond au backend ShopFlow)
  getAll(params?: {
    search?: string;
    prixMin?: number;
    prixMax?: number;
    page?: number;
    size?: number;
    sort?: string;
  }): Observable<PageResponse<Product>> {
    let httpParams = new HttpParams();
    if (params?.search)  httpParams = httpParams.set('search', params.search);
    if (params?.prixMin) httpParams = httpParams.set('prixMin', params.prixMin);
    if (params?.prixMax) httpParams = httpParams.set('prixMax', params.prixMax);
    if (params?.page !== undefined) httpParams = httpParams.set('page', params.page);
    if (params?.size)    httpParams = httpParams.set('size', params.size);
    if (params?.sort)    httpParams = httpParams.set('sort', params.sort);
    return this.http.get<PageResponse<Product>>(this.apiUrl, { params: httpParams });
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  getTopSelling(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/top-selling`);
  }

  getPromo(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/promo`);
  }

  create(product: ProductRequest): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  update(id: number, product: ProductRequest): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}