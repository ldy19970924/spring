package cn.ldy.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

public class ClassPathXMLApplication implements ApplicationContext{
    private BeanFactory beanFactory;

    public ClassPathXMLApplication(String location) throws DocumentException, ClassNotFoundException {
        BeanFactoryDefine beanFactoryDefine = new DefaultBeanFactoryDefine();
        beanFactory= (BeanFactory) beanFactoryDefine;
        SAXReader reader = new SAXReader();
        Document document = reader.read(getClass().getResourceAsStream("/" + location));
        List<Element> beanNodes = document.selectNodes("//bean");
        if (beanNodes!=null) {
            BeanDefine define = null;
            Property property = null;
            for (Element beanNode:beanNodes) {
                define = new BeanDefine();
                define.setId(beanNode.attributeValue("id"));
                define.setType(Class.forName(beanNode.attributeValue("class")));
                String scope = beanNode.attributeValue("scope");
                if ("prototype".equals(scope)) {
                    define.setScope("prototype");
                }else {
                    define.setScope("singleton");
                }
                List<Element> propertyNodes = beanNode.elements("property");
                if (propertyNodes!=null) {
                    for (Element prepertyNode:propertyNodes) {
                         property = new Property();
                         property.setName(prepertyNode.attributeValue("name"));
                        property.setRef(prepertyNode.attributeValue("ref"));
                        define.addProperty(property);
                    }
                }
                beanFactoryDefine.registerBeanDefine(define);
            }
        }
    }

    @Override
    public Object getBean(String name) throws ClassNotFoundException {
        return beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> type) throws ClassNotFoundException {
        return beanFactory.getBean(type);
    }

    @Override
    public <T> T getBean(String name, Class<T> type) {
        return beanFactory.getBean(name,type);
    }
}
