// src/app/pages/register/register.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';

function passwordMatch(g: AbstractControl): ValidationErrors | null {
  return g.get('password')?.value === g.get('confirmPassword')?.value
    ? null : { passwordMismatch: true };
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  form: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      prenom:              ['', [Validators.required, Validators.minLength(2)]],
      nom:                 ['', [Validators.required, Validators.minLength(2)]],
      email:               ['', [Validators.required, Validators.email]],
      password:            ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword:     ['', Validators.required],
      role:                ['CUSTOMER', Validators.required],
      // Champs vendeur (optionnels, requis si SELLER)
      nomBoutique:         [''],
      descriptionBoutique: ['']
    }, { validators: passwordMatch });

    // Ajouter validation dynamique si SELLER
    this.form.get('role')?.valueChanges.subscribe(role => {
      const nomBoutique = this.form.get('nomBoutique');
      if (role === 'SELLER') {
        nomBoutique?.setValidators([Validators.required, Validators.minLength(3)]);
      } else {
        nomBoutique?.clearValidators();
      }
      nomBoutique?.updateValueAndValidity();
    });
  }

  get isSeller(): boolean {
    return this.form.get('role')?.value === 'SELLER';
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;

    const { confirmPassword, ...data } = this.form.value;

    this.auth.register(data).subscribe({
      next: res => {
        this.snack.open('Compte créé avec succès !', 'OK', { duration: 3000 });
        this.auth.redirectAfterLogin(res.user.role);
      },
      error: err => {
        this.snack.open(err.message || 'Erreur lors de l\'inscription', 'Fermer', { duration: 4000 });
        this.loading = false;
      },
      complete: () => { this.loading = false; }
    });
  }
}