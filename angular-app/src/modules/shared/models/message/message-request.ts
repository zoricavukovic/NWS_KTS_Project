export interface MessageRequest {
  chatId: number;
  message: string;
  senderEmail: string;
  receiverEmail: string;
  adminResponse: boolean;
}

export class MessageSeenRequest {
  chatRoomId: number;
  adminLogged: boolean;

  constructor(chatRoomId: number, adminLogged: boolean) {
    this.chatRoomId = chatRoomId;
    this.adminLogged = adminLogged;
  }
}
