import { MatDatepickerModule } from '@angular/material/datepicker';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AnalyticsService } from '../../core/services/analytics.service';
import { AnalyticsData, AnalyticsDataPoint } from '../../core/models/analytics.model';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonToggleModule } from '@angular/material/button-toggle'
import { Subject, takeUntil } from 'rxjs';
import { GranularitySelectorComponent } from './granularity-selecter.component';
import { CategoryScale, Chart, LinearScale, LineController, LineElement, PointElement, registerables, Title } from 'chart.js';
Chart.register(LineController, LineElement, PointElement, LinearScale, Title, CategoryScale);
Chart.register(...registerables);

@Component({
  selector: 'app-analytics',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatIconModule,
    MatButtonToggleModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatCardModule,
    MatInputModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    GranularitySelectorComponent
  ],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css',
})
export class AnalyticsComponent implements OnInit, OnDestroy {
  chartInstance: Chart | null = null;
  analyticsData: AnalyticsData | null = null;
  isLoading = false;

  selectedGranularity: 'day' | 'month' | 'year' = 'day';
  offset = 0;

  timeSeriesChartData: any;

  filterForm = new FormGroup({
    granularity: new FormControl<'day' | 'month' | 'year'>('day'),
    startDate: new FormControl<Date | null>(null),
    endDate: new FormControl<Date | null>(null),
    category: new FormControl<string>(''),
    minAmount: new FormControl<number | null>(null),
    maxAmount: new FormControl<number | null>(null),
  });

  private destroy$ = new Subject<void>();

  constructor(private analyticsService: AnalyticsService) { }

  ngOnInit(): void {
    this.filterForm.get('granularity')!.valueChanges
      .pipe(
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        this.offset = 0;
        this.loadTimeSeries();
      });
    this.loadTimeSeries();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onGranularityChange(value: 'day' | 'month' | 'year') {
    this.offset = 0;
    this.filterForm.get('granularity')!.setValue(value, { emitEvent: false });
    this.loadTimeSeries();
  }

  loadTimeSeries() {
    this.isLoading = true;
    const granularity = this.filterForm.get('granularity')!.value!;
    const offset = this.offset;

    this.analyticsService.getTimeSeriesAnalytics(granularity, offset)
      .subscribe({
        next: (data) => {
          this.prepareTimeSeriesChart(data);
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        }
      });
  }


  changeOffset(value: number) {
    this.offset += value;
    this.loadTimeSeries();
  }

  prepareTimeSeriesChart(data: AnalyticsDataPoint[]) {
    if (this.chartInstance) {
      this.chartInstance.destroy(); // remove old chart
    }

    this.chartInstance = new Chart('myChart', {
      type: 'line',
      data: {
        labels: data.map(d => d.label),
        datasets: [{
          label: 'Spending',
          data: data.map(d => d.amount),
          backgroundColor: '#4f39f6',
          borderColor: '#4f39f6',
          fill: false,
          tension: 0.2 // optional: smooth line
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          tooltip: {
            enabled: true,        // enable tooltip
            mode: 'index',        // show all dataset values at hovered x-axis point
            intersect: false,     // tooltip shows even if cursor is between points
            callbacks: {
              label: function (context) {
                return `$${context.parsed.y.toFixed(2)}`; // format the tooltip
              }
            }
          }
        },
        interaction: {
          mode: 'nearest',        // make hover interaction more responsive
          intersect: false
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }
}
