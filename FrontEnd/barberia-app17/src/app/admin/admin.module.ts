import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';

@NgModule({
  declarations: [], // Remover AdminLayoutComponent de aquí
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }