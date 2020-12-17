package com.micheal.mybatis;

import com.micheal.mybatis.mapper.UserMapper;
import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/15 17:52
 * @Description
 */
public class Application {

    public static void main(String[] args) {
        UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(Application.class.getClassLoader(), new Class[]{UserMapper.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Map<String, Object> nameArgsMap = buildMethodArgNameMap(method, args);
                Select annotation = method.getAnnotation(Select.class);
                if (annotation != null) {
                    String[] value = annotation.value();
                    String paseSQL = paseSQL(value[0], nameArgsMap);
                    System.out.println(paseSQL);
                }
                return null;
            }
        });

        userMapper.selectUserList(1, "test");
    }

    public static String paseSQL(String sql, Map<String, Object> nameArgsMap) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = sql.length();
        for (int i = 0; i < length; i++) {
            char c = sql.charAt(i);
            if (c == '#') {
                int nextIndex = i + 1;
                char nextChar = sql.charAt(nextIndex);
                if (nextChar != '{') {
                    throw new RuntimeException(String.format("这里应该为#{\nsql:%s\nindex:%d"
                            , stringBuilder.toString(), nextIndex));
                }

                StringBuilder argSB = new StringBuilder();
                i = parseSQLArg(argSB, sql, nextIndex);
                String argName = argSB.toString();
                Object argValue = nameArgsMap.get(argName);
                if (argValue == null) {
                    throw new RuntimeException(String.format("找不到参数值:%s", argName));
                }
                stringBuilder.append(argValue.toString());
                continue;
            }

            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    /**
     * 查找对应右侧 } 并替换{}中的参数
     * @param argSB
     * @param sql
     * @param nextIndex
     */
    private static int parseSQLArg(StringBuilder argSB, String sql, int nextIndex) {
        nextIndex++;
        for (; nextIndex < sql.length(); nextIndex++) {
            char c = sql.charAt(nextIndex);
            if (c != '}') {
                argSB.append(c);
                continue;
            }

            return nextIndex;
        }
        // 找不到 } 抛出异常
        throw new RuntimeException(String.format("缺少右括号\nindex:%d", nextIndex));
    }


    public static Map<String, Object> buildMethodArgNameMap(Method method, Object[] args) throws Exception {
        Map<String, Object> nameArgsMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        if (parameters.length < 1) {
            throw new Exception();
        }
        int index[] = {0};
        Arrays.asList(parameters).forEach(parameter -> {
            String name = parameter.getName();
            nameArgsMap.put(name, args[index[0]]);
            index[0]++;
        });

        return nameArgsMap;
    }
}

