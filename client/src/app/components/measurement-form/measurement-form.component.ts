import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Measurement } from '../../models/measurement.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-measurement-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './measurement-form.component.html',
  styleUrls: ['./measurement-form.component.css'],
})
export class MeasurementFormComponent implements OnInit {
  @Input() measurement: Measurement | null = null;
  @Input() isSubmitting = false;
  @Output() submitForm = new EventEmitter<any>();
  @Output() cancelForm = new EventEmitter<void>();

  measurementForm: FormGroup;
  isEditMode = false;

  constructor(private fb: FormBuilder) {
    this.measurementForm = this.fb.group({
      patientId: ['', [Validators.required]],
      result: [
        '',
        [Validators.required, Validators.min(50.0), Validators.max(100.0)],
      ],
    });
  }

  ngOnInit() {
    if (this.measurement) {
      this.isEditMode = true;
      this.measurementForm.patchValue({
        patientId: this.measurement.patientId,
        result: this.measurement.result,
      });
    }
  }

  onSubmit() {
    if (this.measurementForm.valid) {
      this.submitForm.emit(this.measurementForm.value);
    }
  }

  onCancel() {
    this.measurementForm.reset();
    this.cancelForm.emit();
  }
}
