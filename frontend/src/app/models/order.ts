// src/app/models/order.ts
import { Product } from './product';

export interface CartItem {
  product: Product;
  quantity: number;
}

export interface Address {
  id?: number;
  rue: string;
  ville: string;
  codePostal: string;
  pays: string;
  principal?: boolean;
}

export interface OrderItemRequest {
  productId: number;
  variantId?: number;
  quantite: number;
}

export interface OrderRequest {
  addressId: number;
}

export interface OrderItemResponse {
  productId: number;
  productNom: string;
  quantite: number;
  prixUnitaire: number;
}

export interface OrderResponse {
  id: number;
  numeroCommande: string;
  statut: 'PENDING' | 'PAID' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  sousTotal: number;
  fraisLivraison: number;
  totalTTC: number;
  dateCommande: string;
  lignes: OrderItemResponse[];
  adresseLivraison?: Address;
}

export interface CartItemRequest {
  productId: number;
  variantId?: number;
  quantite: number;
}

export interface CartResponse {
  id: number;
  lignes: {
    id: number;
    product: Product;
    quantite: number;
  }[];
  couponCode?: string;
  remise?: number;
  dateModification: string;
}

export interface DashboardStats {
  totalProduits: number;
  totalCategories: number;
  totalCommandes: number;
  totalUtilisateurs: number;
  chiffreAffaires: number;
  commandesRecentes: {
    id: number;
    numeroCommande: string;
    statut: string;
    totalTTC: number;
    dateCommande: string;
  }[];
  topProduits: { nom: string; quantiteVendue: number }[];
  commandesParStatut: { statut: string; count: number }[];
}

export interface Review {
  id: number;
  customer?: { prenom: string; nom: string };
  note: number;
  commentaire: string;
  dateCreation: string;
  approuve: boolean;
}

export interface ReviewRequest {
  productId: number;
  note: number;
  commentaire: string;
}

export interface Coupon {
  id: number;
  code: string;
  type: 'PERCENT' | 'FIXED';
  valeur: number;
  dateExpiration?: string;
  actif: boolean;
}