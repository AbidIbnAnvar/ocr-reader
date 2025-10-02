import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatMenuModule } from '@angular/material/menu';
import { ReceiptService } from '../../../core/services/receipt.service';
import { Observable } from 'rxjs';
import { ReceiptResponse } from '../../../core/models/receipt.model';
import { MatDialog } from '@angular/material/dialog';
import { ReceiptEditComponent } from '../receipt-edit/receipt-edit.component';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-receipt-list',
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    CurrencyPipe,
    DatePipe,
  ],
  templateUrl: './receipt-list.component.html',
  styleUrl: './receipt-list.component.css',
})
export class ReceiptListComponent implements OnInit {
  displayedColumns: string[] = ['date', 'items', 'total', 'actions'];
  dataSource = new MatTableDataSource<ReceiptResponse>();

  constructor(
    private receiptService: ReceiptService,
    private dialog: MatDialog,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.receiptService.loadReceipts();
    this.receiptService.receipts$.subscribe((receipts) => {
      this.dataSource.data = receipts ?? [];
      this.cdr.markForCheck();
    });
  }

  editReceipt(receipt: ReceiptResponse) {
    this.dialog.open(ReceiptEditComponent, {
      width: '600px',
      data: receipt,
    });
  }

  deleteReceipt(id: string) {
    if (confirm('Are you sure you want to delete this receipt?')) {
      this.receiptService.deleteReceipt(id).subscribe();
    }
  }
}
