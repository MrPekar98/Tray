package org.aau.tray.store;

import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.graph.Node;

import java.util.Iterator;

public interface Graph extends Iterable<Node>
{
    void add(Tuple<Node> t);
    void remove(Tuple<Node> t);
    boolean contains(Tuple<Node> t);
    Iterator<Node> find(Tuple<Node> t);
}
