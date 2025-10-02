import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ThemeToggleComponent } from '../../theme-toggle/theme-toggle.component/theme-toggle.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, ThemeToggleComponent, MatMenuModule, MatIconModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  get user() {
    return this.authService.currentUserValue;
  }

  get userInitials() {
    return this.user?.username?.charAt(0) || 'U';
  }

  constructor(
    private authService: AuthService
  ) { }

}
