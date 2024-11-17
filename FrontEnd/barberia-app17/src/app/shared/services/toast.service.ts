import { Injectable, inject } from '@angular/core';
import { Message, MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  readonly #messageService = inject(MessageService);

  showToast(opts: Message): void {
    this.#messageService.add({ ...opts, key: opts.key ?? 'br' });
  }
}
