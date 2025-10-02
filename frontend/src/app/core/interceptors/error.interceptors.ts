import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { catchError, throwError } from 'rxjs';

export const errorInterceptors: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const toastr = inject(ToastrService);

  return next(req).pipe(
    catchError(error => {
      if (error.status === 401) {
        toastr.error('Your session has expired. Please login again.');
        router.navigate(['/auth/login']);
      }
      else if (error.status === 400) {
        // Bad request - show validation errors
        const errors = error.error?.errors || [];
        if (errors.length > 0) {
          errors.forEach((err: any) => toastr.error(err.message));
        } else {
          toastr.error(error.error?.message || 'Invalid request');
        }
      }
      else if (error.status === 403) {
        // Forbidden
        toastr.error('You do not have permission to perform this action');
      }
      else if (error.status >= 500) {
        // Server errors
        toastr.error('Server error. Please try again later.');
      }
      else {
        // Generic error handling
        toastr.error('An unexpected error occurred');
      }

      return throwError(() => error);
    })
  )
};
