import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SetsTableComponent} from './components/sets/sets-table/sets-table.component';
import {SetComponent} from './components/set/set/set.component';

const routes: Routes = [
  {path: 'sets', component: SetsTableComponent},
  {path: 'sets/:modelId', component: SetComponent},
  {path: '**', redirectTo: 'sets', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
