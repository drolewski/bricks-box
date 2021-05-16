package pl.rolex.bricksbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Piece {
    private String element;
    private String design;
    private String colour;
    private int count;
    private String img_url;
}
