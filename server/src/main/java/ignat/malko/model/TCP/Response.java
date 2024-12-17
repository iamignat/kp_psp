package ignat.malko.model.TCP;

import ignat.malko.model.enums.ResponseType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Response {
    private ResponseType responseType;
    private String responseMessage;
    private String responseData;
}

