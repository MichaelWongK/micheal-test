package org.com.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * this is a mark annotation represent it's a MVC-C
 * @author Smith
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Controller {

}
