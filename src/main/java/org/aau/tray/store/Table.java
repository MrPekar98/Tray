package org.aau.tray.store;

import org.apache.jena.atlas.lib.tuple.Tuple;

import java.util.function.Predicate;

public abstract class Table<E>
{
    private int dimension;

    public Table(int dimension)
    {
        this.dimension = dimension;
    }

    public int getDimension()
    {
        return this.dimension;
    }

    public synchronized void add(Tuple<E> record)
    {
        if (record.len() != this.dimension)
            throw new IllegalArgumentException("Un-matching record dimension: add");

        abstractAdd(record);
    }

    public synchronized void delete(Tuple<E> record)
    {
        if (record.len() != this.dimension)
            throw new IllegalArgumentException("Un-matching record dimension: delete");

        abstractDelete(record);
    }

    public synchronized void delete(int row)
    {
        if (row < 0)
            throw new IllegalArgumentException("Row index cannot be negative: delete");

        abstractDelete(row);
    }

    public synchronized E get(int row)
    {
        if (row < 0)
            throw new IllegalArgumentException("Row index cannot be negative: get");

        return abstractGet(row);
    }

    public synchronized E get(Predicate<E> predicate)
    {
        return abstractGet(predicate);
    }

    public int size()
    {
        return abstractSize();
    }

    protected abstract void abstractAdd(Tuple<E> record);
    protected abstract void abstractDelete(Tuple<E> record);
    protected abstract void abstractDelete(int row);
    protected abstract E abstractGet(int row);
    protected abstract E abstractGet(Predicate<E> predicate);
    protected abstract int abstractSize();
}
