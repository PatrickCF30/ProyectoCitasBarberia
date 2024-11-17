import {
  Component,
  inject,
  input,
  Input,
  numberAttribute,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { AppointmentService } from '../../../features/appointments/services/appointment.service';
import { map, Subscription, switchMap, tap } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthFacade } from '../../../auth/services/auth-facade.service';
import { ToastService } from '../../../shared/services/toast.service';

@Component({
  selector: 'app-book-appointment',
  standalone: true,
  imports: [CalendarModule, FormsModule, DatePipe],
  templateUrl: './book-appointment.component.html',
  styles: ``,
})
export class BookAppointmentComponent implements OnInit, OnDestroy {
  private readonly appointmentService = inject(AppointmentService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly authFacade = inject(AuthFacade);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);

  @Input({ transform: numberAttribute }) serviceId = 0;
  @Input({ transform: numberAttribute }) profesionalId = 0;

  userId = this.authFacade.userId;
  date: Date | undefined;
  selectedDate = signal<Date>(new Date());
  selectedTime = signal<string | null>(null);
  selectedDate$ = toObservable(this.selectedDate);
  public subscription!: Subscription;

  public service = toSignal(
    this.activatedRoute.data.pipe(map(({ service }) => service)),
    { initialValue: '' },
  );

  minDate: Date | undefined;

  times: string[] = [
    '09:00',
    '10:00',
    '11:00',
    '12:00',
    '13:00',
    '14:00',
    '15:00',
    '16:00',
    '17:00',
    '18:00',
    '19:00',
    '20:00',
  ];

  availableTimes = signal<string[]>([]);

  ngOnInit() {
    this.minDate = new Date();

    this.subscription = this.selectedDate$
      .pipe(
        tap((date) => console.log('selectedDate pipe', date)),
        switchMap((date) =>
          this.appointmentService.getAvailableTimes(
            this.profesionalId,
            date.toISOString().split('T')[0],
          ),
        ),
        tap((data) => {
          console.log('after switch map', data);
          this.availableTimes.set(data.horariosDisponibles);
        }),
      )
      .subscribe();

    console.log(this.serviceId, this.profesionalId);
  }

  protected readonly format = format;
  protected readonly es = es;

  selectTime(time: string) {
    this.selectedTime.set(time); // Establece el horario seleccionado
  }

  async reservar() {
    await this.appointmentService.reservar(
      this.profesionalId,
      this.serviceId,
      this.selectedDate(),
      this.selectedTime() ?? '11:00',
      this.userId(),
    );

    this.toastService.showToast({
      severity: 'success',
      detail: 'Cita reservada con éxito',
      summary: 'Éxito',
    });

    await this.router.navigateByUrl('/turnos');
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
