package fr.xebia.profiling.agent;

public interface RestransformClassListener {
    void beginRetransform(int length);

    void updateTransform(int i);

    void endRetransform();
}
