import {Component, Input, OnInit} from '@angular/core';
import {SetModel} from '../../../model/set.model';

@Component({
  selector: 'app-set-tile',
  template: `
    <div class="tile">
      <div class="header">
        <div class="model-name">{{ set.name }}</div>
        <div class="model-theme">{{ set.theme }}</div>
      </div>
      <div class="model-image">
        <img src="{{ set.img_link }}" alt="{{ set.name }}">
      </div>
      <div class="content">
        <div><span>Identyfikator modelu: </span>{{ set.modelId | modelId}}</div>
        <div><span>Rok produkcji: </span>{{ set.year }}</div>
        <div><span>Liczba elementów: </span>{{ set.pieces_num }}</div>
        <div><span>Liczba instrukcji: </span>{{ set.instructions.length }}</div>
      </div>
    </div>
  `,
  styleUrls: ['./set-tile.component.scss']
})
export class SetTileComponent implements OnInit {

  @Input() set: SetModel;

  constructor() {
  }

  ngOnInit(): void {
  }

}
