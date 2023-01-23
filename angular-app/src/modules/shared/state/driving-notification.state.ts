import {State, Action, StateContext, Selector, Select} from '@ngxs/store';
import { Injectable } from '@angular/core';
import {DrivingNotification} from "../models/notification/driving-notification";
import {
  AddDrivingNotification,
  AddDrivings,
  ClearStore,
  UpdateDrivingNotification,
  UpdateMinutesStatusDrivingNotification, UpdateOnlyMinutesStatus,
  UpdateDrivings,
  UpdateStatusDrivingNotification
} from "../actions/driving-notification.action";
import {Observable} from "rxjs";
import { Driving } from '../models/driving/driving';

export class DrivingNotificationStateModel {
    currentDrivingNotification: DrivingNotification;
    activeDrivings: Driving[];
}

@State<DrivingNotificationStateModel>({
    name: 'currentDrivingNotification',
    defaults: {
        currentDrivingNotification: null,
        activeDrivings: []
    }
})
@Injectable()
export class DrivingNotificationState {

    @Selector()
    static getDrivingNotification(state: DrivingNotificationStateModel) {
        return state.currentDrivingNotification;
    }
    @Select() drivingNotificationState$: Observable<DrivingNotificationState>;

    @Action(AddDrivingNotification)
    addDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: AddDrivingNotification) {
        const state = getState();
        setState({
            ...state,
            currentDrivingNotification: payload
        });
        console.log(state);
    }


    @Action(UpdateMinutesStatusDrivingNotification)
    updateMinutesDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateMinutesStatusDrivingNotification) {
      console.log("updateMinutesDrivingNotification");
      const state = getState();
        state.currentDrivingNotification.minutes = payload.minutes;
        state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
        state.currentDrivingNotification.drivingId = payload.drivingId;
        setState({
            ...state,
            currentDrivingNotification: state.currentDrivingNotification
        });
    }

  @Action(UpdateOnlyMinutesStatus)
  updateOnlyMinutesStatus({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateOnlyMinutesStatus) {
    console.log("updateOnlyMinutesStatus");
      const state = getState();
    console.log(payload)
    state.currentDrivingNotification.minutes = payload.minutes;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification
    });
    console.log(state);
  }

    @Action(UpdateStatusDrivingNotification)
    updateStatusDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateStatusDrivingNotification) {
      try {
        console.log("updateStatusDrivingNotification");
        console.log(payload);
        const state = getState();
        console.log("blafd");
        console.log(state);
        state.currentDrivingNotification.active = payload.active;
        state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
        setState({
            ...state,
            currentDrivingNotification: state.currentDrivingNotification
        });
        console.log(state);
      } catch (error) {
        console.log("eroor se javio");
      }
    }

    @Action(UpdateDrivingNotification)
    updateDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateDrivingNotification) {
      const state = getState();
      console.log("updateDrivingNotification");
      if (state.currentDrivingNotification){
        state.currentDrivingNotification.drivingId = payload.drivingId;
        state.currentDrivingNotification.minutes = payload.minutes;
        state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
        state.currentDrivingNotification.price = payload.cost;
        state.currentDrivingNotification.active = payload.active;
        state.currentDrivingNotification.vehicleId= payload.vehicleId;
        state.currentDrivingNotification.route = payload.route;
        setState({
            ...state,
            currentDrivingNotification: state.currentDrivingNotification
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
            senderEmail: null
          }
        })
      }
    }

  @Action(ClearStore)
  clearStore({getState, setState}: StateContext<DrivingNotificationStateModel>) {
    const state = getState();
    setState({
      ...state,
      currentDrivingNotification: null
    });
  }

  @Selector()
    static getDrivings(state: DrivingNotificationStateModel) {
        return state.activeDrivings;
    }

    @Select() drivingsState$: Observable<DrivingNotificationState>;


    @Action(AddDrivings)
    addDrivings({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: AddDrivings) {
        console.log("fdfafs");
        const state = getState();
        setState({
            ...state,
            activeDrivings: payload
        });
        // const state = getState();
        console.log(state);
    }

    @Action(UpdateDrivings)
    updateDrivings({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateDrivings) {
        console.log(payload);
        const state = getState();
        console.log(state);
        state.activeDrivings.push(payload)
        console.log(state);
        setState({
            ...state,
            activeDrivings: state.activeDrivings
        });
    }

}


