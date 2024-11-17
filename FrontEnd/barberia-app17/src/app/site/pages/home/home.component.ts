import { Component, inject, Signal } from '@angular/core';
import { HeroComponent } from '../../ui/components/hero/hero.component';
import { CurrencyPipe, NgOptimizedImage } from '@angular/common';
import { BarberServicesStore } from '../../../features/barber-services/store/barber-services.store';
import { Service } from '../../../features/barber-services/models/service.interface';
import { ProfesionalsStore } from '../../../features/profesionals/profesionals.store';
import { Profesional } from '../../../features/profesionals/models/profesional.interface';

@Component({
  standalone: true,
  imports: [HeroComponent, NgOptimizedImage, CurrencyPipe],
  templateUrl: './home.component.html',
  styles: ``,
})
export class HomeComponent {
  private readonly barberServicesStore = inject(BarberServicesStore);
  private readonly profesionalsStore = inject(ProfesionalsStore);

  public services: Signal<Service[]> = this.barberServicesStore.entities;
  public profesionals: Signal<Profesional[]> = this.profesionalsStore.entities;
}
