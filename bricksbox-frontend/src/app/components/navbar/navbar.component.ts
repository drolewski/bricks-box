import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  template: `
    <img src="../../../favicon.ico" alt="" (click)="onLogoClick()"/>
    <button mat-button (click)="onUserClick()">Użytkownik</button>
    <button mat-button (click)="onLogin()">Zaloguj</button>
  `,
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  constructor(private router: Router) { }


  onLogoClick(): void {
    this.router.navigate(["../"]);
  }

  onUserClick(): void {
    this.router.navigate(["../user"]);
  }

  onLogin() {
    this.router.navigate(["../login"]);
  }
}
