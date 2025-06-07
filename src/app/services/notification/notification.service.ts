import { Injectable } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private stompClient: Client | null = null;
  private subscription: StompSubscription | null | undefined = null;

  connect() {
    const token = localStorage.getItem('accessToken');
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(`http://localhost:8080/ws-notification?token=${token}`),
      reconnectDelay: 5000, // tự động kết nối lại sau 5s nếu mất kết nối
      debug: (str) => {
        console.log(str);
      }
    });

    this.stompClient.onConnect = (frame) => {
      console.log('WebSocket connected');

      // Subscribe vào queue riêng tư của user
      this.subscription = this.stompClient!.subscribe('/user/queue/notifications', (message: IMessage) => {
        console.log('Thông báo riêng tư:', message.body);
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker error:', frame.headers['message']);
      console.error('Details:', frame.body);
    };

    this.stompClient.activate(); // bắt đầu kết nối
  }

  disconnect() {
    if (this.subscription) {
      this.subscription.unsubscribe();
      this.subscription = null;
    }

    if (this.stompClient) {
      this.stompClient.deactivate();
      console.log('WebSocket disconnected');
    }
  }
}
