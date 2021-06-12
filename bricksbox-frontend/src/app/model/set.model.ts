import {Piece} from './piece.model';

export interface SetModel {
  id: string;
  modelId: string
  name: string
  theme: string;
  img_link: string;
  year: string;
  pieces: Piece[];
  instructions: string[];
  pieces_num: number;
  pieces_url: string;
}
