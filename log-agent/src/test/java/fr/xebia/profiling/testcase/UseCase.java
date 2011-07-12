package fr.xebia.profiling.testcase;

import fr.xebia.profiling.interceptor.Interceptor;

/**
 * Created by IntelliJ IDEA.
 * User: jburet
 * Date: 10/07/11
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */
public class UseCase {

    public Object method1(Object b) {
        long timer;
        timer = System.nanoTime();
        Interceptor.exitMethod("", "", System.nanoTime() - timer);
        return b;
    }
}
