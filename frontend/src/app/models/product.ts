// src/app/models/product.ts

export interface Category {
  id: number;
  nom: string;
  description?: string;
  parent?: Category;
  sousCategories?: Category[];
}

export interface ProductVariant {
  id: number;
  attribut: string;
  valeur: string;
  stockSupplementaire: number;
  prixDelta: number;
}

export interface Product {
  id: number;
  nom: string;
  description: string;
  prix: number;
  prixPromo?: number;
  remisePourcentage?: number;
  stock: number;
  images?: string;
  actif: boolean;
  dateCreation?: string;
  seller?: { id: number; nomBoutique: string };
  categories?: Category[];
  variantes?: ProductVariant[];
  noteMoyenne?: number;
  nombreAvis?: number;
}

export interface ProductRequest {
  nom: string;
  description: string;
  prix: number;
  prixPromo?: number;
  stock: number;
  images?: string;
  categoryIds: number[];
}

// Pour la pagination Spring Boot
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}