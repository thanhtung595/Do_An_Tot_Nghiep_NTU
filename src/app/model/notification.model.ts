export interface Notification {
  id: number;
  patientname: string;
  doctorname: string;
  phone: string;
  type: 'appointment' | 'reminder' | 'result' | 'payment';
  title: string;
  content: string;
  date: Date;
  isread: boolean;
  status: 'pending' | 'sent' | 'failed';
  appointmentdate?: Date;
  appointmenttime?: string;
}
