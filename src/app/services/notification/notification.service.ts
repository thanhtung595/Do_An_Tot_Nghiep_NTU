import { Injectable } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { BehaviorSubject, Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private stompClient: Client | null = null;
  private subscriptions: Map<string, StompSubscription> = new Map();
  private notificationSubjects: Map<string, BehaviorSubject<any>> = new Map();

  // BehaviorSubject báo trạng thái kết nối (true khi connect xong)
  private connectionStatusSubject = new BehaviorSubject<boolean>(false);
  public connectionStatus$ = this.connectionStatusSubject.asObservable();

  connect() {
    const token = localStorage.getItem('accessToken');

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(`${API_BASE_URL}ws-notification?access_token=${token}`),
      reconnectDelay: 55000,
      connectHeaders: {}
    });

    this.stompClient.onConnect = (frame) => {
      console.log('WebSocket connected success.');
      this.connectionStatusSubject.next(true); // báo đã kết nối
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker error:', frame.headers['message']);
      console.error('Details:', frame.body);
    };

    this.stompClient.activate();
  }

  subscribeToTopic(topic: string): Observable<any> {
    if (!this.stompClient) {
      throw new Error('WebSocket chưa được khởi tạo');
    }

    if (!this.stompClient.connected) {
      throw new Error('WebSocket chưa kết nối hoặc chưa activate');
    }

    if (this.notificationSubjects.has(topic)) {
      return this.notificationSubjects.get(topic)!.asObservable();
    }

    const subject = new BehaviorSubject<any>(null);
    this.notificationSubjects.set(topic, subject);

    const subscription = this.stompClient.subscribe(topic, (message: IMessage) => {
      try {
        const notification = JSON.parse(message.body);
        subject.next(notification);
      } catch (error) {
        console.error('Lỗi parse notification JSON:', error);
      }
    });

    this.subscriptions.set(topic, subscription);

    return subject.asObservable();
  }

  unsubscribeFromTopic(topic: string) {
    const subscription = this.subscriptions.get(topic);
    if (subscription) {
      subscription.unsubscribe();
      this.subscriptions.delete(topic);
    }

    const subject = this.notificationSubjects.get(topic);
    if (subject) {
      subject.complete();
      this.notificationSubjects.delete(topic);
    }
  }

  disconnect() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions.clear();

    this.notificationSubjects.forEach(subj => subj.complete());
    this.notificationSubjects.clear();

    if (this.stompClient) {
      this.stompClient.deactivate();
      console.log('WebSocket disconnected');
      this.connectionStatusSubject.next(false);
    }
  }
}
