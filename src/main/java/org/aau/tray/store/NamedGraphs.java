package org.aau.tray.store;

import org.apache.jena.atlas.iterator.IteratorConcat;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;

import java.util.*;

public class NamedGraphs implements Graph
{
    private Map<Node, Graph> graphs = new HashMap<>();

    public void addGraph(Node n, Graph g)
    {
        if (!this.graphs.containsKey(n))
            this.graphs.put(n, g);
    }

    private static Tuple<Node> toTriple(Tuple<Node> quad)
    {
        return TupleFactory.tuple(quad.get(1), quad.get(2), quad.get(3));
    }

    @Override
    public void add(Tuple<Node> t)
    {
        if (t.len() != 4)
            throw new IllegalArgumentException("Can only add quads");

        this.graphs.get(t.get(0)).add(toTriple(t));
    }

    @Override
    public void remove(Tuple<Node> t)
    {
        if (t.len() != 4)
            throw new IllegalArgumentException("Can only remove quads");

        this.graphs.get(t.get(0)).remove(toTriple(t));
    }

    @Override
    public boolean contains(Tuple<Node> t)
    {
        if (t.len() != 4)
            throw new IllegalArgumentException("Can only check existence for quads");

        return this.graphs.get(t.get(0)).contains(toTriple(t));
    }

    @Override
    public Iterator<Node> find(Tuple<Node> t)
    {
        if (t.len() != 4)
            throw new IllegalArgumentException("Can only find from quads");

        return this.graphs.get(t.get(0)).find(toTriple(t));
    }

    @Override
    public Iterator<Node> iterator()
    {
        Iterator<Node> iter = Collections.emptyIterator();

        for (Map.Entry<Node, Graph> entry : this.graphs.entrySet())
        {
            iter = IteratorConcat.concat(iter, entry.getValue().iterator());
        }

        return iter;
    }
}
