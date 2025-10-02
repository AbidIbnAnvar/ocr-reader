import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr'

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  signupForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(3), Validators.maxLength(20), Validators.pattern(/^[a-zA-Z0-9_]+$/)] }),
    email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(8)] })
  });

  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
  ) { }

  onSubmit() {
    if (this.signupForm.valid) {
      const username = this.signupForm.get('username')!.value;
      const email = this.signupForm.get('email')!.value;
      const password = this.signupForm.get('password')!.value;
      this.authService.signup({ username, email, password }).subscribe({
        next: () => {
          this.router.navigate(['/dashboard'])
        },
        error: (err) => {
          this.toastr.error('Invalid credentials', 'Login Failed');
        }
      })
    }
  }

}
