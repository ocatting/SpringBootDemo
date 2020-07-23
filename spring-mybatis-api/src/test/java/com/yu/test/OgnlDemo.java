package com.yu.test;

import ognl.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 表达式语言
 * @Author Yan XinYu
 **/
public class OgnlDemo {

    public static class OgnlMemberAccess implements MemberAccess {

        @Override
        public Object setup(Map context, Object target, Member member, String propertyName) {
            Object result = null;
            if (isAccessible(context, target, member, propertyName)) {
                AccessibleObject accessible = (AccessibleObject) member;
                if (!accessible.isAccessible()) {
                    result = Boolean.FALSE;
                    accessible.setAccessible(true);
                }
            }
            return result;
        }

        @Override
        public void restore(Map context, Object target, Member member, String propertyName, Object state) {
            if (state != null) {
                ((AccessibleObject) member).setAccessible((Boolean) state);
            }
        }

        @Override
        public boolean isAccessible(Map context, Object target, Member member, String propertyName) {
            return true;
        }
    }

    public static class OgnlClassResolver extends DefaultClassResolver {
        @Override
        protected Class toClassForName(String className) throws ClassNotFoundException {
            return super.toClassForName(className);
        }
    }

    public static void main(String[] args) throws OgnlException {

        OgnlMemberAccess MEMBER_ACCESS = new OgnlMemberAccess();
        OgnlClassResolver CLASS_RESOLVER = new OgnlClassResolver();

        Map<String,String> root = new HashMap<>();
        root.put("name","a");
        root.put("person","人");

        String expression = "name != null and name != ''";

        Object o = Ognl.parseExpression(expression);
        // (name != null) && (name != "")
        System.out.println(o);

        Map context = Ognl.createDefaultContext(root, MEMBER_ACCESS, CLASS_RESOLVER, null);
        Object value = Ognl.getValue(o, context, root);
        System.out.println(value);
    }

}
