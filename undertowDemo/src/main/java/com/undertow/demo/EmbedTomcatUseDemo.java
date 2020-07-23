package com.undertow.demo;

import org.apache.catalina.startup.Tomcat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class EmbedTomcatUseDemo {

//    Tomcat tomcat = new Tomcat();
//         HttpServlet httpServlet = new HttpServlet() {
//             @Override
//             protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//                            resp.getWriter().write("hello, i'm embed tomcat");
//                         }
//
//                     @Override
//             protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//                            doGet(req, resp);
//                        }
//        };
//        tomcat.setHostname("localhost");
//        tomcat.setPort(8080);
//        final Context context = tomcat.addContext("/embed-tomcat", null);
//        Tomcat.addServlet(context, "dispatch", httpServlet);
//         context.addServletMappingDecoded("/dispatch", "dispatch");
//        Filter filter = new OncePerRequestFilter() {
//            @Override
//             protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//                             System.out.println("filter do something");
//                            filterChain.doFilter(httpServletRequest, httpServletResponse);
//                        }
//       };
//        FilterDef filterDef = new FilterDef();
//        filterDef.setFilter(filter);
//         filterDef.setFilterName("myFilter");
//        filterDef.addInitParameter("userName", "Allen");
//
//                 FilterMap filterMap = new FilterMap();
//         filterMap.addURLPatternDecoded("/*");
//         filterMap.addServletName("*");
//         filterMap.setFilterName("myFilter");
//         filterMap.setCharset(Charset.forName("UTF-8"));
//
//                 context.addFilterDef(filterDef);
//         context.addFilterMap(filterMap);
//         try {
//                     tomcat.init();
//                     tomcat.start();
//                    tomcat.getServer().await();
//                 } catch (LifecycleException e) {
//                     e.printStackTrace();
//                 }
//     }
}
