// src/app/guards/role.guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  // Protège les routes réservées ADMIN et SELLER
  canActivate(): boolean {
    if (this.auth.isAdmin || this.auth.isSeller) return true;
    this.router.navigate(['/produits']);
    return false;
  }
}