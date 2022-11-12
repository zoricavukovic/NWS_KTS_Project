import { FormGroup, ValidationErrors, ValidatorFn } from "@angular/forms";

export function matchPasswordsValidator(): ValidatorFn {
    return (formGroup: FormGroup): ValidationErrors | null => {
        const passwordControl = formGroup.get('passwordFormControl');
        const confirmPasswordControl = formGroup.get('passwordAgainFormControl');
        console.log(passwordControl.value !== confirmPasswordControl.value);
        return passwordControl.value !== confirmPasswordControl.value ? {mismatch:true} : null;
  }
}

