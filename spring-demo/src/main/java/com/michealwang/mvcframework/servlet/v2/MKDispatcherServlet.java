package com.michealwang.mvcframework.servlet.v2;

import com.michealwang.mvcframework.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 自定义servlet
 */
public class MKDispatcherServlet extends HttpServlet {

    // 2.初始化ioc容器
    private Map<String, Object> ioc = new HashMap<String, Object>();

    private Properties coonfigContext = new Properties();

    // 存储所有类名
    private List<String> classNames = new ArrayList<String>();

    private Map<String, Method> handlerMapping = new HashMap<String, Method>();

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
        url = ("/" + url).replaceAll(contextPath, "").replaceAll("/+", "/");

        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 Not Found !!!");
            return;
        }

        Map<String, String[]> params = req.getParameterMap();
        Method method = this.handlerMapping.get(url);

        // 形参
        Class<?>[] paramterTypes = method.getParameterTypes();

        // 实参
        Object[] paramValues = new Object[paramterTypes.length];

        for (int i = 0; i < paramterTypes.length; i++) {
            Class paramterType = paramterTypes[i];
            if (paramterType == HttpServletRequest.class) {
                paramValues[i] = req;
            } else if (paramterType == HttpServletResponse.class) {
                paramValues[i] = resp;
            } else if (paramterType == String.class) {
                Annotation[][] pa = method.getParameterAnnotations();
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
        method.invoke(ioc.get(beanName), paramValues);

    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        // 1.读取配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 2.扫描相关的类
        doScanner(coonfigContext.getProperty("scanPackage"));

        // 3.实例化相关的类，并缓存到ioc容器中
        doInstance();

        // 4.完成依赖注入
        doAutowired();

        // 5.初始化handlerMapping
        doInitHandlerMapping();

        // 6.完成
        System.out.println("MKSpring is init OK !!!");

    }

    private void doInitHandlerMapping() {

        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if (!clazz.isAnnotationPresent(MKController.class)) {
                continue;
            }

            String baseUrl = clazz.getAnnotation(MKRequestMapping.class).value();

            for (Method method : clazz.getMethods()) {

                if (!method.isAnnotationPresent(MKRequestMapping.class)) {
                    continue;
                }

                MKRequestMapping requestMapping = method.getAnnotation(MKRequestMapping.class);

                // 多个 / 替换成一个
                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                handlerMapping.put(url, method);
                System.out.println("Mapped : " + url);
            }

        }


    }

    private void doAutowired() {

        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            // private/public/protected/default
            Field fields[] = entry.getClass().getDeclaredFields();

            for (Field field : fields) {
                if (!field.isAnnotationPresent(MKAutowired.class)) {
                    continue;
                }

                MKAutowired autowired = field.getAnnotation(MKAutowired.class);
                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }

                // 暴力访问
                field.setAccessible(true);

                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(MKController.class)) {
                    // 默认类名首字母小写
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(MKService.class)) {
                    // 1.默认类名首字母小写
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    // 2.自定义beanName 不同包下相同类名
                    MKService service = clazz.getAnnotation(MKService.class);
                    if (!"".equals(service.value())) {
                        beanName = service.value();
                    }

                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);

                    // 3.如果是接口，用它的实现类赋值
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (ioc.containsKey(i.getName())) {
                            throw new Exception("The beanName is exists!!");
                        }
                        // 匹配接口类型
                        ioc.put(i.getName(), instance);
                    }

                } else {
                    continue;
                }


            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 大写字母A对应ASCII码是65，加上32就是小写字母a对应ASCII码97
     * 同理：小写转大写字符-32即可
     * 如果已经是小写字母再加上32，超出字母对应ascii码值，输出" "空字符
     *
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String scanPackage) {

        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classParh = new File(url.getFile());

        for (File file : classParh.listFiles()) {

            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                // 取反，减少代码嵌套
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                // 拼接全类名
                String className = scanPackage + "." + file.getName().replace(".class", "");
                classNames.add(className);

            }


        }

    }

    private void doLoadConfig(String contextConfigLocation) {
        // ClassPath 下去找到application.properties 文件并读取
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);

        try {
            coonfigContext.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
