import {Piece} from './piece.model';

export interface UserSet {
  modelId: string;
  name: string;
  theme: string;
  imgLink: string;
  year: string;
  pieces: Piece[];
  instructions: string[];
  piecesNum: number;
  piecesUrl: string;
  missingPieces: Piece[];
}
