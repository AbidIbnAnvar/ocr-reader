import { Component, inject } from '@angular/core';
import { SettingsService } from '../../../../core/services/settings.service';

@Component({
  selector: 'app-theme-toggle',
  imports: [],
  templateUrl: './theme-toggle.component.html',
  styleUrl: './theme-toggle.component.css'
})
export class ThemeToggleComponent {
  private settingsService = inject(SettingsService);
  darkMode = this.settingsService.getDarkMode();

  toggleTheme() {
    this.settingsService.toggleDarkMode();
  }
}
