package cn.ldy.view;

import cn.ldy.util.ClassPathXMLApplication;
import org.dom4j.DocumentException;

public class ApplicationView {
    public static void main(String[] args) {
        try {
            new ClassPathXMLApplication("bean.xml");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
