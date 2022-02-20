package org.aau.tray.store.dictionary;

import org.aau.tray.store.data.Fetchable;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.graph.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NodeDictionary implements Dictionary<Node, Integer>
{
    private Map<Node, Integer> dictionary;
    private NodeDictionaryMemory mem;

    public NodeDictionary(Fetchable nodeMemory)
    {
        this.dictionary = new HashMap<>();  // Default initial capacity is 16, which is way too low
        this.mem = new NodeDictionaryMemory(nodeMemory);
        Iterator<Pair<Node, Integer>> nodes = this.mem.iterator();

        while (nodes.hasNext())
        {
            Pair<Node, Integer> p = nodes.next();
            this.dictionary.put(p.getLeft(), p.getRight());
        }
    }

    @Override
    public synchronized boolean contains(Node n)
    {
        return this.dictionary.containsKey(n);
    }

    @Override
    public synchronized void add(Node n, Integer id)
    {
        this.dictionary.put(n, id);
        this.mem.save(n, id);
    }

    @Override
    public synchronized void remove(Node n)
    {
        if (this.dictionary.containsKey(n))
        {
            this.mem.delete(this.dictionary.get(n));
            this.dictionary.remove(n);
        }
    }

    @Override
    public Integer get(Node n)
    {
        return this.dictionary.get(n);
    }

    // Warning: heavy computation!
    public Node idToNode(Integer id)
    {
        for (Map.Entry<Node, Integer> entry : this.dictionary.entrySet())
        {
            if (entry.getValue().equals(id))
                return entry.getKey();
        }

        return null;
    }
}
