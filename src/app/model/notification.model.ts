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
  status: string;
  appointmentdate?: Date;
  appointmenttime?: string;
  userfrom?: string;
  userto?: string;
}
