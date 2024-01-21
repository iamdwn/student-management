package dozun.mini.model;

import dozun.mini.constants.ResponseStatus;
import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {
    private ResponseStatus status;
    private String message;
    private Object data;
}