import { patchState, signalStore, withHooks, withMethods } from '@ngrx/signals';
import { setAllEntities, withEntities } from '@ngrx/signals/entities';
import { Profesional } from './models/profesional.interface';
import {
  setLoaded,
  setLoading,
  withCallState,
} from '@angular-architects/ngrx-toolkit';
import { inject } from '@angular/core';
import { ProfesionalsService } from './services/profesionals.service';

export const ProfesionalsStore = signalStore(
  { providedIn: 'root' },
  withEntities<Profesional>(),
  withCallState(),
  withMethods((store, profesionalsService = inject(ProfesionalsService)) => ({
    _loadAll: async () => {
      patchState(store, setLoading());
      const services = await profesionalsService.getAll();
      patchState(store, setAllEntities(services));

      patchState(store, setLoaded());
    },
  })),
  withHooks({
    onInit(store) {
      store._loadAll();
    },
  }),
);
