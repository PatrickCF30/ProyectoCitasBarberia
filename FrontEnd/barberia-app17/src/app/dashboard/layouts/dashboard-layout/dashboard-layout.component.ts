import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [],
  templateUrl: './dashboard-layout.component.html',
  styleUrl: './dashboard-layout.component.css',
})
export class DashboardLayoutComponent {
  public isCollapsed = signal<boolean>(false);

  toggleCollapsed() {
    this.isCollapsed.update((value) => !value);
  }
}
