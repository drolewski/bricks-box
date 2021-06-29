import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-sets-filter',
  template: `
    <mat-form-field class="search">
      <input matInput
             [placeholder]="'Szukaj'"
             type="text"
             [(ngModel)]="searchValue"
             (ngModelChange)="changeSearchValue($event)">
      <button *ngIf="searchValue" matSuffix mat-icon-button (click)="changeSearchValue('')">
        <mat-icon>close</mat-icon>
      </button>
    </mat-form-field>
  `,
  styleUrls: ['./sets-filter.component.scss']
})
export class SetsFilterComponent {

  @Input() searchValue: string;

  @Output() searchValueChanged: EventEmitter<string> = new EventEmitter<string>();

  changeSearchValue($event: string): void {
    this.searchValueChanged.emit($event)
  }
}
