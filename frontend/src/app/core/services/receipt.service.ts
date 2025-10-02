import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { ReceiptResponse } from '../models/receipt.model';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class ReceiptService {

  private receiptsSubject = new BehaviorSubject<ReceiptResponse[]>([]);
  receipts$ = this.receiptsSubject.asObservable()

  constructor(
    private http: HttpClient,
    private toastr: ToastrService
  ) { }

  loadReceipts() {
    this.http.get<ReceiptResponse[]>(`${environment.apiUrl}/receipts`).subscribe({
      next: receipts => this.receiptsSubject.next(receipts),
      error: () => this.toastr.error("Failed to load receipts")
    })
  }

  uploadReceipt(file: File): Observable<{ imageUrl: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ imageUrl: string }>(`${environment.apiUrl}/upload`, formData);
  }

  extractText(imageUrl: string) {
    return this.http.post<ReceiptResponse>(`${environment.apiUrl}/extract-text`, { imageUrl: imageUrl });
  }

  saveReceipt(receipt: ReceiptResponse): Observable<ReceiptResponse> {
    return this.http.post<ReceiptResponse>(`${environment.apiUrl}/receipts`, receipt).pipe(
      tap(() => {
        this.toastr.success('Receipt saved successfully!');
        this.loadReceipts();
      })
    )
  }

  updateReceipt(id: string, receipt: ReceiptResponse): Observable<ReceiptResponse> {
    return this.http.put<ReceiptResponse>(`${environment.apiUrl}/receipts/${id}`, receipt).pipe(
      tap(() => {
        this.toastr.success('Receipt updated!');
        this.loadReceipts();
      })
    )
  }

  deleteReceipt(id: string): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/receipts/${id}`).pipe(
      tap(() => {
        this.toastr.success('Receipt deleted!');
        this.loadReceipts();
      })
    )
  }

}
