// src/app/pages/login/login.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  form: FormGroup;
  loading = false;
  hide = true;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      email:    ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
    if (this.auth.isLoggedIn) this.auth.redirectAfterLogin(this.auth.currentUser!.role);
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;

    this.auth.login(this.form.value).subscribe({
      next: res => {
        this.snack.open('Connexion réussie !', 'OK', { duration: 3000 });
        this.auth.redirectAfterLogin(res.user.role);
      },
      error: err => {
        this.snack.open(err.message || 'Identifiants incorrects', 'Fermer', { duration: 4000 });
        this.loading = false;
      },
      complete: () => { this.loading = false; }
    });
  }
}