package com.michealwang.spring.framework.webmvc.servlet;

import com.michealwang.spring.framework.annotation.*;
import com.michealwang.spring.framework.context.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义servlet
 */
public class MKDispatcherServlet extends HttpServlet {

    private List<MKHandlerMapping> handlerMappings = new ArrayList<MKHandlerMapping>();

    private Map<MKHandlerMapping, MKHandlerAdapter> handlerAdapters = new HashMap<MKHandlerMapping, MKHandlerAdapter>();

    private List<MKViewResolver> viewResolvers = new ArrayList<MKViewResolver>();

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
//            resp.getWriter().write("500 Exception Detail " + Arrays.toString(e.getStackTrace()));
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("detail", "500 Exception Detail");
            model.put("stackTrace", Arrays.toString(e.getStackTrace()));
            try {
                processDispatchResult(req, resp, new MKModelAndView("500", model));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // 1. 根据URL区拿到对应的HandleerMapping
        MKHandlerMapping handler = getHandler(req);

        // 2. 如果拿到的Handler为null，返回404
        if (handler == null) {
            processDispatchResult(req, resp, new MKModelAndView("404"));
            return;
        }

        // 3. 根据handlerMapping拿到它对应的HandlerAdater
        MKHandlerAdapter ha = getHandlerAdapter(handler);

        // 4. 根据HandlerAdater的返回结果判断
        MKModelAndView mv = ha.handle(req, resp, handler);

        // 5. 根据ModelAndView内容，决定选择用哪个ViewResolver去解析
        processDispatchResult(req, resp, mv);

    }

    private MKHandlerAdapter getHandlerAdapter(MKHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {return null;}
        return this.handlerAdapters.get(handler);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, MKModelAndView mv) throws Exception {
        if (null == mv) {return;}

        if (this.viewResolvers.isEmpty()) {return;}

        for (MKViewResolver viewResolver : this.viewResolvers) {
            MKView view = viewResolver.resolveViewName(mv.getViewName());
            view.render(mv.getModel(), req, resp);
        }

    }

    private MKHandlerMapping getHandler(HttpServletRequest req) {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = ( "/" + url).replaceAll(contextPath, "").replaceAll("/+", "/");

        for (MKHandlerMapping handlerMapping : handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) {continue;}
            return handlerMapping;
        }
        return null;
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
//        doInitHandlerMapping();

        initStrategies(context);
        // 6.完成
        System.out.println("MKSpring is init OK !!!");

    }

    private void initStrategies(MKApplicationContext context) {

        // HandlerMappings
        this.initHandlerMappings(context);
        // 初始化参数适配器
        this.initHandlerAdapters(context);
        // 初始化视图转换器
        this.initViewResolvers(context);

    }

    private void initViewResolvers(MKApplicationContext context) {

        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        for (File file : templateRootDir.listFiles()) {
            this.viewResolvers.add(new MKViewResolver(templateRoot));
        }

    }

    private void initHandlerAdapters(MKApplicationContext context) {
        for (MKHandlerMapping handlerMapping : handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new MKHandlerAdapter());
        }

    }

    private void initHandlerMappings(MKApplicationContext context) {

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
                Pattern pattern = Pattern.compile(url);
                handlerMappings.add(new MKHandlerMapping(instance, method, pattern));
                System.out.println("Mapped : " + url);
            }
        }
    }

    private void doInitHandlerMapping() {

//        if (this.context.getBeanDefinitionCount() == 0) {return;}
//
//        String[] beanNames = this.context.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            Object instance = this.context.getBean(beanName);
//
//            Class<?> clazz = instance.getClass();
//
//            if (!clazz.isAnnotationPresent(MKController.class)) {continue;}
//
//            String baseUrl = clazz.getAnnotation(MKRequestMapping.class).value();
//
//            for (Method method : clazz.getMethods()) {
//
//                if (!method.isAnnotationPresent(MKRequestMapping.class)) {continue;}
//
//                MKRequestMapping requestMapping = method.getAnnotation(MKRequestMapping.class);
//
//                // 多个 / 替换成一个
//                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
//                handlerMapping.put(url, method);
//                System.out.println("Mapped : " + url);
//            }
//        }

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
