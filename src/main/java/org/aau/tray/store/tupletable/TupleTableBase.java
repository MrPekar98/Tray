package org.aau.tray.store.tupletable;

import org.aau.tray.store.index.Index;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.graph.Node;

import java.util.Iterator;
import java.util.List;

public abstract class TupleTableBase
{
    protected Index<Integer>[] indexes;
    protected List<String> orders;

    protected TupleTableBase(Index<Integer>[] indexOrders, List<String> orders)
    {
        if (indexOrders.length != orders.size())
            throw new IllegalArgumentException("Un-matching: number of indexes do not match number of index orders");

        this.indexes = indexOrders;
        this.orders = orders;
    }

    public Index<Integer>[] getIndexes()
    {
        return this.indexes;
    }

    protected abstract void add(Tuple<Node> t);
    protected abstract void remove(Tuple<Node> t);
    protected abstract Iterator<Node> find(Tuple<Node> t);
}
