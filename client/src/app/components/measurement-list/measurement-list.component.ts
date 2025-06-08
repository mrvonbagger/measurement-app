import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Measurement } from '../../models/measurement.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-measurement-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './measurement-list.component.html',
  styleUrls: ['./measurement-list.component.html'],
})
export class MeasurementListComponent {
  @Input() measurements: Measurement[] = [];
  @Output() editMeasurement = new EventEmitter<Measurement>();
  @Output() deleteMeasurement = new EventEmitter<string>();
}
