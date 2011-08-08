package fr.xebia.profiling.common.agent;

public interface RestransformClassListener {
    void beginRetransform(int length);

    void updateTransform(int i);

    void endRetransform();
}
