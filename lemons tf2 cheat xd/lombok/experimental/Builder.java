// 
// Decompiled by Procyon v0.5.36
// 

package lombok.experimental;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.SOURCE)
@Deprecated
public @interface Builder {
    String builderMethodName() default "builder";
    
    String buildMethodName() default "build";
    
    String builderClassName() default "";
    
    boolean fluent() default true;
    
    boolean chain() default true;
}
