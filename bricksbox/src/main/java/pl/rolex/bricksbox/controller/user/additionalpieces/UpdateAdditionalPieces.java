package pl.rolex.bricksbox.controller.user.additionalpieces;

import lombok.Data;

@Data
public class UpdateAdditionalPieces {
    private String token;
    private String element;
    private String design;
    private String colour;
    private int count;
    private String imgUrl;
}
