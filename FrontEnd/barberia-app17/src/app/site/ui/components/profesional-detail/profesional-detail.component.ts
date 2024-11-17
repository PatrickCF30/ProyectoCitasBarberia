import { Component, Input, numberAttribute } from '@angular/core';

@Component({
  selector: 'app-profesional-detail',
  standalone: true,
  imports: [],
  templateUrl: './profesional-detail.component.html',
  styles: ``,
})
export class ProfesionalDetailComponent {
  @Input({ transform: numberAttribute }) id = 0;
}
