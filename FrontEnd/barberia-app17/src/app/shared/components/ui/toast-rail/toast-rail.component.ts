import { Component } from '@angular/core';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'toast-rail',
  standalone: true,
  imports: [ToastModule],
  templateUrl: './toast-rail.component.html',
})
export class ToastRailComponent {
  protected breakpoints = {
    '640px': {
      width: '100%',
      right: 0,
      left: 0,
    },
  };
}
