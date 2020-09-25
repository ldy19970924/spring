package cn.ldy.util;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Property {
    private String name;
    private String ref;
}
