package pl.rolex.bricksbox.controller.user.set;

import lombok.Builder;
import lombok.Data;
import pl.rolex.bricksbox.model.Piece;

import java.util.List;

@Data
@Builder
public class UserSetResponse {
    private String modelId;
    private String name;
    private String theme;
    private String imgLink;
    private String year;
    private List<Piece> pieces;
    private List<String> instructions;
    private int piecesNum;
    private String piecesUrl;
    private List<Piece> missingPieces;
}
