package nick_snt1.lab;

import java.io.Serializable;
import lombok.*;

@Getter @Setter @ToString @EqualsAndHashCode
public class Point implements Serializable {
    private String  id;
    private Double  x, y;
    private Integer r;
    private String  hit;
}
