import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, Output } from '@angular/core';
import { ReceiptService } from '../../../core/services/receipt.service';
import { MatDialog } from "@angular/material/dialog"
import { finalize } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { ReceiptResponse } from '../../../core/models/receipt.model';
import { ReceiptEditComponent } from '../receipt-edit/receipt-edit.component';

@Component({
  selector: 'app-receipt-upload',
  imports: [CommonModule],
  templateUrl: './receipt-upload.component.html',
  styleUrl: './receipt-upload.component.css'
})
export class ReceiptUploadComponent {
  @Output() receiptAdded = new EventEmitter<void>();
  isProcessing = false;

  constructor(
    private receiptService: ReceiptService,
    private dialog: MatDialog,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) { }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.processReceipt(input.files[0]);
    }
  }

  processReceipt(file: File) {
    this.isProcessing = true;
    console.log("Processing");
    this.receiptService.uploadReceipt(file).subscribe({
      next: (uploadRes) => {
        console.log("Inside uploadReceipt():", uploadRes);
        this.receiptService.extractText(uploadRes.imageUrl).subscribe({
          next: (extracted) => {
            this.openEditDialog({
              ...extracted,
              imageUrl: uploadRes.imageUrl,
              date: new Date().toISOString()
            });
          },
          error: (err) => {
            this.toastr.error('Failed to extract text from receipt');
            console.log(err);
          },
          complete: () => {
            this.isProcessing = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: (err) => {
        this.toastr.error('Failed to upload receipt');
        console.log(err);
        this.isProcessing = false;
        this.cdr.detectChanges();
      }
    })
  }

  openEditDialog(receipt: ReceiptResponse) {
    const dialogRef = this.dialog.open(ReceiptEditComponent, {
      width: '600px',
      data: receipt
    })

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.receiptAdded.emit();
      }
    })
  }

}
