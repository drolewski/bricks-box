import {Component, Input} from '@angular/core';
import {UserService} from '../../../service/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-own-set',
  template: `
    <mat-icon (click)="addItemForUser($event)" matTooltip="Dodaj do posiadanych modeli">add_box</mat-icon>
  `,
  styleUrls: ['./own-set.component.scss']
})
export class OwnSetComponent {

  @Input() setId: string;

  constructor(private userService: UserService,
              private _snackBar: MatSnackBar) {
  }

  addItemForUser(event: any): void {
    this.userService.addUserSet(this.setId).subscribe(message =>
      this._snackBar.open(message.message, "Close", {duration: 5000}));
    event.stopPropagation();
  }

}
