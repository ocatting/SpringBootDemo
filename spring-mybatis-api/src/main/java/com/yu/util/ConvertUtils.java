package com.yu.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description: 转换工具
 * @Author Yan XinYu
 **/
public class ConvertUtils {

    private static final Log logger = LogFactory.getLog(ConvertUtils.class);

    /**
     * bean copay 缓存
     */
    private static final ConcurrentMap<Class<?>, CachedPropertyDescriptor> strongClassCache =
            new ConcurrentHashMap<>(64);

    /**
     * bean copy 工具
     * @param beanClass
     * @return
     */
    private static CachedPropertyDescriptor forClass(Class<?> beanClass){
        CachedPropertyDescriptor results = strongClassCache.get(beanClass);
        if (results == null) {
            synchronized (CachedPropertyDescriptor.class){
                if (results == null) {
                    results = new CachedPropertyDescriptor(beanClass);
                    strongClassCache.put(beanClass,results);
                }
            }
        }
        return results;
    }

    /**
     * bean copy 工具
     */
    static class CachedPropertyDescriptor {

        private final Map<String, PropertyDescriptor> propertyDescriptorCache;

        CachedPropertyDescriptor(Class<?> clazz) {
            this.propertyDescriptorCache = new LinkedHashMap<>();
            this.put(clazz);
        }

        void put (Class<?> clazz){
            PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
            for(PropertyDescriptor property:propertyDescriptors){
                propertyDescriptorCache.put(generalProperty(property.getName()), property);
            }
        }

        PropertyDescriptor get(String name){
            return propertyDescriptorCache.get(name);
        }
    }

    /**
     * bean Property Name
     * @param name
     * @return
     */
    static String generalProperty(String name){
        return name.replaceAll("_","").toLowerCase();
    }

    static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return beanInfo.getPropertyDescriptors();
    }

    static PropertyDescriptor getPropertyDescriptor(Class<?> clazz,String name) {
        return forClass(clazz).get(name);
    }

    /**
     * bean copy 通配，去掉“_”数据，将数据都转为小写匹配属性
     * @param source
     * @param target
     * @throws BeansException
     */
    public static <T> T copyProperties(Object source, Class<T> target) throws BeansException {
        T targetInstance = null;
        try {
            targetInstance = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        copyProperties(source, targetInstance, null, (String[]) null);
        return targetInstance;
    }

    /**
     * bean copy 通配，去掉“_”数据，将数据都转为小写匹配属性
     * @param source
     * @param target
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target) throws BeansException {
        copyProperties(source, target, null, (String[]) null);
    }

    /**
     * Map 属性赋值
     * @param map
     * @param target
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static <T> T copyProperties(Map map, Class<T> target) throws BeansException {
        T targetInstance = null;
        try {
            targetInstance = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        copyProperties(map, targetInstance);
        return targetInstance;
    }

    /**
     *
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * bean copy 通配，去掉“_”数据，将数据都转为小写匹配属性
     * @param map
     * @param target
     * @throws BeansException
     */
    public static void copyProperties(Map map, Object target) throws BeansException {
        Assert.notNull(map, "map must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            String PropertyName = generalProperty(targetPd.getName());
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null ) {
                Object value = map.get(PropertyName);
                if (value != null) {
                    if (ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], value.getClass())) {
                        try {
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * 将字段名称对应的值写入到bean中
     * @param fieldName
     * @param value
     * @param target
     */
    public static void copyProperties(String fieldName ,String value,Object target) throws BeansException {
        PropertyDescriptor targetPd = getPropertyDescriptor(target.getClass(), generalProperty(fieldName));
        if(targetPd!=null){
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null ) {
                try{
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    writeMethod.invoke(target, value);
                }catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                }
            }
        }
    }

    /**
     * bean copy 统计
     * @param source
     * @param target
     * @param editable
     * @param ignoreProperties
     * @throws BeansException
     */
    private static void copyProperties(Object source, Object target, @Nullable Class<?> editable,
                                       @Nullable String... ignoreProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            String PropertyName = generalProperty(targetPd.getName());
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(PropertyName))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), PropertyName);
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * 把中文转成Unicode码
     * @param str
     * @return
     */
    public static String chinaToUnicode(String str){
        if(str == null || "".equals(str))
            return str;
        String result="";
        for (int i = 0; i < str.length(); i++){
            int chr1 = (char) str.charAt(i);
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)
                result+="\\u" + Integer.toHexString(chr1);
            }else{
                result+=str.charAt(i);
            }
        }
        return result;
    }

    /**
     * 把Unicode码 转成中文
     * @param utfString
     * @return
     */
    public static String unicodeToChinas(String utfString){
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        while(pos < utfString.length()){
            if((i=utfString.indexOf("\\u", pos)) == pos){
                if(i+5 < utfString.length()){
                    pos = i+6;
                    sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
                }
            }else{
                sb.append(utfString.substring(pos, pos + 1));
                pos ++;
            }
        }
        return sb.toString();
    }
}
