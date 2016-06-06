package org.xdevs23.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tells the developer that the element shall not be used.
 * This annotation has no effect on the source code.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DontUse {
}
