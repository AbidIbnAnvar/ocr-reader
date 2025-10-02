import { Component, OnInit } from '@angular/core';
import { ReceiptService } from '../../../core/services/receipt.service';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { Observable } from 'rxjs';
import { ReceiptResponse } from '../../../core/models/receipt.model';

@Component({
  selector: 'app-recent-receipts',
  imports: [
    CommonModule,
    RouterLink,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatCardModule,
    CurrencyPipe,
    DatePipe
  ],
  templateUrl: './recent-receipts.component.html',
  styleUrl: './recent-receipts.component.css'
})
export class RecentReceiptsComponent {
  receipts$!: Observable<ReceiptResponse[]>;

  constructor(private receiptService: ReceiptService) {
    this.receiptService.loadReceipts();
    this.receipts$ = this.receiptService.receipts$;
  }

}
