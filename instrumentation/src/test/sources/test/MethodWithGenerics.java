package test;

import java.util.ArrayList;
import java.util.Collection;

public class MethodWithGenerics {

    public Object test1(Collection<? extends Number> collection){
        return new ArrayList(collection);
    }

}
