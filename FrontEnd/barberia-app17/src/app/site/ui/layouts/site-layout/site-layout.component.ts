import { Component } from '@angular/core';
import { SiteHeaderComponent } from '../../components/site-header/site-header.component';
import { RouterOutlet } from '@angular/router';

@Component({
  standalone: true,
  imports: [SiteHeaderComponent, RouterOutlet],
  templateUrl: './site-layout.component.html',
})
export class SiteLayoutComponent {}
