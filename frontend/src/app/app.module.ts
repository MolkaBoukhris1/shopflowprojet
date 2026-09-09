// src/app/app.module.ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgChartsModule } from 'ng2-charts';

import { AppRoutingModule } from './app-routing.module';
import { AuthInterceptor } from './interceptors/auth.interceptor';

// Angular Material
import { MatToolbarModule }         from '@angular/material/toolbar';
import { MatButtonModule }          from '@angular/material/button';
import { MatIconModule }            from '@angular/material/icon';
import { MatCardModule }            from '@angular/material/card';
import { MatInputModule }           from '@angular/material/input';
import { MatFormFieldModule }       from '@angular/material/form-field';
import { MatSelectModule }          from '@angular/material/select';
import { MatTableModule }           from '@angular/material/table';
import { MatSnackBarModule }        from '@angular/material/snack-bar';
import { MatBadgeModule }           from '@angular/material/badge';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule }           from '@angular/material/chips';
import { MatPaginatorModule }       from '@angular/material/paginator';
import { MatSortModule }            from '@angular/material/sort';
import { MatTooltipModule }         from '@angular/material/tooltip';
import { MatMenuModule }            from '@angular/material/menu';
import { MatDividerModule }         from '@angular/material/divider';
import { MatListModule }            from '@angular/material/list';
import { MatCheckboxModule }        from '@angular/material/checkbox';
import { MatRadioModule }           from '@angular/material/radio';

// Pages
import { AppComponent }          from './app.component';
import { LoginComponent }        from './pages/login/login.component';
import { RegisterComponent }     from './pages/register/register.component';
import { ProduitsComponent }     from './pages/produits/produits.component';
import { ProductFormComponent }  from './pages/product-form/product-form.component';
import { CategoriesComponent }   from './pages/categories/categories.component';
import { DashboardComponent }    from './pages/dashboard/dashboard.component';
import { CartComponent }         from './pages/cart/cart.component';
import { CheckoutComponent }     from './pages/checkout/checkout.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ProduitsComponent,
    ProductFormComponent,
    CategoriesComponent,
    DashboardComponent,
    CartComponent,
    CheckoutComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,          // Pour [(ngModel)] du coupon
    AppRoutingModule,
    NgChartsModule,
    MatToolbarModule, MatButtonModule, MatIconModule, MatCardModule,
    MatInputModule, MatFormFieldModule, MatSelectModule, MatTableModule,
    MatSnackBarModule, MatBadgeModule, MatProgressSpinnerModule,
    MatChipsModule, MatPaginatorModule, MatSortModule, MatTooltipModule,
    MatMenuModule, MatDividerModule, MatListModule,
    MatCheckboxModule, MatRadioModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}