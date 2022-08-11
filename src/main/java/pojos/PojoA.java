package pojos;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PojoA {
    String title = "Test Title";
    String body = "Test Body";
    String userId = "786";
}
