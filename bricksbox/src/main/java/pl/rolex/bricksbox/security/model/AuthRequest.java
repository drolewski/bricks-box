package pl.rolex.bricksbox.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
