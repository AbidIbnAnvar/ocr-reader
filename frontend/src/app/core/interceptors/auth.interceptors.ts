import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environment/environment';

export const authInterceptors: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  const isApiRequest = req.url.startsWith(`${environment.apiUrl}`);
  const publicPaths = ['/auth/login', '/auth/signup', '/public/'];
  const isPublic = publicPaths.some(path => req.url.startsWith(`${environment.apiUrl}${path}`));

  if (token && isApiRequest && !isPublic) {
    const cloned = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(cloned);
  }
  return next(req);
};
