import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DashboardSummaryComponent } from './dashboard-summary/dashboard-summary.component';
import { RecentReceiptsComponent } from './recent-receipts/recent-receipts.component';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { HlmButtonDirective } from '@spartan-ng/helm/button'
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [
    CommonModule,
    RouterModule,
    DashboardSummaryComponent,
    RecentReceiptsComponent,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    HlmButtonDirective
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

  monthlyTotal = 485.75;
  lastMonthTotal = 620.30;
  weeklyAverage = 125.20;

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }

}
