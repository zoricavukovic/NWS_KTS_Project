import {State, Action, StateContext, Selector, Select} from '@ngxs/store';
import { Injectable } from '@angular/core';
import {DrivingNotification} from "../models/notification/driving-notification";
import {
  AddDrivingNotification,
  ClearStore,
  UpdateDrivingNotification,
  UpdateMinutesStatusDrivingNotification, UpdateOnlyMinutesStatus,
  UpdateStatusDrivingNotification
} from "../actions/driving-notification.action";
import {Observable} from "rxjs";

export class DrivingNotificationStateModel {
    currentDrivingNotification: DrivingNotification
}

@State<DrivingNotificationStateModel>({
    name: 'currentDrivingNotification',
    defaults: {
        currentDrivingNotification: null
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
    const state = getState();
    state.currentDrivingNotification.minutes = payload.minutes;
    setState({
      ...state,
      currentDrivingNotification: state.currentDrivingNotification
    });
  }

    @Action(UpdateStatusDrivingNotification)
    updateStatusDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateStatusDrivingNotification) {
      try {
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
      } catch (error) {
        console.log("eroor se javio");
      }
    }

    @Action(UpdateDrivingNotification)
    updateDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateDrivingNotification) {
      const state = getState();

      if (state.currentDrivingNotification){
        state.currentDrivingNotification.drivingId = payload.drivingId;
        state.currentDrivingNotification.minutes = payload.minutes;
        state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
        state.currentDrivingNotification.price = payload.cost;
        state.currentDrivingNotification.active = payload.active;
        state.currentDrivingNotification.vehicleId= payload.vehicleId;
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
            route: null,
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

}


