import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class FormValidatorsService {
  public static readonly firstNameAndLastnamePattern: string =
    '([a-zA-Z]+) ([a-zA-Z]+)';
  public static readonly emailPattern: string =
    '^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$';

  public isNotValidField(form: FormGroup, field: string) {
    return form.controls[field].errors && form.controls[field].touched;
  }
}
