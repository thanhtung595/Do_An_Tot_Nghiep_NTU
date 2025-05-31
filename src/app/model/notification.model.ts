export interface Notification {
  id: number;
  patientName: string;
  phone: string;
  type: 'appointment' | 'reminder' | 'result' | 'payment';
  title: string;
  content: string;
  date: Date;
  isRead: boolean;
  status: 'pending' | 'sent' | 'failed';
  appointmentDate?: Date;
  appointmentTime?: string;
}
