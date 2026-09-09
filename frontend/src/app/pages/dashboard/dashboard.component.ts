// src/app/pages/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { ChartConfiguration, ChartData } from 'chart.js';
import { OrderService } from '../../services/order.service';
import { AuthService } from '../../services/auth.service';
import { DashboardStats } from '../../models/order';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  loading = false;
  orderCols = ['id', 'numero', 'statut', 'total', 'date'];

  barData: ChartData<'bar'>           = { labels: [], datasets: [] };
  doughnutData: ChartData<'doughnut'> = { labels: [], datasets: [] };

  barOpts: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true } }
  };

  doughnutOpts: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { position: 'bottom' } }
  };

  constructor(
    private orderSvc: OrderService,
    public  auth:     AuthService
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.orderSvc.getStats().subscribe({
      next: data => {
        this.stats = data;
        this.buildCharts(data);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  buildCharts(s: DashboardStats): void {
    // Bar chart - top produits
    this.barData = {
      labels: s.topProduits.map(x => x.nom),
      datasets: [{
        data: s.topProduits.map(x => x.quantiteVendue),
        backgroundColor: ['#667eea','#764ba2','#f093fb','#4facfe','#43e97b','#fa709a'],
        borderRadius: 8
      }]
    };

    // Doughnut - statuts commandes
    this.doughnutData = {
      labels: s.commandesParStatut.map(x => x.statut),
      datasets: [{
        data: s.commandesParStatut.map(x => x.count),
        backgroundColor: ['#ffa726','#66bb6a','#42a5f5','#26c6da','#ab47bc','#ef5350']
      }]
    };
  }

  getStatusColor(statut: string): string {
    const map: Record<string, string> = {
      PENDING:    'warn',
      PAID:       'primary',
      PROCESSING: 'accent',
      SHIPPED:    'primary',
      DELIVERED:  'primary',
      CANCELLED:  'warn'
    };
    return map[statut] || 'default';
  }
}