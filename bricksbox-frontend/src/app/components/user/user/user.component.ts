import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {UserService} from '../../../service/user.service';
import {Piece} from '../../../model/piece.model';
import {SetModel} from '../../../model/set.model';
import {BuildSetModel} from '../../../model/build-set.model';

@Component({
  selector: 'app-user',
  template: `
    <div *ngIf="ownedPieces && userSets && buildingPlans">
      <mat-tab-group>
        <mat-tab label="List klocków ({{ownedPiecesLength}})">
          <app-pieces-table [pieces]="ownedPieces"></app-pieces-table>
        </mat-tab>
        <!--      <mat-tab label="Plan zakupu ({{this.shoppingPieces.length}})">-->
        <!--        <app-pieces-table [pieces]="shoppingPieces"></app-pieces-table>-->
        <!--      </mat-tab>-->
        <mat-tab label="Posiadane zestawy ({{userSetsLength}})">
          <app-set-table [sets]="userSets"></app-set-table>
        </mat-tab>
        <mat-tab label="Plan budowania ({{buildingPlansLength}})">
          <app-build-plans-table [sets]="buildingPlans" (deleteEvent)="onDelete($event)"></app-build-plans-table>
        </mat-tab>
      </mat-tab-group>
    </div>
  `,
  styleUrls: ['./user.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserComponent implements OnInit {

  ownedPieces: Piece[];
  shoppingPieces: Piece[];
  userSets: SetModel[];
  buildingPlans: BuildSetModel[];

  get ownedPiecesLength(): number {
    return this.ownedPieces ? this.ownedPieces.length : 0;
  }

  get shoppingPiecesLength(): number {
    return this.shoppingPieces ? this.shoppingPieces.length : 0;
  }

  get userSetsLength(): number {
    return this.userSets ? this.userSets.length : 0;
  }

  get buildingPlansLength(): number {
    return this.buildingPlans ? this.buildingPlans.length : 0;
  }

  constructor(private userService: UserService, private changeDetector: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.userService.getUserPieces().subscribe(pieces => {
      this.ownedPieces = pieces;
      this.changeDetector.detectChanges();
    });

    this.userService.getShoppingPieces().subscribe(pieces => {
      this.shoppingPieces = pieces;
      this.changeDetector.detectChanges();
    });

    this.userService.getUserSets().subscribe(sets => {
      this.userSets = sets;
      this.changeDetector.detectChanges();
    });

    this.userService.getUserBuildingPlans().subscribe(sets => {
      this.buildingPlans = sets;
      this.changeDetector.detectChanges();
    });
  }

  onDelete(index: any): void {
    this.buildingPlans = this.buildingPlans.splice(index, 1);
  }
}
