import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Piece} from '../../../model/piece.model';

@Component({
  selector: 'app-pieces-table',
  template: `
    <table mat-table matSort [dataSource]="piecesTableSource">

      <ng-container matColumnDef="element">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Numer elementu</th>
        <td mat-cell *matCellDef="let row"> {{row.element}} </td>
      </ng-container>

      <ng-container matColumnDef="design">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Numer wzoru</th>
        <td mat-cell *matCellDef="let row"> {{row.design}} </td>
      </ng-container>

      <ng-container matColumnDef="colour">
        <th mat-header-cell *matHeaderCellDef>Kolor</th>
        <td mat-cell *matCellDef="let row"> {{row.colour}} </td>
      </ng-container>

      <ng-container matColumnDef="count">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Ilość</th>
        <td mat-cell *matCellDef="let row"> {{row.count}} </td>
      </ng-container>

      <ng-container matColumnDef="img">
        <th mat-header-cell *matHeaderCellDef>Zdjęcie</th>
        <td mat-cell *matCellDef="let row"><img src="{{ row.img_url || row.imgUrl }}" alt="{{ row.element }}"></td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="columnsDef"></tr>
      <tr mat-row *matRowDef="let row; columns: columnsDef;"></tr>

      <tr class="mat-row" *matNoDataRow>
        <td class="mat-cell" colspan="4">No matching data</td>
      </tr>
    </table>

    <mat-paginator [pageSizeOptions]="pageSizeOptions" showFirstLastButtons></mat-paginator>
  `,
  styleUrls: ['./pieces-table.component.scss']
})
export class PiecesTableComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  @Input() pieces: Piece[] = [];

  readonly columnsDef: string[] = ['element', 'design', 'colour', 'count', 'img'];
  readonly pageSizeOptions: number[] = [5, 10, 25];

  piecesTableSource: MatTableDataSource<Piece>;

  ngOnInit(): void {
    this.piecesTableSource = new MatTableDataSource(this.pieces);
  }

  ngAfterViewInit(): void {
    this.piecesTableSource.paginator = this.paginator;
    this.piecesTableSource.sort = this.sort;
  }

}
