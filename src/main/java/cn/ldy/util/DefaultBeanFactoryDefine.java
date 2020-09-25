package cn.ldy.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class DefaultBeanFactoryDefine implements BeanFactoryDefine,BeanFactory{
   private Map<String,BeanDefine> names = new HashMap<>();
    private Map<Class, List<BeanDefine>> types = new HashMap<>();
    private Map<String, Object> beans = new HashMap<>();

    @Override
    public Object getBean(String name) throws ClassNotFoundException {
        BeanDefine define = names.get(name);
        if (define==null) {
            throw new ClassNotFoundException(define.getId());
        }
        return getBean(name,define.getType());
    }

    @Override
    public <T> T getBean(Class<T> type) throws ClassNotFoundException {
        List<BeanDefine> list = types.get(type);
        if (list.size()>1) {
            throw new RuntimeException("有多个定义："+list);
        } else if (list.isEmpty()) {
            throw new ClassNotFoundException(type.getName());
        }
        return getBean(list.get(0).getId(),type);
    }

    @Override
    public <T> T getBean(String name, Class<T> type) {
        Objects.requireNonNull(name,"名称不能为空");
        Objects.requireNonNull(type,"类型不能为空");
        BeanDefine beanDefine = names.get(name);
        //如果当前的bean已经被实例化，则直接返回
        if ("singleton".equals(beanDefine.getScope())&&beans.containsKey(name)) {
            return (T) beans.get(name);
        }
        try {
            Object obj = newInstance(beanDefine);
            if ("singleton".equals(beanDefine.getScope())) {
                beans.put(name,obj);
            }
            return (T)obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object newInstance(BeanDefine beanDefine) throws IllegalAccessException, InstantiationException, IntrospectionException, ClassNotFoundException, InvocationTargetException {
        Class type = beanDefine.getType();
        Object o = type.newInstance();
        List<Property> properties = beanDefine.getProperties();
        if (!properties.isEmpty()) {
            for (Property property : properties) {
                //内省机制
                PropertyDescriptor pd = new PropertyDescriptor(property.getName(), type);
                //获得对应属性的set方法对象
                Method method = pd.getWriteMethod();
                //执行set方法将依赖设置到当前对象当中
                method.invoke(o, getBean(property.getRef()));

            }
        }
        return null;
    }

    @Override
    public void registerBeanDefine(BeanDefine beanDefine) {
        names.put(beanDefine.getId(),beanDefine);
        if (!types.containsKey(beanDefine.getType())){
            types.put(beanDefine.getType(),new ArrayList<>());
        }
        types.get(beanDefine.getType().add(beanDefine));
    }
}
