import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const excludedPaths = ['/api/auth/login', '/api/auth/signup'];

  const shouldSkip = excludedPaths.some((path) => req.url.includes(path));

  if (shouldSkip) {
    return next(req);
  }

  const token = localStorage.getItem('token');

  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    return next(authReq);
  }

  return next(req);
};
