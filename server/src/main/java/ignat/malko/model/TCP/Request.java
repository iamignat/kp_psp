package ignat.malko.model.TCP;

import ignat.malko.model.enums.RequestType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Request {
    private RequestType requestType;
    private String requestMessage;
}

