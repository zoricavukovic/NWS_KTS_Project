import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Message } from '../model/response/messages/message';
import { ChatRoom } from '../model/response/messages/chat-room';
import { MessageRequest } from '../model/request/message-request';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  messages$ = new BehaviorSubject<Message[]>([]);

  constructor( 
    private http: HttpClient,
    private configService: ConfigService
    ) { }

  getAllMessages() : Observable<ChatRoom> {
    
    return this.http.get<ChatRoom>(this.configService.all_messages);
  }

  getMessagesPerUser(email: string) : BehaviorSubject<Message[]> {
    this.http.get<Message[]>(this.configService.all_messages + "/" + email).subscribe(
      res => {
        this.messages$.next(res);
      });

      return this.messages$;
  }

  sendMessage(messageReq: MessageRequest): Observable<Message> {

    return this.http.post<Message>(this.configService.all_messages, messageReq);
  }

  addMessage(message: Message) {
    
    this.messages$.next(this.messages$.value.concat(message));
  }


}
