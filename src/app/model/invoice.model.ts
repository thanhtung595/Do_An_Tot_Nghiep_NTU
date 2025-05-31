export interface Invoice {
  id: number;
  patientName: string;
  age: number;
  gender: string;
  address: string;
  phone: string;
  symptoms: string;
  diagnosis: string;
  treatment: string;
  medications: string[];
  notes: string;
  nextAppointment?: string;
  totalMoney: number;
  isPaid: boolean;
  date: Date;
}
