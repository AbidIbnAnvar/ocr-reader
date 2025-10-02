import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { ReceiptService } from '../../../core/services/receipt.service';
import { Item, ReceiptResponse } from '../../../core/models/receipt.model';
import { ToastrService } from 'ngx-toastr';
import { HlmInputDirective } from '@spartan-ng/helm/input';
import { NgIcon } from '@ng-icons/core';
import { HlmDatePickerModule } from '@spartan-ng/helm/date-picker';
import {
  HlmSelectLabelDirective,
  HlmSelectModule,
} from '@spartan-ng/helm/select';
import { BrnSelectModule } from '@spartan-ng/brain/select';
import { HlmButtonDirective, HlmButtonModule } from '@spartan-ng/helm/button';
import { nonEmptyFormArray } from '../../../core/validators/receipitems.validators';
import {
  HlmDialogComponent,
  HlmDialogContentComponent,
  HlmDialogDescriptionDirective,
  HlmDialogFooterComponent,
  HlmDialogHeaderComponent,
  HlmDialogModule,
  HlmDialogTitleDirective,
} from '@spartan-ng/helm/dialog';
import {
  BrnDialogContentDirective,
  BrnDialogModule,
  BrnDialogTriggerDirective,
  injectBrnDialogContext,
} from '@spartan-ng/brain/dialog';

@Component({
  selector: 'app-receipt-edit',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatSelectModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    NgIcon,
    HlmInputDirective,
    HlmDatePickerModule,
    HlmSelectModule,
    BrnSelectModule,
    HlmButtonModule,
  ],
  templateUrl: './receipt-edit.component.html',
  styleUrl: './receipt-edit.component.css',
})
export class ReceiptEditComponent {
  maxDate = new Date();

  context = Inject(injectBrnDialogContext);

  receiptForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private receiptService: ReceiptService,
    public dialogRef: MatDialogRef<ReceiptEditComponent>,
    private toastr: ToastrService,
    @Inject(MAT_DIALOG_DATA) public data: ReceiptResponse,
  ) {
    this.receiptForm = this.fb.group({
      items: this.fb.array([], nonEmptyFormArray),
      subtotal: [data.subtotal || '', [Validators.min(0)]],
      tax: [data.tax, [Validators.min(0)]],
      total: [data.total, [Validators.required, Validators.min(0)]],
      currencyCode: [data.currencyCode, Validators.required],
      date: [new Date(data.date), Validators.required],
    });

    if (data.items) {
      data.items.forEach((item) => this.addItem(item));
    }
  }

  get items(): FormArray {
    return this.receiptForm.get('items') as FormArray;
  }

  addItem(item?: Item) {
    this.items.push(
      this.fb.group({
        name: [item?.name, Validators.required],
        price: [item?.price, [Validators.required, Validators.min(0)]],
        quantity: [
          item?.quantity || 1,
          [Validators.required, Validators.min(1)],
        ],
      }),
    );
  }

  removeItem(index: number) {
    this.items.removeAt(index);
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSave() {
    if (this.receiptForm.valid) {
      const receiptData: ReceiptResponse = {
        ...this.data,
        ...this.receiptForm.value,
        date: new Date(this.receiptForm.value.date).toISOString(),
      };

      const operation$ = this.data.id
        ? this.receiptService.updateReceipt(this.data.id, receiptData)
        : this.receiptService.saveReceipt(receiptData);

      operation$.subscribe({
        next: () => this.dialogRef.close(true),
        error: (err) => {
          this.toastr.error('Failed to save receipt');
          console.log(err);
        },
      });
    }
  }
}
