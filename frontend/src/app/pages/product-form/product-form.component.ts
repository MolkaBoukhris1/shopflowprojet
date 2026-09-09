// src/app/pages/product-form/product-form.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/product';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {
  form: FormGroup;
  categories: Category[] = [];
  loading = false;
  isEdit  = false;
  editId: number | null = null;

  constructor(
    private fb:          FormBuilder,
    private productSvc:  ProductService,
    private categorySvc: CategoryService,
    private route:       ActivatedRoute,
    private router:      Router,
    private snack:       MatSnackBar
  ) {
    this.form = this.fb.group({
      nom:         ['', [Validators.required, Validators.minLength(2)]],
      description: ['',  Validators.required],
      prix:        [0,  [Validators.required, Validators.min(0)]],
      prixPromo:   [null],
      stock:       [0,  [Validators.required, Validators.min(0)]],
      // ShopFlow utilise categoryIds (tableau)
      categoryIds: [[], Validators.required],
      images:      ['']
    });
  }

  ngOnInit(): void {
    this.categorySvc.getAll().subscribe(data => this.categories = data);

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.editId = +id;
      this.productSvc.getById(this.editId).subscribe(p => {
        this.form.patchValue({
          nom:         p.nom,
          description: p.description,
          prix:        p.prix,
          prixPromo:   p.prixPromo,
          stock:       p.stock,
          categoryIds: p.categories?.map(c => c.id) || [],
          images:      p.images || ''
        });
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;

    const request$ = this.isEdit && this.editId
      ? this.productSvc.update(this.editId, this.form.value)
      : this.productSvc.create(this.form.value);

    request$.subscribe({
      next: () => {
        this.snack.open(
          this.isEdit ? 'Produit modifié !' : 'Produit créé !',
          'OK', { duration: 3000 }
        );
        this.router.navigate(['/produits']);
      },
      error: err => {
        this.snack.open(err.message, 'Fermer', { duration: 4000 });
        this.loading = false;
      },
      complete: () => { this.loading = false; }
    });
  }

  cancel(): void { this.router.navigate(['/produits']); }
}