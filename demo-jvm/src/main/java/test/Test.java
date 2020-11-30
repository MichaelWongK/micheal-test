package test;

import com.micheal.demo.temp.domain.Person;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Test extends Person {
    public static void main(String[] args) throws IOException {
        InputStream resourceAsStream = Test.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        System.out.println(properties.get("path"));
    }
}
