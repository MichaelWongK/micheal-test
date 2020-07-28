package com.michealwang.spring.framework.context;

import com.michealwang.spring.framework.annotation.MKRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MKHandlerAdapter {
    public MKModelAndView handle(HttpServletRequest req, HttpServletResponse resp, MKHandlerMapping handler) throws InvocationTargetException, IllegalAccessException {

        // 1. 保存參数名称和位置
        Map<String, Integer> paramIndexMapping = new HashMap<String, Integer>();
        Annotation[][] pa = handler.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof MKRequestParam) {
                    String paramName = ((MKRequestParam) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }

        // 2. 匹配形参列表，给实参列表赋值
        // 形参
        Class<?>[] paramterTypes = handler.getMethod().getParameterTypes();
        for (int i = 0; i < paramterTypes.length; i++) {
            Class paramterType = paramterTypes[i];
            if (paramterType == HttpServletRequest.class || paramterType == HttpServletResponse.class) {
                paramIndexMapping.put(paramterType.getName(), i);
            }
        }

        // 3. 实际赋值
        Map<String, String[]> params = req.getParameterMap();
        // 实参
        Object[] paramValues = new Object[paramterTypes.length];

        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(params.get(param.getKey()))
                    .replaceAll("\\[|\\]", "")
                    .replaceAll("\\s", "");
            if (!paramIndexMapping.containsKey(param.getKey())) {
                continue;
            }

            int index = paramIndexMapping.get(param.getKey());

            paramValues[index] = caseStringValue(value, paramterTypes[index]);
        }

        if (paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }

        if (paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = resp;
        }

        // 4. 执行调用动作
        Object result = handler.getMethod().invoke(handler.getController(), paramValues);
        if (result == null || result instanceof Void) {
            return null;
        }

        boolean isModelAndView = handler.getMethod().getReturnType() == MKModelAndView.class;
        if (isModelAndView) {
            return (MKModelAndView) result;
        }

        return null;
    }

    private Object caseStringValue(String value, Class<?> paramterType) {
        if (String.class == paramterType) {
            return value;
        }

        if (Integer.class == paramterType) {
            return Integer.valueOf(value);
        } else if (Double.class == paramterType) {
            return Double.valueOf(value);
        } else {
            if (value != null) {
                return value;
            }
            return null;
        }
    }
}
