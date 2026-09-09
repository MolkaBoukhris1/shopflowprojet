// src/app/pages/categories/categories.component.ts
import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { CategoryService } from '../../services/category.service';

// IMPORTANT : importer Category depuis product.ts (qui a "nom")
import { Category } from '../../models/product';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit, AfterViewInit {

  displayedColumns = ['id', 'nom', 'description', 'actions'];
  dataSource = new MatTableDataSource<Category>();
  loading  = false;

  form: FormGroup;
  isEdit   = false;
  editId: number | null = null;
  showForm = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort)      sort!: MatSort;

  constructor(
    private categorySvc: CategoryService,
    private fb:          FormBuilder,
    private snack:       MatSnackBar
  ) {
    this.form = this.fb.group({
      nom:         ['', [Validators.required, Validators.minLength(2)]],
      description: [''],
      parentId:    [null]
    });
  }

  ngOnInit(): void { this.loadCategories(); }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort      = this.sort;
  }

  loadCategories(): void {
    this.loading = true;
    this.categorySvc.getAll().subscribe({
      next: data => { this.dataSource.data = data; this.loading = false; },
      error: ()   => { this.loading = false; }
    });
  }

  applyFilter(event: Event): void {
    this.dataSource.filter = (event.target as HTMLInputElement).value.trim().toLowerCase();
  }

  openAdd(): void {
    this.isEdit   = false;
    this.editId   = null;
    this.showForm = true;
    this.form.reset({ nom: '', description: '', parentId: null });
  }

  openEdit(cat: Category): void {
    this.isEdit   = true;
    this.editId   = cat.id;
    this.showForm = true;
    this.form.patchValue({
      nom:         cat.nom,
      description: cat.description || '',
      parentId:    cat.parent?.id || null
    });
  }

  cancelForm(): void {
    this.showForm = false;
    this.form.reset();
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const request$ = this.isEdit && this.editId
      ? this.categorySvc.update(this.editId, this.form.value)
      : this.categorySvc.create(this.form.value);

    request$.subscribe({
      next: () => {
        this.snack.open(
          this.isEdit ? 'Catégorie modifiée !' : 'Catégorie créée !',
          'OK', { duration: 3000 }
        );
        this.showForm = false;
        this.form.reset();
        this.loadCategories();
      },
      error: (err: any) => this.snack.open(err.message, 'Fermer', { duration: 4000 })
    });
  }

  delete(cat: Category): void {
    if (!confirm(`Supprimer "${cat.nom}" ?`)) return;
    this.categorySvc.delete(cat.id).subscribe({
      next: () => {
        this.snack.open('Catégorie supprimée', 'OK', { duration: 3000 });
        this.loadCategories();
      },
      error: (err: any) => this.snack.open(err.message, 'Fermer', { duration: 4000 })
    });
  }
}