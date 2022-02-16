package org.aau.tray.store.dictionary;

import org.aau.tray.store.data.Fetchable;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// This dictionary requires first two nodes to be concrete
public class TriplePatternDictionary extends TripleDictionary
{
    private Map<Tuple<Integer>, Integer> dictionary;
    private String order;

    public TriplePatternDictionary(Fetchable patternMemory, NodeDictionary nodeDictionary, String order)
    {
        super(nodeDictionary, new TriplePatternDictionaryMemory(patternMemory));
        this.dictionary = new HashMap<>();
        this.order = order;
        Iterator<Pair<Tuple<Integer>, Integer>> patterns = getDictionaryMemory().iterator();

        while (patterns.hasNext())
        {
            Pair<Tuple<Integer>, Integer> p = patterns.next();
            this.dictionary.put(p.getLeft(), p.getRight());
        }
    }

    public String getOrder()
    {
        return this.order;
    }

    @Override
    protected boolean abstractContains(Tuple<Node> triple)
    {
        if (triple.len() < 2)
            return false;

        Integer id1 = getNodeDictionary().get(triple.get(0)),
                id2 = getNodeDictionary().get(triple.get(1));

        return this.dictionary.containsKey(TupleFactory.tuple(id1, id2));
    }

    @Override
    protected void abstractAdd(Tuple<Node> triple, Integer id)
    {
        if (triple.len() >= 2)
        {
            Integer id1 = getNodeDictionary().get(triple.get(0)),
                    id2 = getNodeDictionary().get(triple.get(1));

            if (id1 != null && id2 != null)
            {
                Tuple<Integer> ids = TupleFactory.tuple(id1, id2);
                this.dictionary.put(ids, id);
                getDictionaryMemory().save(ids, id);
            }
        }
    }

    @Override
    protected void abstractRemove(Tuple<Node> triple)
    {
        if (triple.len() >= 2)
        {
            Integer id1 = getNodeDictionary().get(triple.get(0)),
                    id2 = getNodeDictionary().get(triple.get(1));
            Tuple<Integer> ids = TupleFactory.tuple(id1, id2);

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
        if (triple.len() >= 2)
        {
            Integer id1 = getNodeDictionary().get(triple.get(0)),
                    id2 = getNodeDictionary().get(triple.get(1));

            return this.dictionary.get(TupleFactory.tuple(id1, id2));
        }

        return null;
    }
}
