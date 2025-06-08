import { Component } from '@angular/core';
import { MeasurementsComponent } from './components/measurements/measurements.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [MeasurementsComponent],
  template: ` <app-measurements></app-measurements> `,
})
export class AppComponent {
  title = 'measurements-app';
}
