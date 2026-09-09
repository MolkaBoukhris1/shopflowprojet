// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { User, LoginRequest, RegisterRequest, AuthResponse } from '../models/user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  private currentUserSubject = new BehaviorSubject<User | null>(
    this.getUserFromStorage()
  );
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  // ---- Getters ----
  get currentUser(): User | null { return this.currentUserSubject.value; }
  get isLoggedIn(): boolean      { return !!this.getToken(); }
  get isAdmin(): boolean         { return this.currentUser?.role === 'ADMIN'; }
  get isSeller(): boolean        { return this.currentUser?.role === 'SELLER'; }
  get isCustomer(): boolean      { return this.currentUser?.role === 'CUSTOMER'; }
  get isAdminOrSeller(): boolean { return this.isAdmin || this.isSeller; }

  getToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  // ---- API Calls ----
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(res => this.saveSession(res))
    );
  }

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data).pipe(
      tap(res => this.saveSession(res))
    );
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  // ---- Session ----
  private saveSession(res: AuthResponse): void {
    localStorage.setItem('accessToken', res.accessToken);
    localStorage.setItem('refreshToken', res.refreshToken);
    localStorage.setItem('user', JSON.stringify(res.user));
    this.currentUserSubject.next(res.user);
  }

  private getUserFromStorage(): User | null {
    const u = localStorage.getItem('user');
    return u ? JSON.parse(u) : null;
  }

  // Redirection selon le rôle après login
  redirectAfterLogin(role: string): void {
    switch (role) {
      case 'ADMIN':    this.router.navigate(['/dashboard']); break;
      case 'SELLER':   this.router.navigate(['/dashboard']); break;
      case 'CUSTOMER': this.router.navigate(['/produits']);  break;
      default:         this.router.navigate(['/produits']);
    }
  }
}