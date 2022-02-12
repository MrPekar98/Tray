package org.aau.tray.system;

import org.aau.tray.core.NodeId;
import org.aau.tray.core.NodeSerializer;
import org.aau.tray.store.data.Fetchable;
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
    private Fetchable[] mem;

    public IdDictionary(List<String> orders, Fetchable[] memory)
    {
        this.dictionaries = new ArrayList<>(orders.size());
        this.orders = orders;
        this.mem = memory;

        for (String order : this.orders)
        {
            this.dictionaries.add(new TreeMap<>());
        }

        this.dictionaries.add(new TreeMap<>());
        this.orders.add("f");
        load();
    }

    private void load()
    {
        for (String order : this.orders)
        {
            int orderIdx = this.orders.indexOf(order);

        }
    }

    private void save(String order, Tuple<Node> t, Integer id)
    {
        NodeSerializer s1 = new NodeSerializer(t.get(0)), s2 = new NodeSerializer(t.get(1));
        int orderIdx = this.orders.indexOf(order);
        this.mem[orderIdx].write(id, s1.serialize() + "--" + s2.serialize());
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
        save(order, t, id);
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

    public NodeId highestId()
    {
        int highest = 0;

        for (Map.Entry<Node, Integer> entry : this.nodeDictionary.entrySet())
        {
            if (entry.getValue() > highest)
                highest = entry.getValue();
        }

        return NodeId.make(highest);
    }
}
