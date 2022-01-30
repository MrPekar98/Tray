package org.aau.tray.store.index;

import org.aau.tray.core.NodeId;
import org.apache.jena.graph.Node;

import java.util.Iterator;
import java.util.Set;

public interface Index<K>
{
    boolean add(K key, Set<NodeId> solutions);
    boolean remove(K key);
    Iterator<Node> find(K key);
}
