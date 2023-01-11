import {State, Action, StateContext, Selector} from '@ngxs/store';
import {tap} from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {DrivingNotification} from "../models/notification/driving-notification";
import {DrivingNotificationService} from "../services/driving-notification-service/driving-notification.service";
import {AddDrivingNotification} from "../actions/driving-notification.action";

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
    // addDrivingNotification({patchState}: StateContext<DrivingNotificationStateModel>, {payload}: AddDrivingNotification){
    //     patchState({
    //         currentDrivingNotification: null
    //     })
    // }


    @Action(AddDrivingNotification)
    addDrivingNotification({getState, setState}: StateContext<DrivingNotificationStateModel>, {payload}: AddDrivingNotification) {
        return this.drivingNotificationService.create(payload).pipe(tap((result) => {
            const state = getState();
            setState({
                currentDrivingNotification: result
            });
        }));
    }

    // @Action(UpdateTodo)
    // updateTodo({getState, setState}: StateContext<TodoStateModel>, {payload, id}: UpdateTodo) {
    //     return this.todoService.updateTodo(payload, id).pipe(tap((result) => {
    //         const state = getState();
    //         const todoList = [...state.todos];
    //         const todoIndex = todoList.findIndex(item => item.id === id);
    //         todoList[todoIndex] = result;
    //         setState({
    //             ...state,
    //             todos: todoList,
    //         });
    //     }));
    // }

}

