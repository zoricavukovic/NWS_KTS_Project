import {State, Action, StateContext, Selector, Store, Select} from '@ngxs/store';
import { Injectable } from '@angular/core';
import {DrivingNotification} from "../models/notification/driving-notification";
import {
  AddDrivingNotification,
  AddDrivings,
  ClearStore,
  UpdateDrivingNotification,
  UpdateDrivings,
  UpdateMinutesStatusDrivingNotification,
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
        console.log("fdfafs");
        const state = getState();
        setState({
            ...state,
            currentDrivingNotification: payload
        });
        // const state = getState();
        console.log(state);
    }


    @Action(UpdateMinutesStatusDrivingNotification)
    updateMinutesDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateMinutesStatusDrivingNotification) {
        console.log(payload);
        const state = getState();
        console.log("blafd");
        console.log(state);
        state.currentDrivingNotification.minutes = payload.minutes;
        state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
        state.currentDrivingNotification.drivingId = payload.drivingId;
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


