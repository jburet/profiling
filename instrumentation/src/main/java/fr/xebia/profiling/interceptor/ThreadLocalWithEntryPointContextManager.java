package fr.xebia.profiling.interceptor;

import fr.xebia.log.configuration.RegExpMethodPattern;

import java.util.concurrent.atomic.AtomicLong;

public class ThreadLocalWithEntryPointContextManager implements ContextMananager {

    private AtomicLong counter = new AtomicLong(0);

    private ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    private RegExpMethodPattern regExpMethodPattern;

    public ThreadLocalWithEntryPointContextManager(RegExpMethodPattern regExpMethodPattern) {
        this.regExpMethodPattern = regExpMethodPattern;
    }

    @Override
    public boolean match(String classname, String methodname, Object[] args) {
        // Always match, no need of args
        return true;
    }

    @Override
    public Long getContextIdentifier(String classname, String methodname, Object[] args) {
        // Seach in thread local for identifier.
        // if classname + method name match an entry point create a new identifier
        // Or not exist
        Long contextId = threadLocal.get();
        if (contextId == null || aLeastOnePatterMatch(classname, methodname)) {
            // get a new identifier
            contextId = counter.incrementAndGet();
            threadLocal.set(contextId);
        }
        return contextId;
    }

    private boolean aLeastOnePatterMatch(String classname, String methodname) {
        return regExpMethodPattern.match(classname, methodname);
    }
}
