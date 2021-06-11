package pl.rolex.bricksbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSet {
    private String modelId;
    private String name;
    private String theme;
    private String imgLink;
    private String year;
    private int numberOfPieces;
    private boolean areInstructions;
    private boolean isBuilt;
}
