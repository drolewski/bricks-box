import {Component} from '@angular/core';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  template: `
    <div class="login-div">
      <mat-form-field appearance="fill">
        <mat-label>Login</mat-label>
        <input matInput placeholder="Login" [(ngModel)]="login">
      </mat-form-field>
      <mat-form-field appearance="fill">
        <mat-label>Hasło</mat-label>
        <input matInput placeholder="Hasło" [(ngModel)]="password" type="password">
      </mat-form-field>

      <button mat-button (click)="onClick()">Loguj</button>
    </div>
  `,
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  password: string;
  login: string;

  constructor(private userService: UserService, private router: Router) {
  }

  onClick() {
    this.userService.loginUser(this.login, this.password);
    this.router.navigate(["./sets"]);
  }
}
