// interfaces/citas.interfaces.ts
export interface Cita {
  datetime: string;
  service: string;
  end_time: string;
  cancelled: boolean;
  id: number;
  employee: string;
  customer: string;
}

export interface CitasResponse {
  citas: Cita[];
}
  
  export interface Service {
    id: number;
    name: string;
    description: string;
    price: number;
    duration_minutes: number;
  }
  
  export interface ServiceResponse {
    fecha: string;
    services: Service;
    mensaje: string;
    status: string;
  }