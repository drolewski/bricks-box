package pl.rolex.bricksbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Set {
    @Id
    private String id;
    @Field("model_id")
    private String modelId;
    private String name;
    private String theme;
    private String img_link;
    private String year;
    private List<Piece> pieces;
    private List<String> instructions;
    private int pieces_num;
    private String pieces_url;

}
