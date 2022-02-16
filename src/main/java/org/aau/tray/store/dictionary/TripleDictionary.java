package org.aau.tray.store.dictionary;

import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.graph.Node;

public abstract class TripleDictionary implements Dictionary<Tuple<Node>, Integer>
{
    private NodeDictionary nodeDictionary;
    private DictionaryMemory<Tuple<Integer>> dictionaryMemory;

    protected TripleDictionary(NodeDictionary nodeDictionary, DictionaryMemory<Tuple<Integer>> dictionaryMemory)
    {
        this.nodeDictionary = nodeDictionary;
        this.dictionaryMemory = dictionaryMemory;
    }

    public NodeDictionary getNodeDictionary()
    {
        return this.nodeDictionary;
    }

    public DictionaryMemory<Tuple<Integer>> getDictionaryMemory()
    {
        return this.dictionaryMemory;
    }

    @Override
    public synchronized boolean contains(Tuple<Node> triple)
    {
        return abstractContains(triple);
    }

    @Override
    public synchronized void add(Tuple<Node> triple, Integer id)
    {
        abstractAdd(triple, id);
    }

    @Override
    public synchronized void remove(Tuple<Node> triple)
    {
        abstractRemove(triple);
    }

    @Override
    public synchronized Integer get(Tuple<Node> triple)
    {
        return abstractGet(triple);
    }

    protected abstract boolean abstractContains(Tuple<Node> t);
    protected abstract void abstractAdd(Tuple<Node> t, Integer id);
    protected abstract void abstractRemove(Tuple<Node> t);
    protected abstract Integer abstractGet(Tuple<Node> t);
}
