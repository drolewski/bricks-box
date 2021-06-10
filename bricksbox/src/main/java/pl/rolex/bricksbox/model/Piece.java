package pl.rolex.bricksbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Piece {
    private String element;
    private String design;
    private String colour;
    private int count;
    private String img_url;
}
