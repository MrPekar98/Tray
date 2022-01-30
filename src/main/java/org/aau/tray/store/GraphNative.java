package org.aau.tray.store;

import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.graph.Node;

import java.util.Iterator;

public class GraphNative implements Graph
{

    @Override
    public void add(Tuple<Node> t)
    {

    }

    @Override
    public void remove(Tuple<Node> t)
    {

    }

    @Override
    public boolean contains(Tuple<Node> t)
    {
        return false;
    }

    @Override
    public Iterator<Node> find(Tuple<Node> t)
    {
        return null;
    }

    @Override
    public Iterator<Node> iterator()
    {
        return null;
    }
}
