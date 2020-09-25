package cn.ldy.util;

/**
 * 获取bean
 * @author 李东盈
 */
public interface BeanFactory {
    Object getBean(String name) throws ClassNotFoundException;
    <T> T getBean(Class<T> type) throws ClassNotFoundException;
    <T> T getBean(String name,Class<T> type);


}
