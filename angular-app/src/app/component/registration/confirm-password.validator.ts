import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function matchPasswordsValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const passwordControl = control.get('passwordFormControl');
        const confirmPasswordControl = control.get('passwordAgainFormControl');
        return passwordControl.value !== confirmPasswordControl.value ? {mismatch:true} : null;
  }
}

