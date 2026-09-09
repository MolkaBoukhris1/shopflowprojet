// src/app/models/category.ts
// Adapté ShopFlow — utilise "nom" comme le backend

export interface Category {
  id: number;
  nom: string;
  description?: string;
  parent?: Category;
  sousCategories?: Category[];
}