package test;

import java.io.Serializable;
import java.lang.Object;

public class StaticMethod {

    public static void methodWithIntArg(int i, int j) {
    }

    public static void methodWithObjectArg(Serializable i, Serializable j) {
    }

    public static class internalClass{
        public static Object callStatic(Class classname){
            return new Object();
        }
    }
}
