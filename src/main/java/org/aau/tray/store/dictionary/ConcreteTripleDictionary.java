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

public class ConcreteTripleDictionary extends TripleDictionary
{
    private Map<Tuple<Integer>, Integer> dictionary;

    public ConcreteTripleDictionary(Fetchable tripleMemory, NodeDictionary nodeDictionary)
    {
        super(nodeDictionary, new TripleDictionaryMemory(tripleMemory));
        this.dictionary = new HashMap<>();
        Iterator<Pair<Tuple<Integer>, Integer>> triples = getDictionaryMemory().iterator();

        while (triples.hasNext())
        {
            Pair<Tuple<Integer>, Integer> p = triples.next();
            this.dictionary.put(p.getLeft(), p.getRight());
        }
    }

    @Override
    protected boolean abstractContains(Tuple<Node> triple)
    {
        if (!isFullyConcrete(triple))
            return false;

        Integer subjectId = getNodeDictionary().get(triple.get(0)),
                predicateId = getNodeDictionary().get(triple.get(1)),
                objectId = getNodeDictionary().get(triple.get(2));

        return this.dictionary.containsKey(TupleFactory.tuple(subjectId, predicateId, objectId));
    }

    @Override
    protected void abstractAdd(Tuple<Node> triple, Integer id)
    {
        if (isFullyConcrete(triple))
        {
            Integer subjectId = getNodeDictionary().get(triple.get(0)),
                    predicateId = getNodeDictionary().get(triple.get(1)),
                    objectId = getNodeDictionary().get(triple.get(2));

            if (subjectId != null && predicateId != null && objectId != null)
            {
                Tuple<Integer> ids = TupleFactory.tuple(subjectId, predicateId, objectId);
                this.dictionary.put(ids, id);
                getDictionaryMemory().save(ids, id);
            }
        }
    }

    @Override
    protected void abstractRemove(Tuple<Node> triple)
    {
        if (isFullyConcrete(triple))
        {
            Integer subjectId = getNodeDictionary().get(triple.get(0)),
                    predicateId = getNodeDictionary().get(triple.get(1)),
                    objectId = getNodeDictionary().get(triple.get(2));
            Tuple<Integer> ids = TupleFactory.tuple(subjectId, predicateId, objectId);

            if (this.dictionary.containsKey(ids))
            {
                getDictionaryMemory().delete(this.dictionary.get(ids));
                this.dictionary.remove(ids);
            }
        }
    }

    @Override
    protected Integer abstractGet(Tuple<Node> triple)
    {
        if (isFullyConcrete(triple))
        {
            Integer subjectId = getNodeDictionary().get(triple.get(0)),
                    predicateId = getNodeDictionary().get(triple.get(1)),
                    objectId = getNodeDictionary().get(triple.get(2));
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
}
