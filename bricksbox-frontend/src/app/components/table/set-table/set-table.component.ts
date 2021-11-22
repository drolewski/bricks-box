import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {SetModel} from '../../../model/set.model';

@Component({
  selector: 'app-set-table',
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

      <ng-container matColumnDef="pieces_count">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Liczba elementów</th>
        <td mat-cell *matCellDef="let row"> {{row?.pieces_num}} </td>
      </ng-container>

      <ng-container matColumnDef="img">
        <th mat-header-cell *matHeaderCellDef>Zdjęcie</th>
        <td mat-cell *matCellDef="let row"><img src="{{ row.img_link }}" alt="{{ row.modelId }}"></td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="columnsDef"></tr>
      <tr mat-row *matRowDef="let row; columns: columnsDef;"></tr>

      <tr class="mat-row" *matNoDataRow>
        <td class="mat-cell" colspan="4">No matching data</td>
      </tr>
    </table>

    <mat-paginator [pageSizeOptions]="pageSizeOptions" showFirstLastButtons></mat-paginator>
  `,
  styleUrls: ['./set-table.component.scss']
})
export class SetTableComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  @Input() sets: SetModel[] = [];

  readonly columnsDef: string[] = ['modelId', 'name', 'year', 'pieces_count', 'img'];
  readonly pageSizeOptions: number[] = [5, 10, 25];

  setsTableSource: MatTableDataSource<SetModel>;

  ngOnInit(): void {
    this.setsTableSource = new MatTableDataSource(this.sets);
  }

  ngAfterViewInit(): void {
    this.setsTableSource.paginator = this.paginator;
    this.setsTableSource.sort = this.sort;
  }

}
