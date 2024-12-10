import { AbstractControl } from '@angular/forms';

export function required(controls: AbstractControl) {
  if (controls.value.trim() !== '') {
    return null;
  }
  return { emptyValue: true };
}
