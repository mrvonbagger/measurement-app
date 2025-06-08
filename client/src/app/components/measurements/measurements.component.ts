import { Component, OnInit } from '@angular/core';
import { MeasurementService } from '../../services/measurement.service';
import {
  Measurement,
  CreateMeasurementRequest,
  UpdateMeasurementRequest,
} from '../../models/measurement.model';
import { CommonModule } from '@angular/common';
import { MeasurementFormComponent } from '../measurement-form/measurement-form.component';
import { MeasurementListComponent } from '../measurement-list/measurement-list.component';

@Component({
  selector: 'app-measurements',
  standalone: true,
  imports: [CommonModule, MeasurementFormComponent, MeasurementListComponent],
  templateUrl: './measurements.component.html',
  styleUrls: ['./measurements.component.css'],
})
export class MeasurementsComponent implements OnInit {
  measurements: Measurement[] = [];
  editingMeasurement: Measurement | null = null;
  isLoading = false;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(private measurementService: MeasurementService) {}

  ngOnInit() {
    this.loadMeasurements();
  }

  loadMeasurements() {
    this.isLoading = true;
    this.measurementService.getAllMeasurements().subscribe({
      next: (measurements) => {
        this.measurements = measurements.filter((m) => !m.deleted);
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load measurements: ' + error;
        this.isLoading = false;
      },
    });
  }

  onSubmitForm(formData: CreateMeasurementRequest | UpdateMeasurementRequest) {
    this.isSubmitting = true;
    this.clearMessages();

    if (this.editingMeasurement) {
      // Update existing measurement
      this.measurementService
        .updateMeasurement(this.editingMeasurement.id!, formData)
        .subscribe({
          next: () => {
            this.successMessage = 'Measurement updated successfully!';
            this.loadMeasurements();
            this.onCancelForm();
            this.isSubmitting = false;
          },
          error: (error) => {
            this.errorMessage = 'Failed to update measurement: ' + error;
            this.isSubmitting = false;
          },
        });
    } else {
      // Create new measurement
      this.measurementService
        .createMeasurement(formData as CreateMeasurementRequest)
        .subscribe({
          next: () => {
            this.successMessage = 'Measurement created successfully!';
            this.loadMeasurements();
            this.onCancelForm();
            this.isSubmitting = false;
          },
          error: (error) => {
            this.errorMessage = 'Failed to create measurement: ' + error;
            this.isSubmitting = false;
          },
        });
    }
  }

  onEditMeasurement(measurement: Measurement) {
    this.editingMeasurement = measurement;
    this.clearMessages();
  }

  onCancelForm() {
    this.editingMeasurement = null;
  }

  onDeleteMeasurement(id: string) {
    if (confirm('Are you sure you want to delete this measurement?')) {
      this.measurementService.logicalDeleteMeasurement(id).subscribe({
        next: () => {
          this.successMessage = 'Measurement deleted successfully!';
          this.loadMeasurements();
        },
        error: (error) => {
          this.errorMessage = 'Failed to delete measurement: ' + error;
        },
      });
    }
  }

  private clearMessages() {
    this.successMessage = '';
    this.errorMessage = '';
  }
}
