import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SetsTableComponent} from './components/sets/sets-table/sets-table.component';
import {SetComponent} from './components/set/set/set.component';
import {UserComponent} from './components/user/user/user.component';
import {LoginComponent} from './components/login/login.component';
import {LoginAuthentication} from './components/login/login-authentication.service';

const routes: Routes = [
  {path: 'sets', component: SetsTableComponent},
  {path: 'sets/:modelId/:withShoppingList', component: SetComponent, canActivate: [LoginAuthentication]},
  {path: 'user', component: UserComponent, canActivate: [LoginAuthentication]},
  {path: 'login', component: LoginComponent},
  {path: '**', redirectTo: 'sets', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
