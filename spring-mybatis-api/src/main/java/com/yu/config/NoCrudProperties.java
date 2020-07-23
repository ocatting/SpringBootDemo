package com.yu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@ConfigurationProperties(prefix = "no.crud")
public class NoCrudProperties {


    private final Servlet servlet = new Servlet();





    /**
     *  HTTPServlet 配置
     */
    public static class Servlet {

        private String username;

        private String password;

        /**
         * Path of the dispatcher servlet.
         */
        private String path = "/nocrud/*";

        /**
         * Load on startup priority of the dispatcher servlet.
         */
        private int loadOnStartup = -99;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPath() {
            return this.path;
        }

        public void setPath(String path){
            this.path = path;
        }

        public int getLoadOnStartup() {
            return this.loadOnStartup;
        }

        public void setLoadOnStartup(int loadOnStartup) {
            this.loadOnStartup = loadOnStartup;
        }

    }

    public Servlet getServlet() {
        return this.servlet;
    }


}
