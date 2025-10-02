import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http'

import { routes } from './app.routes';
import { authInterceptors } from './core/interceptors/auth.interceptors';
import { provideToastr } from 'ngx-toastr'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { errorInterceptors } from './core/interceptors/error.interceptors';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts'
import { provideNativeDateAdapter } from '@angular/material/core'

import { LogoutOutline, FileAddOutline, PieChartOutline, SettingOutline } from "@ant-design/icons-angular/icons";
import { registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { FormsModule } from '@angular/forms';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideIcons } from '@ng-icons/core';
import { lucidePlus, lucideTrash2 } from '@ng-icons/lucide';

registerLocaleData(en);

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([authInterceptors, errorInterceptors])
    ),
    provideToastr({
      positionClass: 'toast-bottom-right',
      timeOut: 3000,
      progressBar: false,
      closeButton: false,
      preventDuplicates: true,
    }),
    importProvidersFrom(BrowserAnimationsModule),
    provideCharts(withDefaultRegisterables()),
    provideNativeDateAdapter(),
    importProvidersFrom(
      FormsModule),
    provideAnimationsAsync(),
    provideHttpClient(),
    provideIcons({
      lucidePlus,
      lucideTrash2
    })
  ]
};
