import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReceiptUploadComponent } from './receipt-upload/receipt-upload.component';
import { ReceiptListComponent } from './receipt-list/receipt-list.component';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { ReceiptManualEntryComponent } from './receipt-manual-entry/receipt-manual-entry.component';

@Component({
  selector: 'app-receipts',
  imports: [
    CommonModule,
    RouterModule,
    ReceiptListComponent,
    ReceiptManualEntryComponent,
    ReceiptUploadComponent,
    MatIconModule,
  ],
  templateUrl: './receipts.component.html',
  styleUrl: './receipts.component.css',
})
export class ReceiptsComponent {
  activeTab: 'list' | 'manual' = 'list';

  showList() {
    this.activeTab = 'list';
  }
}
