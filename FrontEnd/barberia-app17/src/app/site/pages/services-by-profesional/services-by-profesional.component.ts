import {
  afterNextRender,
  Component,
  inject,
  Input,
  numberAttribute,
  OnInit,
} from '@angular/core';
import { ProfesionalsService } from '../../../features/profesionals/services/profesionals.service';
import { patchState, signalState } from '@ngrx/signals';
import { Service } from '../../../features/barber-services/models/service.interface';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-services-by-profesional',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './services-by-profesional.component.html',
  styles: ``,
})
export class ServicesByProfesionalComponent implements OnInit {
  private readonly profesionalsService = inject(ProfesionalsService);

  @Input({ transform: numberAttribute }) profesionalId = 0;

  public profesionalServices = signalState<{
    employee: string;
    services: Service[];
  }>({
    employee: '',
    services: [],
  });

  ngOnInit() {
    this.profesionalsService
      .getServicesByEmployee(this.profesionalId)
      .subscribe((res) => {
        patchState(this.profesionalServices, res);
      });
  }
}
