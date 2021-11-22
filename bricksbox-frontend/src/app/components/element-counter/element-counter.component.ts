import {Component, Input, OnInit} from '@angular/core';
import {Piece} from '../../model/piece.model';

@Component({
  selector: 'app-element-counter',
  template: `
    <div class="counter-row">
      <mat-icon (click)="onAdd()">add_circle</mat-icon>
      <input matInput [(ngModel)]="count"/>
      <mat-icon (click)="onRemove()">remove_circle</mat-icon>
    </div>
  `,
  styleUrls: ['./element-counter.component.scss']
})
export class ElementCounterComponent implements OnInit {

  @Input() piece: Piece;
  count: number = 0;

  ngOnInit(): void {
    this.count = this.piece.count;
  }

  onAdd(): void {
    this.count += 1;
  }

  onRemove(): void {
    if (this.count > 0) {
      this.count -= 1;
    }
  }
}
