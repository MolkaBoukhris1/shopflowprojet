// src/app/models/user.ts

export interface User {
  id: number;
  prenom: string;
  nom: string;
  email: string;
  role: 'ADMIN' | 'SELLER' | 'CUSTOMER';
  actif?: boolean;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  prenom: string;
  nom: string;
  email: string;
  password: string;
  role: 'CUSTOMER' | 'SELLER';
  nomBoutique?: string;
  descriptionBoutique?: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}