import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {
  Measurement,
  CreateMeasurementRequest,
  UpdateMeasurementRequest,
} from '../models/measurement.model';

@Injectable({
  providedIn: 'root',
})
export class MeasurementService {
  private readonly apiUrl = 'http://localhost:8080/api/measurements'; // Adjust based on backend URL

  constructor(private http: HttpClient) {}

  getAllMeasurements(): Observable<Measurement[]> {
    return this.http
      .get<Measurement[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  getMeasurementById(id: string): Observable<Measurement> {
    return this.http
      .get<Measurement>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  createMeasurement(
    measurement: CreateMeasurementRequest
  ): Observable<Measurement> {
    return this.http
      .post<Measurement>(this.apiUrl, measurement)
      .pipe(catchError(this.handleError));
  }

  updateMeasurement(
    id: string,
    measurement: UpdateMeasurementRequest
  ): Observable<Measurement> {
    return this.http
      .put<Measurement>(`${this.apiUrl}/${id}`, measurement)
      .pipe(catchError(this.handleError));
  }

  logicalDeleteMeasurement(id: string): Observable<void> {
    return this.http
      .delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => errorMessage);
  }
}
