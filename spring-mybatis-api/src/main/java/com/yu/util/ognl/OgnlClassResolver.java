package com.yu.util.ognl;

import ognl.DefaultClassResolver;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class OgnlClassResolver extends DefaultClassResolver {

    @Override
    protected Class toClassForName(String className) throws ClassNotFoundException {
        return super.toClassForName(className);
    }
}
