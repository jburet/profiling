package fr.xebia.profiling.test.manual.tested;


public class TestedApp {


    public void method1() {
        int j = 1;
        for (int i = 0; i < 100000; i++) {
            j++;
        }
    }

    public int method2(int max) {
        int j = 1;
        for (int i = 0; i < max; i++) {
            j++;
        }
        return j;
    }

        public int method3(int max, int offset) {
        int j = offset;
        for (int i = 0; i < max; i++) {
            j++;
        }
        return j;
    }
}
