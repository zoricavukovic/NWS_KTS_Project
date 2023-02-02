import {FormBuilder, FormGroup} from "@angular/forms";
import {SearchingRoutesForm} from "../models/route/searching-routes-form";
import {Route} from "../models/route/route";

export function createEmptySearchForm(formBuilder: FormBuilder): FormGroup {
  return formBuilder.group(new SearchingRoutesForm());
}

export function createEmptyRoute(): Route {
  return {
    locations: [],
    distance: 0,
    timeInMin: 0,
    routePathIndex: [],
  };
}
