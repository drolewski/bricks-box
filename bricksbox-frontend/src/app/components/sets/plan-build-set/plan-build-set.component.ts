import {Component, Input} from '@angular/core';
import {UserService} from '../../../service/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-plan-build-set',
  template: `
    <mat-icon (click)="addItemForUser($event)" matTooltip="Dodaj do planowania budowania">queue</mat-icon>
  `,
  styleUrls: ['./plan-build-set.component.scss']
})
export class PlanBuildSetComponent {

  @Input() setId: string;

  constructor(private userService: UserService,
              private _snackBar: MatSnackBar) {
  }

  addItemForUser(event: any): void {
    this.userService.addBuildPlan(this.setId).subscribe(() =>
      this._snackBar.open("Zaplanowano budowę", "Close", {duration: 5000})
    );
    event.stopPropagation();
  }

}
