package pl.rolex.bricksbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignPiece {
    private String design;
    private int count;
    private String imgUrl;
}
