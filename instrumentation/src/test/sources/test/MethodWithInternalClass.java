package test;

import java.lang.*;
import java.lang.Number;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MethodWithInternalClass {

    public Runnable test1() {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("srfdf");
            }
        };
    }


    public Map.Entry<String, ? extends Number> test2() {
        return new Map.Entry<String, java.lang.Number>() {
            @Override
            public String getKey() {
                return "tre";
            }

            @Override
            public Number getValue() {
                return 1;
            }

            @Override
            public Number setValue(Number value) {
                return 0;
            }
        };
    }

    public static Map.Entry<String, ? extends Number> test3() {
        return new Map.Entry<String, java.lang.Number>() {
            @Override
            public String getKey() {
                return "tre";
            }

            @Override
            public Number getValue() {
                return 1;
            }

            @Override
            public Number setValue(Number value) {
                return 0;
            }
        };
    }

    public static class InnerClass{

        public void test1(){
            
        }

    }


}
