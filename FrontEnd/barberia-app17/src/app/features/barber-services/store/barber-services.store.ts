import { patchState, signalStore, withHooks, withMethods } from '@ngrx/signals';
import { Service } from '../models/service.interface';
import { setAllEntities, withEntities } from '@ngrx/signals/entities';
import {
  setLoaded,
  setLoading,
  withCallState,
} from '@angular-architects/ngrx-toolkit';
import { inject } from '@angular/core';
import { BarberServicesService } from '../services/barber-services.service';

export const BarberServicesStore = signalStore(
  { providedIn: 'root' },
  withEntities<Service>(),
  withCallState(),
  withMethods(
    (store, barberServicesService = inject(BarberServicesService)) => ({
      _loadAll: async () => {
        patchState(store, setLoading());
        const services = await barberServicesService.getAll();
        patchState(store, setAllEntities(services));

        patchState(store, setLoaded());
      },
    }),
  ),
  withHooks({
    onInit(store) {
      store._loadAll();
    },
  }),
);
