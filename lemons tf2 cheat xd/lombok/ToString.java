// 
// Decompiled by Procyon v0.5.36
// 

package lombok;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface ToString {
    boolean includeFieldNames() default true;
    
    String[] exclude() default {};
    
    String[] of() default {};
    
    boolean callSuper() default false;
    
    boolean doNotUseGetters() default false;
}