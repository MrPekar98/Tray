package org.aau.tray.store.index;

import org.aau.tray.core.NodeId;
import org.apache.jena.graph.Node;

import java.util.Iterator;
import java.util.Set;

public abstract class IndexBase implements Index<Integer>
{
    @Override
    public boolean add(Integer key, Set<NodeId> solutions)
    {
        return abstractAdd(key, solutions);
    }

    @Override
    public boolean remove(Integer key)
    {
        return abstractRemove(key);
    }

    @Override
    public Iterator<Node> find(Integer key)
    {
        return abstractFind(key);
    }

    protected abstract boolean abstractAdd(Integer id, Set<NodeId> solutions);
    protected abstract boolean abstractRemove(Integer id);
    protected abstract Iterator<Node> abstractFind(Integer id);
}
