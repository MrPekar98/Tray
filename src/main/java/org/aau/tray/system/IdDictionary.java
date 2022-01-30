package org.aau.tray.system;

import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// Order n is the node dictionary
// Order f is the fully concrete triple dictionary
public class IdDictionary
{
    private List<Map<Tuple<Node>, Integer>> dictionaries;
    private Map<Node, Integer> nodeDictionary = new TreeMap<>();
    private List<String> orders;

    public IdDictionary(List<String> orders)
    {
        this.dictionaries = new ArrayList<>(orders.size());
        this.orders = orders;

        for (String order : this.orders)
        {
            this.dictionaries.add(new TreeMap<>());
        }

        this.dictionaries.add(new TreeMap<>());
        this.orders.add("f");
    }

    public Map<Node, Integer> getNodeDictionary()
    {
        return this.nodeDictionary;
    }

    public Map<Tuple<Node>, Integer> getTripleDictionary()
    {
        int idx = this.orders.indexOf("f");
        return this.dictionaries.get(idx);
    }

    public void addId(String order, Tuple<Node> t, Integer id)
    {
        int dictionaryIndex = this.orders.indexOf(order);
        this.dictionaries.get(dictionaryIndex).put(TupleFactory.tuple(t.get(0), t.get(1)), id);
    }

    public void removeId(String order, Tuple<Node> t)
    {
        int dictionaryIndex = this.orders.indexOf(order);
        this.dictionaries.get(dictionaryIndex).remove(t);
    }

    public Integer getIdFromTuple(String order, Tuple<Node> t)
    {
        int dictionaryIndex = this.orders.indexOf(order);
        return this.dictionaries.get(dictionaryIndex).get(TupleFactory.tuple(t.get(0), t.get(1)));
    }

    public boolean containsTuple(String order, Tuple<Node> t)
    {
        int dictionaryIndex = this.orders.indexOf(order);
        return this.dictionaries.get(dictionaryIndex).containsKey(t);
    }
}
