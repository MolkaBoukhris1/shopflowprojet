// src/app/pages/produits/produits.component.ts
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Product, Category } from '../../models/product';

@Component({
  selector: 'app-produits',
  templateUrl: './produits.component.html',
  styleUrls: ['./produits.component.css']
})
export class ProduitsComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  loading = false;

  // Pagination
  currentPage  = 0;
  pageSize     = 12;
  totalPages   = 0;
  totalElements = 0;

  searchCtrl = new FormControl('');
  catCtrl    = new FormControl(null);

  constructor(
    private productSvc:  ProductService,
    private categorySvc: CategoryService,
    private cartSvc:     CartService,
    public  auth:        AuthService,
    private router:      Router,
    private snack:       MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadProducts();

    this.searchCtrl.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged())
      .subscribe(() => { this.currentPage = 0; this.loadProducts(); });

    this.catCtrl.valueChanges
      .subscribe(() => { this.currentPage = 0; this.loadProducts(); });
  }

  loadProducts(): void {
    this.loading = true;
    this.productSvc.getAll({
      search:  this.searchCtrl.value || undefined,
      page:    this.currentPage,
      size:    this.pageSize
    }).subscribe({
      next: page => {
        this.products      = page.content;
        this.totalPages    = page.totalPages;
        this.totalElements = page.totalElements;
        this.loading       = false;
      },
      error: () => { this.loading = false; }
    });
  }

  loadCategories(): void {
    this.categorySvc.getAll().subscribe(data => this.categories = data);
  }

  prevPage(): void {
    if (this.currentPage > 0) { this.currentPage--; this.loadProducts(); }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) { this.currentPage++; this.loadProducts(); }
  }

  addToCart(e: Event, product: Product): void {
    e.stopPropagation();
    if (!this.auth.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }
    this.cartSvc.addItem({ productId: product.id, quantite: 1 }).subscribe({
      next: () => {
        this.snack.open(`"${product.nom}" ajouté au panier`, 'Voir panier', { duration: 3000 })
          .onAction().subscribe(() => this.router.navigate(['/cart']));
      },
      error: err => this.snack.open(err.message, 'Fermer', { duration: 4000 })
    });
  }

  editProduct(e: Event, product: Product): void {
    e.stopPropagation();
    this.router.navigate(['/product-form', product.id]);
  }

  deleteProduct(e: Event, product: Product): void {
    e.stopPropagation();
    if (!confirm(`Supprimer "${product.nom}" ?`)) return;
    this.productSvc.delete(product.id).subscribe({
      next: () => {
        this.snack.open('Produit supprimé', 'OK', { duration: 3000 });
        this.loadProducts();
      },
      error: err => this.snack.open(err.message, 'Fermer', { duration: 4000 })
    });
  }
}