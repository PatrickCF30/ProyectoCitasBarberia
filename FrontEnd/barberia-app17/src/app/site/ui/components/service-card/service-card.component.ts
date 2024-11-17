import { Component, input } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { Service } from '../../../../features/barber-services/models/service.interface';

@Component({
  selector: 'service-card',
  standalone: true,
  imports: [CurrencyPipe],
  template: `
    <div
      class="grid grid-rows-subgrid rounded p-4 bg-zinc-900 space-y-[1em] row-span-4"
    >
      <h3 class="font-serif text-xl text-gray-200">{{ service().name }}</h3>
      <p>{{ service().description }}</p>
      <p class="text-base text-gray-100">{{ service().price | currency }}</p>
      <a
        class="inline-block w-fit py-1.5 px-2.5 rounded bg-gold-400 hover:bg-gold-400/90 transition-colors text-gray-200"
        href=""
      >
        Elegir
      </a>
    </div>
  `,
  styles: ``,
})
export class ServiceCardComponent {
  public service = input.required<Service>();
}
