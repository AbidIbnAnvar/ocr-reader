import { MatDatepickerModule } from '@angular/material/datepicker';
import { Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../core/services/analytics.service';
import { AnalyticsData } from '../../core/models/analytics.model';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { saveAs } from 'file-saver';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BaseChartDirective } from 'ng2-charts';
import { RouterModule } from '@angular/router';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-analytics',
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatCardModule,
    MatInputModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    BaseChartDirective,
  ],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css',
})
export class AnalyticsComponent implements OnInit {
  analyticsData: AnalyticsData | null = null;
  isLoading = false;

  filterForm = new FormGroup({
    startDate: new FormControl<Date | null>(null),
    endDate: new FormControl<Date | null>(null),
    category: new FormControl<string>(''),
    minAmount: new FormControl<number | null>(null),
    maxAmount: new FormControl<number | null>(null),
  });

  categoryChartData: any;
  monthlyChartData: any;
  dailyChartData: any;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.loadAnalytics();
  }

  loadAnalytics() {
    this.isLoading = true;
    const filters = this.filterForm.value;

    let startDateISO, endDateISO;

    if (filters.startDate) {
      startDateISO = filters.startDate.toISOString();
    }
    if (filters.endDate) {
      endDateISO = filters.endDate.toISOString();
    }

    this.analyticsService.getAnalytics(filters).subscribe({
      next: (data) => {
        this.analyticsData = data;
        this.prepareCharts();
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  prepareCharts() {
    if (!this.analyticsData) return;

    this.dailyChartData = {
      labels: this.analyticsData.byDate.map((item) => item.date),
      datasets: [
        {
          label: 'Spending',
          data: this.analyticsData.byDate.map((item) => item.amount),
          backgroundColor: '#4BC0C0',
          borderColor: '#4BC0C0',
          fill: false,
        },
      ],
    };
  }

  exportCsv() {
    const filters = this.filterForm.value;
    this.analyticsService.exportToCsv(filters).subscribe((blob) => {
      saveAs(blob, `expenses_${new Date().toISOString().slice(0, 10)}.csv`);
    });
  }

  resetFilters() {
    this.filterForm.reset();
    this.loadAnalytics();
  }
}
