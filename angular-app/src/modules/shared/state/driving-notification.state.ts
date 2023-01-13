import {State, Action, StateContext, Selector} from '@ngxs/store';
import {tap} from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {DrivingNotification} from "../models/notification/driving-notification";
import {DrivingNotificationService} from "../services/driving-notification-service/driving-notification.service";
import {AddDrivingNotification, UpdateDrivingNotification, UpdateMinutesStatusDrivingNotification, UpdateStatusDrivingNotification} from "../actions/driving-notification.action";
import { SimpleDrivingInfo } from '../models/driving/simple-driving-info';

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

    constructor(private drivingNotificationService: DrivingNotificationService) {
    }

    @Selector()
    static getDrivingNotification(state: DrivingNotificationStateModel) {
        return state.currentDrivingNotification;
    }


    // @Action(AddDrivingNotification)
    // addDrivingNotification({setState}: StateContext<DrivingNotificationStateModel>, {payload}: AddDrivingNotification) {
    //     console.log(payload);
    //     return this.drivingNotificationService.create(payload).pipe(tap((result) => {
        
    //         setState({
    //             currentDrivingNotification: result
    //         });
    //     }));
    // }

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
        setState({
            ...state,
            currentDrivingNotification: state.currentDrivingNotification
        });
    }

    @Action(UpdateStatusDrivingNotification)
    updateStatusDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateStatusDrivingNotification) {
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
    }

    @Action(UpdateDrivingNotification)
    updateDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: UpdateDrivingNotification) {
        console.log(payload);
        const state = getState();
        state.currentDrivingNotification.drivingId = payload.drivingId;
        state.currentDrivingNotification.minutes = payload.minutes;
        state.currentDrivingNotification.drivingStatus = payload.drivingStatus;
        state.currentDrivingNotification.price = payload.cost;
        state.currentDrivingNotification.active = payload.active;
        setState({
            ...state,
            currentDrivingNotification: state.currentDrivingNotification
        });
    }
}


