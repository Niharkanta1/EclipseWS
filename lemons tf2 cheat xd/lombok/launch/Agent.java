// 
// Decompiled by Procyon v0.5.36
// 

package lombok.launch;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.instrument.Instrumentation;

final class Agent
{
    public static void agentmain(final String agentArgs, final Instrumentation instrumentation) throws Throwable {
        runLauncher(agentArgs, instrumentation, true);
    }
    
    public static void premain(final String agentArgs, final Instrumentation instrumentation) throws Throwable {
        runLauncher(agentArgs, instrumentation, false);
    }
    
    private static void runLauncher(final String agentArgs, final Instrumentation instrumentation, final boolean injected) throws Throwable {
        final ClassLoader cl = Main.createShadowClassLoader();
        try {
            final Class<?> c = cl.loadClass("lombok.core.AgentLauncher");
            final Method m = c.getDeclaredMethod("runAgents", String.class, Instrumentation.class, Boolean.TYPE, Class.class);
            m.invoke(null, agentArgs, instrumentation, injected, Agent.class);
        }
        catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
