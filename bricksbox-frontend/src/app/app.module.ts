import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {ModelIdPipe} from './pipe/model-id.pipe';
import {FormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {SetTileComponent} from './components/sets/set-tile/set-tile.component';
import {SetsTableComponent} from './components/sets/sets-table/sets-table.component';
import {SetsFilterComponent} from './components/sets/sets-filter/sets-filter.component';
import {AppRoutingModule} from './app-routing.module';
import {ListComponent} from './components/list/list.component';
import {SetComponent} from './components/set/set/set.component';
import {MatTableModule} from '@angular/material/table';
import {MatSortModule} from '@angular/material/sort';
import {MatPaginatorModule} from '@angular/material/paginator';
import {PiecesTableComponent} from './components/table/pieces-table/pieces-table.component';
import {MatTabsModule} from '@angular/material/tabs';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { ElementCounterComponent } from './components/element-counter/element-counter.component';
import { OwnSetComponent } from './components/sets/own-set/own-set.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { NavbarComponent } from './components/navbar/navbar.component';
import { UserComponent } from './components/user/user/user.component';
import {SetTableComponent} from './components/table/set-table/set-table.component';
import {BuildPlansTableComponent} from './components/table/build-plans-table/build-plans-table.component';
import {PlanBuildSetComponent} from './components/sets/plan-build-set/plan-build-set.component';
import { BuildSetComponent } from './components/build-set/build-set.component';
import {MatTooltipModule} from '@angular/material/tooltip';
import { LoginComponent } from './components/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    ModelIdPipe,
    SetTileComponent,
    SetsTableComponent,
    SetsFilterComponent,
    ListComponent,
    SetComponent,
    PiecesTableComponent,
    ElementCounterComponent,
    OwnSetComponent,
    NavbarComponent,
    UserComponent,
    SetTableComponent,
    BuildPlansTableComponent,
    PlanBuildSetComponent,
    BuildSetComponent,
    LoginComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatTabsModule,
    MatCheckboxModule,
    MatSnackBarModule,
    MatTooltipModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
