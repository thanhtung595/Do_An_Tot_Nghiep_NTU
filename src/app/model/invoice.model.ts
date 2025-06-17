export interface Invoice {
  id: number;
  patientid: number;
  patientname: string;
  age: number;
  gender: string;
  address: string;
  phone: string;
  symptoms: string;
  diagnosis: string;
  treatment: string;
  medications: string[];
  services: string[];
  notes: string;
  nextAppointment?: string;
  totalmoney: number;
  ispaid: boolean;
  date: Date;
}
