import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {BuildSetModel} from '../../../model/build-set.model';

@Component({
  selector: 'app-build-plans-table',
  template: `
    <table mat-table matSort [dataSource]="setsTableSource">

      <ng-container matColumnDef="modelId">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Numer modelu</th>
        <td mat-cell *matCellDef="let row"> {{row.modelId}} </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Nazwa modelu</th>
        <td mat-cell *matCellDef="let row"> {{row.name}} </td>
      </ng-container>

      <ng-container matColumnDef="year">
        <th mat-header-cell *matHeaderCellDef>Rok produkcji</th>
        <td mat-cell *matCellDef="let row"> {{row.year}} </td>
      </ng-container>

      <ng-container matColumnDef="numberOfPieces">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Liczba elementów</th>
        <td mat-cell *matCellDef="let row"> {{row?.numberOfPieces}} </td>
      </ng-container>

      <ng-container matColumnDef="img">
        <th mat-header-cell *matHeaderCellDef>Zdjęcie</th>
        <td mat-cell *matCellDef="let row"><img src="{{ row.imgLink }}" alt="{{ row.modelId }}"></td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef>Akcje</th>
        <td mat-cell *matCellDef="let row">
          <app-build-set [set]="row" (deleteEvent)="onDelete(row)"></app-build-set>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="columnsDef"></tr>
      <tr mat-row *matRowDef="let row; columns: columnsDef;"></tr>

      <tr class="mat-row" *matNoDataRow>
        <td class="mat-cell" colspan="4">No matching data</td>
      </tr>
    </table>

    <mat-paginator [pageSizeOptions]="pageSizeOptions" showFirstLastButtons></mat-paginator>
  `,
  styleUrls: ['./build-plans-table.component.scss']
})
export class BuildPlansTableComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  @Input() sets: BuildSetModel[] = [];
  @Output() deleteEvent: EventEmitter<number> = new EventEmitter<number>();

  readonly columnsDef: string[] = ['modelId', 'name', 'year', 'numberOfPieces', 'img', 'actions'];
  readonly pageSizeOptions: number[] = [5, 10, 25];

  setsTableSource: MatTableDataSource<BuildSetModel>;

  ngOnInit(): void {
    this.setsTableSource = new MatTableDataSource(this.sets);
  }

  ngAfterViewInit(): void {
    this.setsTableSource.paginator = this.paginator;
    this.setsTableSource.sort = this.sort;
  }

  onDelete(row: BuildSetModel) {
    this.deleteEvent.emit(this.sets.indexOf(row));
  }
}
