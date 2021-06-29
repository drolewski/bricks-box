import {Component, EventEmitter, Input, Output} from '@angular/core';
import {SetModel} from '../../model/set.model';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-table',
  template: `
    <div class="table">
      <mat-checkbox [checked]="withShoppingList" (change)="withShoppingList = !withShoppingList">Dodaj listę zakupów</mat-checkbox>
      <app-sets-filter [searchValue]="searchValue"
                       (searchValueChanged)="changeSearchValue($event)"></app-sets-filter>
      <div *ngFor="let set of sets">
        <app-set-tile [set]="set" (click)="showSet(set.modelId)"></app-set-tile>
      </div>
    </div>
  `,
  styleUrls: ['./list.component.scss']
})
export class ListComponent {

  @Input() sets: SetModel[];
  @Input() searchValue: string;

  @Output() searchValueChanged: EventEmitter<string> = new EventEmitter<string>();

  withShoppingList: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router) {
  }

  changeSearchValue($event: string): void {
    this.searchValueChanged.emit($event);
  }

  showSet(modelId: string): void {
    this.router.navigate([modelId, this.withShoppingList], {relativeTo: this.route});
  }
}
