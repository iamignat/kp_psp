package ignat.malko.model;

import ignat.malko.model.enums.Role;
import lombok.*;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User implements Serializable {
    private Long id;
    private String login;
    private String password;
    private Role role;
    private PersonData personData;
}
