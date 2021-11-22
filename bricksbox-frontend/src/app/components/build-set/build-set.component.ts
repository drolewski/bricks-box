import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BuildSetModel} from '../../model/build-set.model';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-build-set',
  template: `
    <button mat-button (click)="onClick()">Usuń</button>
  `,
  styleUrls: ['./build-set.component.scss']
})
export class BuildSetComponent {

  @Input() set: BuildSetModel;
  @Output() deleteEvent: EventEmitter<void> = new EventEmitter<void>();

  constructor(private userService: UserService) {
  }

  onClick(): void {
    this.userService.deleteBuildPlanStatus(this.set.modelId);
    this.deleteEvent.emit();
  }
}
