package cn.ldy.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class BeanDefine {
    private String id;
    private Class type;
    private String scope;
    private Map<String, Property> properties = new HashMap<>();
    public void addProperty(Property property){
        properties.put(property.getName(),property);
    }
    public List<Property> getProperties(){
        return new ArrayList<>(properties.values());
    }
}
