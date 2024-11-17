import { Component, inject, Signal } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProfesionalsStore } from '../../../features/profesionals/profesionals.store';
import { Profesional } from '../../../features/profesionals/models/profesional.interface';

@Component({
  selector: 'app-profesionals',
  standalone: true,
  imports: [NgOptimizedImage, RouterLink],
  templateUrl: './profesionals.component.html',
  styles: ``,
})
export class ProfesionalsComponent {
  private readonly profesionalsStore = inject(ProfesionalsStore);

  public profesionals: Signal<Profesional[]> = this.profesionalsStore.entities;
}
