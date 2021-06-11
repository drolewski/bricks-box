package pl.rolex.bricksbox.controller.user.additionalpieces;

import lombok.Data;

@Data
public class DeletePiece {
    private String token;
    private String element;
    private String design;
    private String colour;
}
