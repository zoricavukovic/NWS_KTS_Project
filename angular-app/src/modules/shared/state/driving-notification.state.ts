import { State, Action, StateContext, Selector, Select } from '@ngxs/store';
import { Injectable } from '@angular/core';
import { DrivingNotification } from '../models/notification/driving-notification';
import {
  AddDrivingNotification,
  AddDrivings,
  ClearStore,
  UpdateDrivingNotification,
  UpdateMinutesStatusDrivingNotification,
  UpdateOnlyMinutesStatus,
  UpdateDrivings,
  UpdateStatusDrivingNotification,
  ResetVehicleInDrivingNotification,
  UpdateIdDrivingNotification,
  RemoveDriving,
  SimpleUpdateDrivingNotification,
  UpdateDurationDrivingNotification,
  UpdateIfDriverChooseWrongRoute,
} from '../actions/driving-notification.action';
import { Observable } from 'rxjs';
import { Driving } from '../models/driving/driving';

export class DrivingNotificationStateModel {
  currentDrivingNotification: DrivingNotification;
  activeDrivings: Driving[];
}

@State<DrivingNotificationStateModel>({
  name: 'currentDrivingNotification',
  defaults: {
    currentDrivingNotification: null,
    activeDrivings: [],
  },
})
@Injectable()
export class DrivingNotificationState {
  @Selector()
  static getDrivingNotification(state: DrivingNotificationStateModel) {
    return state.currentDrivingNotification;
  }
  @Select() drivingNotificationState$: Observable<DrivingNotificationState>;

  @Action(AddDrivingNotification)
  addDrivingNotification(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: AddDrivingNotification
  ) {
    const state = getState();
    state.currentDrivingNotification = payload;
    state.currentDrivingNotification.wrongRoute = false;
    setState({
      ...state,
      currentDrivingNotification: payload,
    });
    console.log(state);
  }

  @Action(UpdateMinutesStatusDrivingNotification)
  updateMinutesDrivingNotification(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: UpdateMinutesStatusDrivingNotification
  ) {
    console.log('updateMinutesDrivingNotification');
    const state = getState();
    state.currentDrivingNotification.minutes = payload.minutes;
    state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
    state.currentDrivingNotification.drivingId = payload.drivingId;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification,
    });
  }

  @Action(UpdateOnlyMinutesStatus)
  updateOnlyMinutesStatus(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: UpdateOnlyMinutesStatus
  ) {
    const state = getState();
    state.currentDrivingNotification.minutes = payload.minutes;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification,
    });
  }

  @Action(UpdateStatusDrivingNotification)
  updateStatusDrivingNotification(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: UpdateStatusDrivingNotification
  ) {
    try {
      const state = getState();
      state.currentDrivingNotification.active = payload.active;
      state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
      if (payload.started) {
        state.currentDrivingNotification.started = payload.started;
      }

      setState({
        ...state,
        currentDrivingNotification: state.currentDrivingNotification,
      });
      console.log(state);
    } catch (error) {
      console.log('Error');
    }
  }
  @Action(UpdateDrivingNotification)
  updateDrivingNotification(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: UpdateDrivingNotification
  ) {
    const state = getState();
    if (state.currentDrivingNotification) {
      state.currentDrivingNotification.drivingId = payload.drivingId;
      state.currentDrivingNotification.minutes = payload.minutes;
      state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
      state.currentDrivingNotification.price = payload.cost;
      state.currentDrivingNotification.active = payload.active;
      state.currentDrivingNotification.vehicleId = payload.vehicleId;
      state.currentDrivingNotification.route = payload.route;
      state.currentDrivingNotification.started = payload.started;
      setState({
        ...state,
        currentDrivingNotification: state.currentDrivingNotification,
      });
    } else {
      setState({
        ...state,
        currentDrivingNotification: {
          drivingId: payload.drivingId,
          minutes: payload.minutes,
          drivingStatus: payload.drivingStatus,
          price: payload.cost,
          active: payload.active,
          vehicleId: payload.vehicleId,
          route: payload.route,
          senderEmail: null,
        },
      });
    }
  }

  @Action(SimpleUpdateDrivingNotification)
  simpleUpdateDrivingNotification(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: SimpleUpdateDrivingNotification
  ) {
    const state = getState();
    console.log(payload.route);
    state.currentDrivingNotification.active = payload.active;
    state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
    state.currentDrivingNotification.vehicleId = payload.vehicleId;
    state.currentDrivingNotification.vehicleType = payload.vehicleType;
    state.currentDrivingNotification.drivingId = payload.drivingId;
    state.currentDrivingNotification.route = payload.route;
    state.currentDrivingNotification.duration = payload.minutes;
    state.currentDrivingNotification.minutes = payload.minutes;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification,
    });
  }

  @Action(UpdateDurationDrivingNotification)
  updateDurationDrivingNotification(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: UpdateDurationDrivingNotification
  ) {
    const state = getState();
    state.currentDrivingNotification.duration = payload.duration;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification,
    });
  }

  @Action(ClearStore)
  clearStore({
    getState,
    setState,
  }: StateContext<DrivingNotificationStateModel>) {
    const state = getState();
    setState({
      ...state,
      currentDrivingNotification: null,
    });
  }

  @Action(ResetVehicleInDrivingNotification)
  resetVehicleInDrivingNotification({
    getState,
    setState,
  }: StateContext<DrivingNotificationStateModel>) {
    const state = getState();
    state.currentDrivingNotification.vehicleId = -1;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification,
    });
  }

  @Action(UpdateIdDrivingNotification)
  updateIdDrivingNotification(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: UpdateIdDrivingNotification
  ) {
    const state = getState();
    state.currentDrivingNotification.drivingId = payload.drivingId;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification,
    });

    console.log(state);
  }

  @Action(UpdateIfDriverChooseWrongRoute)
  updateIfDriverChooseWrongRoute(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: UpdateIfDriverChooseWrongRoute
  ) {
    const state = getState();
    state.currentDrivingNotification.wrongRoute = payload;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification,
    });

    console.log(state);
  }

  @Selector()
  static getDrivings(state: DrivingNotificationStateModel) {
    return state.activeDrivings;
  }

  @Select() drivingsState$: Observable<DrivingNotificationState>;

  @Action(AddDrivings)
  addDrivings(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: AddDrivings
  ) {
    const state = getState();
    setState({
      ...state,
      activeDrivings: payload,
    });
  }

  @Action(UpdateDrivings)
  updateDrivings({ getState, setState }: StateContext<DrivingNotificationStateModel>, { payload }: UpdateDrivings) {
    console.log(payload);
    const state = getState();
    console.log(state);
    state.activeDrivings.push(payload);
    console.log(state);
    setState({
      ...state,
      activeDrivings: state.activeDrivings,
    });
  }

  @Action(RemoveDriving)
  removeDriving(
    { getState, setState }: StateContext<DrivingNotificationStateModel>,
    { payload }: RemoveDriving
  ) {
    console.log(payload);
    const state = getState();
    for (const driving of state.activeDrivings) {
      if (driving.id === payload) {
        const indexOfDriving = state.activeDrivings.indexOf(driving);
        state.activeDrivings.slice(indexOfDriving, 1);
      }
    }

    console.log(state);
    setState({
      ...state,
      activeDrivings: state.activeDrivings,
    });
  }
}
