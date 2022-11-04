import { FormGroup } from '@angular/forms';

export function isFormValid(validationFrom: FormGroup){

    return !validationFrom.invalid;
}