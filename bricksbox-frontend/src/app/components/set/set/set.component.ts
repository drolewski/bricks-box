import {Component, OnInit, ViewChild} from '@angular/core';
import {GetSetService} from '../../../service/get-set.service';
import {UserSet} from '../../../model/user-set.model';
import {ActivatedRoute, Router} from '@angular/router';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';

@Component({
  selector: 'app-set',
  template: `
    <div *ngIf="set">
      <div class="piece-header">
        <h1>{{ set.name }} ({{ set.year }})</h1>
        <h2>{{ set.theme }}</h2>
      </div>
      <div class="piece-body">
        <div class="model-image">
          <img src="{{ set.imgLink }}" alt="{{ set.name }}">
        </div>
        <div class="model-data">
          <div *ngFor="let instruction of set.instructions; let i = index"><a href="{{instruction}}" target="_blank">Instrukcja-{{ i + 1 }}</a>
          </div>
        </div>
      </div>
      <mat-tab-group>
        <mat-tab label="List klocków ({{set.piecesNum}})"><app-pieces-table [pieces]="set.pieces"></app-pieces-table></mat-tab>
        <mat-tab label="Lista brakujących klocków ({{set.missingPieces.length}})"><app-pieces-table [pieces]="set.missingPieces"></app-pieces-table></mat-tab>

      </mat-tab-group>
    </div>
  `,
  styleUrls: ['./set.component.scss']
})
export class SetComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  set: UserSet;

  constructor(private getSetService: GetSetService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    const modelId = this.route.snapshot.paramMap.get('modelId');
    if (modelId) {
      this.getSetService.getSet(modelId, false).subscribe(s => this.set = s);
      return;
    }
    this.router.navigate(['/sets']);
  }

}
