package com.yu.web.servlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.util.Assert;
import java.util.Collection;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class NoCrudServletRegistrationBean extends ServletRegistrationBean<NoCrudHttpServlet> {

    private final String path;

    public NoCrudServletRegistrationBean(NoCrudHttpServlet servlet, String path) {
        super(servlet);
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        super.addUrlMappings(getServletUrlMapping());
    }

    public String getPath() {
        return this.path;
    }

    private String getRelativePath(String path) {
        String prefix = getPrefix();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return prefix + path;
    }

    /**
     * Return a cleaned up version of the path that can be used as a prefix for URLs. The
     * resulting path will have path will not have a trailing slash.
     * @return the prefix
     * @see #getRelativePath(String)
     */
    private String getPrefix() {
        String result = getPath();
        int index = result.indexOf('*');
        if (index != -1) {
            result = result.substring(0, index);
        }
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Return a URL mapping pattern that can be used with a
     * {@link ServletRegistrationBean} to map the dispatcher servlet.
     * @return the path as a servlet URL mapping
     */
    private String getServletUrlMapping() {
        if (getPath().equals("") || getPath().equals("/")) {
            return "/";
        }
        if (getPath().contains("*")) {
            return getPath();
        }
        if (getPath().endsWith("/")) {
            return getPath() + "*";
        }
        return getPath() + "/*";
    }

    @Override
    public void setUrlMappings(Collection<String> urlMappings) {
        throw new UnsupportedOperationException("URL Mapping cannot be changed on a DispatcherServlet registration");
    }

    @Override
    public void addUrlMappings(String... urlMappings) {
        throw new UnsupportedOperationException("URL Mapping cannot be changed on a DispatcherServlet registration");
    }
}
