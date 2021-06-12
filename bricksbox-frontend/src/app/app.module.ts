import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {ModelIdPipe} from './pipe/model-id.pipe';
import {FormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import { SetTileComponent } from './sets/set-tile/set-tile.component';
import { SetsTableComponent } from './sets/sets-table/sets-table.component';
import { SetsFilterComponent } from './sets/sets-filter/sets-filter.component';

@NgModule({
  declarations: [
    AppComponent,
    ModelIdPipe,
    SetTileComponent,
    SetsTableComponent,
    SetsFilterComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
