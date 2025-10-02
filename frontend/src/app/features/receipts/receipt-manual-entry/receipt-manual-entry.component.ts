import { Component, EventEmitter, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ReceiptEditComponent } from '../receipt-edit/receipt-edit.component';

@Component({
  selector: 'app-receipt-manual-entry',
  imports: [],
  templateUrl: './receipt-manual-entry.component.html',
  styleUrl: './receipt-manual-entry.component.css',
})
export class ReceiptManualEntryComponent {
  @Output() receiptAdded = new EventEmitter<any>();

  constructor(private dialog: MatDialog) {}

  openDialog() {
    const dialogRef = this.dialog.open(ReceiptEditComponent, {
      width: '600px',
      data: {
        items: [],
        subtotal: 0,
        tax: 0,
        total: 0,
        currencyCode: 'USD',
        date: new Date(),
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.receiptAdded.emit(result);
      }
    });
  }
}
