import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../environment/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  currentUserSubject = new BehaviorSubject<any>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    const token = localStorage.getItem('token');
    if (token) this.setUserFromToken(token);
  }

  login(credentials: { email: string; password: string }) {
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, credentials).pipe(
      tap(res => this.setSession(res))
    );
  }

  signup(userData: { username: string; email: string; password: string }) {
    return this.http.post<any>(`${environment.apiUrl}/auth/signup`, userData).pipe(
      tap(res => this.setSession(res))
    );
  }

  logout() {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/']);
  }

  private setSession(authResult: { token: string }) {
    localStorage.setItem('token', authResult.token);
    this.setUserFromToken(authResult.token);
  }

  private setUserFromToken(token: string) {
    const decoded: any = jwtDecode(token);
    this.currentUserSubject.next({
      id: decoded.sub,
      username: decoded.username,
      email: decoded.email
    });
  }

  get currentUserValue() {
    return this.currentUserSubject.value;
  }
}