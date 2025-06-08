export interface Measurement {
  id?: string;
  uuid: string;
  patientId: string;
  result: number;
  deleted?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateMeasurementRequest {
  patientId: string;
  result: number;
}

export interface UpdateMeasurementRequest {
  patientId?: string;
  result?: number;
}
