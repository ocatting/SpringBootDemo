package com.design.factory.spring;

import java.util.ArrayList;
import java.util.List;

/**
 * 将配置文件解析成 beanDefinition
 */
public class BeanDefinition {

    private String id;
    private String className;
    private Scope scope = Scope.SINGLETON;
    private boolean lazyInit = false;
    private List<ConstructorArg> constructorArgs = new ArrayList<>();

    public static enum Scope { SINGLETON, PROTOTYPE }

    public String getId(){return id;}

    public String getClassName() {return className;}

    public boolean isSingleton() { return scope.equals(Scope.SINGLETON); }

    public boolean isLazyInit(){ return lazyInit;}

    public Scope getScope() {
        return scope;
    }

    public List<ConstructorArg> getConstructorArgs() {
        return constructorArgs;
    }

    public static class ConstructorArg {

        private boolean isRef;
        private Class<?> type;
        private Object arg;

        public boolean isRef() {
            return isRef;
        }

        public void setRef(boolean ref) {
            isRef = ref;
        }

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }

        public Object getArg() {
            return arg;
        }

        public void setArg(Object arg) {
            this.arg = arg;
        }
// 省略必要的getter/setter/constructors
    }
}
