// import { HttpClient } from '@angular/common/http';
// import { Injectable, signal } from '@angular/core';
// import { UserSettings } from '../models/settings.model';
// import { environment } from '../../environment/environment';
// import { FormGroup } from '@angular/forms';
// import { BehaviorSubject } from 'rxjs';

// @Injectable({
//   providedIn: 'root'
// })
// export class SettingsService {
//   private darkModeSubject = new BehaviorSubject<boolean>(
//     localStorage.getItem('darkMode') === 'true'
//   );
//   darkMode = signal(this.darkModeSubject.value);
//   darkMode$ = this.darkModeSubject.asObservable();

//   constructor(private http: HttpClient) {
//     // Initialize from localStorage
//     this.toggleDarkMode(this.darkModeSubject.value);
//   }

//   toggleDarkMode(enable: boolean) {
//     this.darkMode.set(enable);
//     localStorage.setItem('darkMode', enable.toString());

//     if (enable) {
//       document.documentElement.classList.add('dark');
//     } else {
//       document.documentElement.classList.remove('dark');
//     }
//   }

//   // darkMode = signal<boolean>(this.getInitialTheme());

//   // constructor(
//   //   private http: HttpClient
//   // ) {
//   //   this.applyTheme(this.darkMode());
//   // }

//   // toggleDarkMode(): void {
//   //   const isDark = !this.darkMode();
//   //   this.darkMode.set(isDark);
//   //   this.applyTheme(isDark);
//   //   localStorage.setItem('darkMode', JSON.stringify(isDark));
//   // }

//   // private applyTheme(isDark: boolean): void {
//   //   const root = document.documentElement;
//   //   if (isDark) {
//   //     root.classList.add('dark');
//   //   } else {
//   //     root.classList.remove('dark');
//   //   }
//   // }

//   // private getInitialTheme(): boolean {
//   //   const saved = localStorage.getItem('darkMode');
//   //   return saved ? JSON.parse(saved) : window.matchMedia('(prefers-color-scheme: dark)').matches;
//   // }

//   passwordMatchValidator(form: FormGroup) {
//     const password = form.get('password')?.value;
//     const confirmPassword = form.get('confirmPassword')?.value;
//     if (password && confirmPassword && password !== confirmPassword) {
//       return { passwordMismatch: true };
//     }
//     return null;
//   }

//   getUserSettings() {
//     return this.http.get<UserSettings>(`${environment.apiUrl}/settings`);
//   }

//   updateProfile(profile: { email?: string; password?: string }) {
//     return this.http.put(`${environment.apiUrl}/settings/profile`, profile);
//   }

//   updatePreferences(prefs: { currency: string; darkMode: boolean }) {
//     return this.http.put(`${environment.apiUrl}/settings/preferences`, prefs);
//   }

//   deleteAccount() {
//     return this.http.delete(`${environment.apiUrl}/settings/account`);
//   }

// }

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../environment/environment';

@Injectable({ providedIn: 'root' })
export class SettingsService {
  private darkModeSubject = new BehaviorSubject<boolean>(
    this.getInitialDarkMode(),
  );
  darkMode$ = this.darkModeSubject.asObservable();

  private currencySubject = new BehaviorSubject<string>(
    this.getInitialCurrency(),
  );
  currency$ = this.currencySubject.asObservable();

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {
    const initialDarkMode = this.getInitialDarkMode();
    const current = this.darkModeSubject.value;

    if (initialDarkMode !== current) {
      this.setDarkMode(initialDarkMode);
    }

    this.setCurrency(this.getInitialCurrency());
  }

  private getInitialDarkMode(): boolean {
    const saved = localStorage.getItem('darkMode');
    return (
      saved === 'true' ||
      (saved === null &&
        window.matchMedia('(prefers-color-scheme: dark)').matches)
    );
  }

  private getInitialCurrency(): string {
    return localStorage.getItem('currency') || 'USD';
  }

  setDarkMode(value: boolean) {
    localStorage.setItem('darkMode', value.toString());
    this.darkModeSubject.next(value);

    if (value) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }

  getDarkMode(): boolean {
    return this.darkModeSubject.value;
  }

  toggleDarkMode() {
    this.setDarkMode(!this.getDarkMode());
  }

  setCurrency(currency: string) {
    localStorage.setItem('currency', currency);
    this.currencySubject.next(currency);
  }

  getCurrency(): string {
    return this.currencySubject.value;
  }

  updateProfile(username: string, email: string) {
    return this.http.put(`${environment.apiUrl}/user/profile`, {
      username,
      email,
    });
  }

  updatePreferences(prefs: {
    username?: any;
    email?: any;
    currency?: string;
    darkMode?: boolean;
  }) {
    return this.http.put(`${environment.apiUrl}/settings/preferences`, prefs);
  }

  changePassword(currentPassword: string, newPassword: string) {
    return this.http.put(`${environment.apiUrl}/user/change-password`, {
      currentPassword,
      newPassword,
    });
  }

  deleteAccount(password: string) {
    return this.http.delete(`${environment.apiUrl}/user`, {
      body: { password },
    });
  }
}

