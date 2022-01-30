package org.aau.tray.store.index;

import org.aau.tray.core.NodeId;
import org.aau.tray.store.data.Fetchable;
import org.apache.jena.graph.Node;

import java.util.*;

public class BucketArray extends IndexBase
{
    private int capacity;
    private List<Set<NodeId>> array;
    private Fetchable data;

    public BucketArray(int initialCapacity, Fetchable data)
    {
        this.capacity = initialCapacity;
        this.data = data;
        this.array = new ArrayList<>(this.capacity);
        Collections.fill(this.array, null);
    }

    // TODO: Add to disk or RAM also
    @Override
    protected boolean abstractAdd(Integer id, Set<NodeId> solutions)
    {
        if (id >= this.array.size())
            resize();

        else if (id < 0)
            throw new IllegalArgumentException("IDs cannot be negative");

        else if (this.array.get(id) != null)
        {
            this.array.get(id).addAll(solutions);
            return false;
        }

        this.array.add(id, solutions);
        return true;
    }

    private void resize()
    {
        this.array.addAll(Collections.nCopies(this.array.size() + this.capacity, null));
    }

    // TODO: Remove from disk or RAM too
    @Override
    protected boolean abstractRemove(Integer id)
    {
        if (id < 0)
            throw new IllegalArgumentException("IDs cannot be negative");

        else if (id >= this.array.size())
            throw new IllegalArgumentException("ID too high");

        else if (this.array.get(id) == null)
            return false;

        this.array.remove(id.intValue());
        this.array.add(id, null);
        return true;
    }

    @Override
    protected Iterator<Node> abstractFind(Integer id)
    {
        if (id < 0)
            throw new IllegalArgumentException("IDs cannot be negative");

        else if (id >= this.array.size())
            throw new IllegalArgumentException("ID too high");

        return null;
    }
}
