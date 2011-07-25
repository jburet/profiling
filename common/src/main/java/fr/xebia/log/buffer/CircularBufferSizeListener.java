package fr.xebia.log.buffer;


public abstract class CircularBufferSizeListener {

    private int limitSize;

    protected CircularBufferSizeListener(int limitSize) {
        this.limitSize = limitSize;
    }

    public abstract void sizeOverLimitCallback();

    boolean isSizeOver(int currentSize){
        return currentSize >= limitSize;
    }
}
