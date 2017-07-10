package uff.ic.swlab.ckancrawler.core;

public abstract class Crawler<T> implements AutoCloseable {

    public abstract boolean hasNext();

    public abstract T next();
}