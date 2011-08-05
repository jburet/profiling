package fr.xebia.profiling.interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Receive call from intrumented code for context management.
 * Context should provide a solution for group call and retrieve which log or sampling are linked each other
 */
public class ContextInterceptor {

    private static List<ContextMananager> contextMananagers = new ArrayList<ContextMananager>();

    public static Long getAndUpdateContext(String classname, String methodname, Object[] args){
        // Use firstmatching contextmanager
        for(ContextMananager cm : contextMananagers){
            if(cm.match(classname,  methodname, args)){
                return cm.getContextIdentifier(classname, methodname, args);
            }
        }
        return -1l;
    }

    public static void registerContextManager(ContextMananager contextMananager){
        contextMananagers.add(contextMananager);
    }

}
