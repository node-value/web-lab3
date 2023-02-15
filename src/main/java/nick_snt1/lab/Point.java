package nick_snt1.lab;

import java.io.Serializable;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class Point implements Serializable {
    private Double  x, y;
    private Integer r;
    private String  hit;
}
