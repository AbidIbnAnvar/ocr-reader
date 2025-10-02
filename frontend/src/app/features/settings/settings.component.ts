import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs'
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { ThemeToggleComponent } from '../../shared/components/theme-toggle/theme-toggle.component/theme-toggle.component';
import { MatCheckboxModule } from '@angular/material/checkbox'
import { AuthService } from '../../core/services/auth.service';
import { SettingsService } from '../../core/services/settings.service';
import { passwordMatchValidator } from '../../core/validators/password.validators';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-settings',
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatTabsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    ThemeToggleComponent,

  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent implements OnInit {

  currencies = [
    { code: 'USD', name: 'US Dollar' },
    { code: 'EUR', name: 'Euro' },
    { code: 'GBP', name: 'British Pound' },
    { code: 'JPY', name: 'Japanese Yen' },
    { code: 'CAD', name: 'Canadian Dollar' },
    { code: 'AUD', name: 'Australian Dollar' },
    { code: 'CHF', name: 'Swiss Franc' },
    { code: 'CNY', name: 'Chinese Yuan' }
  ];

  profileForm!: FormGroup;
  passwordForm!: FormGroup;
  displayForm!: FormGroup;
  deleteAccountForm!: FormGroup;

  user: any;
  isLoading = false;
  isDeleting = false;
  showPassword = false;
  showNewPassword = false;
  showConfirmPassword = false;


  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private settingsService: SettingsService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.user = this.authService.currentUserValue;

    // Initialize forms
    this.profileForm = this.fb.group({
      username: [this.user?.username || '', [Validators.required, Validators.minLength(3)]],
      email: [this.user?.email || '', [Validators.required, Validators.email]]
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', [Validators.required, Validators.minLength(6)]],
      newPassword: ['', [Validators.required, Validators.minLength(8), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)]],
      confirmPassword: ['', Validators.required]
    }, { validators: passwordMatchValidator });

    this.displayForm = this.fb.group({
      currency: [this.settingsService.getCurrency() || 'USD'],
      darkMode: [this.settingsService.getDarkMode()]
    });

    this.deleteAccountForm = this.fb.group({
      confirm: [false, [Validators.requiredTrue]],
      password: ['', Validators.required]
    });

    // Subscribe to display settings changes
    this.displayForm.get('darkMode')?.valueChanges.subscribe(value => {
      this.settingsService.setDarkMode(value);
    });

    this.displayForm.get('currency')?.valueChanges.subscribe(value => {
      this.settingsService.setCurrency(value);
    });

  }

  updateProfile() {
    if (this.profileForm.invalid) return;

    this.isLoading = true;
    const { username, email } = this.profileForm.value;

    this.settingsService.updatePreferences({ username, email }).subscribe({
      next: () => {
        this.snackBar.open('Profile updated successfully', 'Close', { duration: 3000 });
        this.authService.currentUserSubject.next({
          ...this.user,
          username,
          email
        });
        this.isLoading = false;
      },
      error: () => {
        this.snackBar.open('Failed to update profile', 'Close', { duration: 3000 });
        this.isLoading = false;
      }
    });

  }

  changePassword() {
    if (this.passwordForm.valid) {
      this.isLoading = true;
      const { currentPassword, newPassword } = this.passwordForm.value;

      this.settingsService.changePassword(currentPassword, newPassword).subscribe({
        next: () => {
          this.snackBar.open('Password changed successfully', 'Close', { duration: 3000 });
          this.passwordForm.reset();
          this.isLoading = false;
        },
        error: () => {
          this.snackBar.open('Failed to change password. Check your current password.', 'Close', { duration: 3000 });
          this.isLoading = false;
        }
      });
    }
  }

  deleteAccount() {
    if (this.deleteAccountForm.valid) {
      this.isDeleting = true;
      const password = this.deleteAccountForm.get('password')?.value;

      this.settingsService.deleteAccount(password).subscribe({
        next: () => {
          this.snackBar.open('Account deleted successfully', 'Close', { duration: 3000 });
          this.authService.logout();
          this.isDeleting = false;
        },
        error: () => {
          this.snackBar.open('Failed to delete account. Check your password.', 'Close', { duration: 3000 });
          this.isDeleting = false;
        }
      });
    }
  }

  togglePasswordVisibility(field: string) {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
    } else if (field === 'newPassword') {
      this.showNewPassword = !this.showNewPassword;
    } else if (field === 'confirmPassword') {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
  }

  getPasswordStrength(password: string): string {
    if (!password) return '';

    const hasLetters = /[a-zA-Z]/.test(password);
    const hasNumbers = /\d/.test(password);
    const hasSpecialChars = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    const isLong = password.length >= 10;

    if (isLong && hasLetters && hasNumbers && hasSpecialChars) return 'strong';
    if (password.length >= 8 && hasLetters && hasNumbers) return 'medium';
    return 'weak';
  }

}
