import { Routes } from '@angular/router';
import { WelcomeComponent } from './features/welcome/welcome.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { AnalyticsComponent } from './features/analytics/analytics.component';
import { SettingsComponent } from './features/settings/settings.component';
import { ReceiptsComponent } from './features/receipts/receipts.component';

export const routes: Routes = [
    {
        path: '',
        loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
    },
    {
        path: 'auth',
        loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
    },
    {
        path: 'dashboard',
        canActivate: [authGuard],
        component: DashboardComponent
    },
    {
        path: 'receipts',
        canActivate: [authGuard],
        component: ReceiptsComponent
    },
    {
        path: 'analytics',
        canActivate: [authGuard],
        component: AnalyticsComponent
    },
    {
        path: 'settings',
        canActivate: [authGuard],
        component: SettingsComponent
    },
    { path: '**', redirectTo: '' }
];