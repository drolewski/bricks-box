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

@NgModule({
  declarations: [
    AppComponent,
    ModelIdPipe,
    SetTileComponent,
    SetsTableComponent,
    SetsFilterComponent,
    ListComponent,
    SetComponent,
    PiecesTableComponent
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
    MatTabsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
