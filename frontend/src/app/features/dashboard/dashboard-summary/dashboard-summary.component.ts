import { CommonModule, CurrencyPipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AnalyticsService } from '../../../core/services/analytics.service';
import { ReceiptService } from '../../../core/services/receipt.service';
import { SkeletonLoaderComponent } from '../../../shared/components/skeleton-loader/skeleton-loader.component';

@Component({
  selector: 'app-dashboard-summary',
  imports: [CommonModule, CurrencyPipe, SkeletonLoaderComponent],
  templateUrl: './dashboard-summary.component.html',
  styleUrl: './dashboard-summary.component.css'
})
export class DashboardSummaryComponent implements OnInit {

  analyticsData: any;
  isLoading = true;
  userCurrency = 'USD';

  constructor(
    private analyticsService: AnalyticsService,
    private receiptService: ReceiptService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.analyticsService.getAnalytics().subscribe({
      next: data => {
        this.analyticsData = data;
        this.isLoading = false;
        this.cdr.detectChanges();
        console.log(this.analyticsData);
        console.log(this.isLoading);
      },
      error: () => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });

    this.receiptService.receipts$.subscribe(receipts => {
      if (receipts.length > 0) {
        this.userCurrency = receipts[0].currencyCode;
      }
    })

  }

}
