import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { Message } from '../model/response/messages/message';
import { ChatRoom } from '../model/response/messages/chat-room';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor( 
    private http: HttpClient,
    private configService: ConfigService
    ) { }

  getAllMessages() : Observable<ChatRoom> {

    return this.http.get<ChatRoom>(this.configService.all_messages)
  }


}
