import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';
import { AnalyticsData } from '../models/analytics.model';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {

  constructor(private http: HttpClient) { }

  getAnalytics(filters?: any): Observable<AnalyticsData> {
    return this.http.get<AnalyticsData>(`${environment.apiUrl}/analytics/summary`, { params: filters })
  }

  exportToCsv(filters?: any): Observable<Blob> {
    return this.http.get(`${environment.apiUrl}/analytics/export`, {
      params: filters,
      responseType: 'blob'
    })
  }

}
