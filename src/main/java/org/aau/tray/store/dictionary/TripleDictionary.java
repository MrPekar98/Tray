package org.aau.tray.store.dictionary;

import org.aau.tray.store.data.Fetchable;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TripleDictionary implements Dictionary<Tuple<Node>, Integer>
{
    private Map<Tuple<Integer>, Integer> dictionary;
    private NodeDictionary nodeDictionary;
    private TripleDictionaryMemory mem;

    public TripleDictionary(Fetchable tripleMemory, NodeDictionary nodeDictionary)
    {
        this.dictionary = new HashMap<>();
        this.nodeDictionary = nodeDictionary;
        this.mem = new TripleDictionaryMemory(tripleMemory);
        Iterator<Pair<Tuple<Integer>, Integer>> triples = this.mem.iterator();

        // It is very heavy to find node in dictionary from ID
        while (triples.hasNext())
        {
            Pair<Tuple<Integer>, Integer> p = triples.next();
            this.dictionary.put(TupleFactory.tuple(p.getLeft().get(0), p.getLeft().get(1), p.getLeft().get(2)),
                    p.getRight());
        }
    }

    @Override
    public synchronized boolean contains(Tuple<Node> triple)
    {
        if (!isFullyConcrete(triple))
            return false;

        Integer subjectId = this.nodeDictionary.get(triple.get(0)),
                predicateId = this.nodeDictionary.get(triple.get(1)),
                objectId = this.nodeDictionary.get(triple.get(2));

        return this.dictionary.containsKey(TupleFactory.tuple(subjectId, predicateId, objectId));
    }

    @Override
    public synchronized void add(Tuple<Node> triple, Integer id)
    {
        if (isFullyConcrete(triple))
        {
            Integer subjectId = this.nodeDictionary.get(triple.get(0)),
                    predicateId = this.nodeDictionary.get(triple.get(1)),
                    objectId = this.nodeDictionary.get(triple.get(2));

            if (subjectId != null && predicateId != null && objectId != null)
            {
                Tuple<Integer> ids = TupleFactory.tuple(subjectId, predicateId, objectId);
                this.dictionary.put(ids, id);
                this.mem.save(ids, id);
            }
        }
    }

    @Override
    public synchronized void remove(Tuple<Node> triple)
    {
        if (isFullyConcrete(triple) && this.dictionary.containsKey(triple))
        {
            Integer subjectId = this.nodeDictionary.get(triple.get(0)),
                    predicateId = this.nodeDictionary.get(triple.get(1)),
                    objectId = this.nodeDictionary.get(triple.get(2));
            Tuple<Integer> ids = TupleFactory.tuple(subjectId, predicateId, objectId);

            if (this.dictionary.containsKey(ids))
            {
                this.mem.delete(this.dictionary.get(ids));
                this.dictionary.remove(ids);
            }
        }
    }

    @Override
    public synchronized Integer get(Tuple<Node> triple)
    {
        if (isFullyConcrete(triple))
        {
            Integer subjectId = this.nodeDictionary.get(triple.get(0)),
                    predicateId = this.nodeDictionary.get(triple.get(1)),
                    objectId = this.nodeDictionary.get(triple.get(2));
            Tuple<Integer> ids = TupleFactory.tuple(subjectId, predicateId, objectId);
            return this.dictionary.get(ids);
        }

        return null;
    }

    private static boolean isFullyConcrete(Tuple<Node> t)
    {
        if (t.len() != 3)
            return false;

        Triple triple = Triple.create(t.get(0), t.get(1), t.get(2));
        return triple.isConcrete();
    }

    public NodeDictionary getNodeDictionary()
    {
        return this.nodeDictionary;
    }
}
