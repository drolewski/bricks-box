package pl.rolex.bricksbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Set {
    @Id
    private String id;
    private String model_id;
    private String name;
    private String theme;
    private String img_link;
    private String year;
    private List<Piece> pieces;
    private List<String> instructions;
    private int pieces_num;
    private String pieces_url;

}
