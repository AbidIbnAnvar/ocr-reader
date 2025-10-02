import { AbstractControl, ValidationErrors } from '@angular/forms';

export function nonEmptyFormArray(control: AbstractControl): ValidationErrors | null {
    const formArray = control as unknown as { length: number };
    return formArray && formArray.length > 0 ? null : { required: true };
}
