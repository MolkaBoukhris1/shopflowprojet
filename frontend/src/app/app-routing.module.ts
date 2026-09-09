// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';

import { LoginComponent }       from './pages/login/login.component';
import { RegisterComponent }    from './pages/register/register.component';
import { ProduitsComponent }    from './pages/produits/produits.component';
import { ProductFormComponent } from './pages/product-form/product-form.component';
import { CategoriesComponent }  from './pages/categories/categories.component';
import { DashboardComponent }   from './pages/dashboard/dashboard.component';
import { CartComponent }        from './pages/cart/cart.component';
import { CheckoutComponent }    from './pages/checkout/checkout.component';

const routes: Routes = [
  { path: '', redirectTo: 'produits', pathMatch: 'full' },

  // Public
  { path: 'login',    component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'produits', component: ProduitsComponent },

  // CUSTOMER seulement
  {
    path: 'cart',
    component: CartComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'checkout',
    component: CheckoutComponent,
    canActivate: [AuthGuard]
  },

  // ADMIN et SELLER
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard, RoleGuard]
  },
  {
    path: 'product-form',
    component: ProductFormComponent,
    canActivate: [AuthGuard, RoleGuard]
  },
  {
    path: 'product-form/:id',
    component: ProductFormComponent,
    canActivate: [AuthGuard, RoleGuard]
  },

  // ADMIN seulement
  {
    path: 'categories',
    component: CategoriesComponent,
    canActivate: [AuthGuard, RoleGuard]
  },

  { path: '**', redirectTo: 'produits' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}