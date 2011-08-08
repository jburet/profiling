package test;

import java.io.FileInputStream;
import java.io.IOException;

public class MethodWithTryFinally {
    public boolean failsToVerify() {
        boolean theBool; // = false ;
        try {
            theBool = true;
        } finally {
            try {
                new FileInputStream("/");
            } catch (IOException ioE) {
                theBool = false;
            }
        }
        return theBool;
    }


}