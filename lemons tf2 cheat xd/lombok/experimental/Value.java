// 
// Decompiled by Procyon v0.5.36
// 

package lombok.experimental;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
@Deprecated
public @interface Value {
    String staticConstructor() default "";
}
