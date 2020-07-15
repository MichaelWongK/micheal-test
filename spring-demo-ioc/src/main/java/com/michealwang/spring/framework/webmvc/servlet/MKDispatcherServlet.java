package com.michealwang.spring.framework.webmvc.servlet;

import com.michealwang.spring.framework.annotation.*;
import com.michealwang.spring.framework.context.MKApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 自定义servlet
 */
public class MKDispatcherServlet extends HttpServlet {

    private Map<String, Method> handlerMapping = new HashMap<String, Method>();

    private MKApplicationContext context;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatcher(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception Detail " + Arrays.toString(e.getStackTrace()));
        }

    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // 调用具体的方法

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = ( "/" + url).replaceAll(contextPath, "").replaceAll("/+", "/");

        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 Not Found !!!");
            return;
        }

        Map<String, String[]> params = req.getParameterMap();
        Method method = this.handlerMapping.get(url);

        // 形参
        Class<?> [] paramterTypes = method.getParameterTypes();

        // 实参
        Object [] paramValues = new Object[paramterTypes.length];

        for (int i=0; i < paramterTypes.length; i++) {
            Class paramterType = paramterTypes[i];
            if (paramterType == HttpServletRequest.class) {
                paramValues[i] = req;
            } else if (paramterType == HttpServletResponse.class) {
                paramValues[i] = resp;
            } else if (paramterType == String.class) {
                Annotation[][] pa =method.getParameterAnnotations();
                for (Annotation annotation : pa[i]) {
                    if (annotation instanceof MKRequestParam) {
                        String paramName = ((MKRequestParam) annotation).value();
                        if (!"".equals(paramName)) {
                            String value = Arrays.toString(params.get(paramName))
                                    .replaceAll("\\[\\]", "")
                                    .replaceAll("\\s", "");
                            paramValues[i] = value;
                        }
                    }
                }

            } else {
                paramValues[i] = null;
            }
        }


        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        method.invoke(context.getBean(beanName), paramValues);

    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        // IOC
//        // 1.读取配置文件
//        doLoadConfig(config.getInitParameter("contextConfigLocation"));
//
//        // 2.扫描相关的类
//        doScanner(coonfigContext.getProperty("scanPackage"));
//
//        // 3.实例化相关的类，并缓存到ioc容器中
//        doInstance();

        // 4.完成依赖注入
//        doAutowired();
        context = new MKApplicationContext(config.getInitParameter("contextConfigLocation"));

        // 5.初始化handlerMapping
        doInitHandlerMapping();

        // 6.完成
        System.out.println("MKSpring is init OK !!!");

    }

    private void doInitHandlerMapping() {

        if (this.context.getBeanDefinitionCount() == 0) {return;}

        String[] beanNames = this.context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object instance = this.context.getBean(beanName);

            Class<?> clazz = instance.getClass();

            if (!clazz.isAnnotationPresent(MKController.class)) {continue;}

            String baseUrl = clazz.getAnnotation(MKRequestMapping.class).value();

            for (Method method : clazz.getMethods()) {

                if (!method.isAnnotationPresent(MKRequestMapping.class)) {continue;}

                MKRequestMapping requestMapping = method.getAnnotation(MKRequestMapping.class);

                // 多个 / 替换成一个
                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                handlerMapping.put(url, method);
                System.out.println("Mapped : " + url);
            }
        }

    }

    /**
     * 大写字母A对应ASCII码是65，加上32就是小写字母a对应ASCII码97
     * 同理：小写转大写字符-32即可
     * 如果已经是小写字母再加上32，超出字母对应ascii码值，输出" "空字符
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

}
