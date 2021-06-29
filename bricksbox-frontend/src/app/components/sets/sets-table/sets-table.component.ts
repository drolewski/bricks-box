import {Component, OnDestroy, OnInit} from '@angular/core';
import {SetModel} from '../../../model/set.model';
import {Subscription} from 'rxjs';
import {GetSetsService} from '../../../service/get-sets.service';

@Component({
  selector: 'app-sets-table',
  template: `
    <app-table [sets]="getSets()"
               [searchValue]="searchValue"
               (searchValueChanged)="changeSearchValue($event)"></app-table>
  `,
  styleUrls: ['./sets-table.component.css']
})
export class SetsTableComponent implements OnInit, OnDestroy {

  searchValue: string = '';

  private sets: SetModel[] = [];
  private subscriptions: Subscription[] = [];

  constructor(private getSetsService: GetSetsService) {
  }

  ngOnInit(): void {
    this.subscriptions.push(this.getSetsService.getSets().subscribe(sets => this.sets = sets));
  }

  changeSearchValue($event: string): void {
    this.searchValue = $event;
  }

  getSets(): SetModel[] {
    return this.sets.filter(set => this.filterCondition(set))
  }

  private filterCondition(set: SetModel): boolean {
    return set.name.toLocaleLowerCase().includes(this.searchValue.toLocaleLowerCase()) || set.modelId.includes(this.searchValue);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
