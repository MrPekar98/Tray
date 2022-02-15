package org.aau.tray.system;

import org.aau.tray.core.NodeId;
import org.aau.tray.store.data.Fetchable;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;

import java.util.*;

// TODO: Dictionaries should not contain tuple of nodes, but tuple of node IDs, and then use the node dictionary for the conversion
// TODO: Same for triple dictionary
// TODO: I think this ID dictionary component needs an entire rewrite. It is a pretty central elements. It won't harm the TupleTable, which uses this component.
// Order f is the fully concrete triple dictionary
public class IdDictionary
{
    private List<Map<Tuple<Node>, Integer>> dictionaries;
    private Map<Node, Integer> nodeDictionary = new HashMap<>();
    private IdDictionaryMemory mem;

    public IdDictionary(List<String> orders, Fetchable[] memory, Fetchable tripleMemory, Fetchable nodeMemory)
    {
        orders.add("f");
        this.mem = new IdDictionaryMemory(orders, (Fetchable[]) List.of(memory, tripleMemory).toArray(), nodeMemory);
        this.dictionaries = new ArrayList<>(orders.size());
        this.dictionaries.add(new HashMap<>());

        for (String order : this.mem.getOrders())
        {
            this.dictionaries.add(new HashMap<>());
        }

        load();
    }

    // TODO: Missing saving of single nodes and their serialization
    private void load()
    {
        List<Pair<Node, Integer>> allNodes = this.mem.allNodes();
        allNodes.forEach(p -> this.nodeDictionary.put(p.getLeft(), p.getRight()));

        for (String order : this.mem.getOrders())
        {
            List<Pair<Tuple<Node>, Integer>> allTuples = this.mem.allTuples(order);
            allTuples.forEach(p -> this.dictionaries.get(this.mem.getOrders().indexOf(order)).put(p.getLeft(), p.getRight()));
        }
    }

    public void addNode(Node n, Integer id)
    {
        this.nodeDictionary.put(n, id);
        this.mem.saveNode(n, id);
    }

    public void removeNode(Node n)
    {
        if (containsNode(n))
            this.mem.deleteNode(getNodeId(n));

        this.nodeDictionary.remove(n);
    }

    public boolean containsNode(Node n)
    {
        return this.nodeDictionary.containsKey(n);
    }

    public Integer getNodeId(Node n)
    {
        return this.nodeDictionary.get(n);
    }

    private Map<Tuple<Node>, Integer> getTripleDictionary()
    {
        int idx = this.mem.getOrders().indexOf("f");
        return this.dictionaries.get(idx);
    }

    public void addTriple(Tuple<Node> triple, Integer id)
    {
        if (triple.len() != 3)
            throw new IllegalArgumentException("Tuple is not triple: addTriple");

        getTripleDictionary().put(triple, id);
        this.mem.save("f", triple, id);
    }

    public void removeTriple(Tuple<Node> triple)
    {
        if (triple.len() != 3)
            throw new IllegalArgumentException("Tuple is not triple: removeTriple");

        if (containsTriple(triple))
            this.mem.delete("f", getTripleId(triple));

        getTripleDictionary().remove(triple);
    }

    public boolean containsTriple(Tuple<Node> triple)
    {
        if (triple.len() != 3)
            throw new IllegalArgumentException("Tuple is not triple: containsTriple");

        return getTripleDictionary().containsKey(triple);
    }

    public Integer getTripleId(Tuple<Node> triple)
    {
        if (triple.len() != 3)
            throw new IllegalArgumentException("Tuple is not triple: getTripleId");

        return getTripleDictionary().get(triple);
    }

    public void addId(String order, Tuple<Node> t, Integer id)
    {
        int dictionaryIndex = this.mem.getOrders().indexOf(order);
        this.dictionaries.get(dictionaryIndex).put(TupleFactory.tuple(t.get(0), t.get(1)), id);
        this.mem.save(order, t, id);
    }

    public void removeId(String order, Tuple<Node> t)
    {
        int dictionaryIndex = this.mem.getOrders().indexOf(order);

        if (containsTuple(order, t))
            this.mem.delete(order, getIdFromTuple(order, t));

        this.dictionaries.get(dictionaryIndex).remove(t);
    }

    public Integer getIdFromTuple(String order, Tuple<Node> t)
    {
        int dictionaryIndex = this.mem.getOrders().indexOf(order);
        return this.dictionaries.get(dictionaryIndex).get(TupleFactory.tuple(t.get(0), t.get(1)));
    }

    public boolean containsTuple(String order, Tuple<Node> t)
    {
        int dictionaryIndex = this.mem.getOrders().indexOf(order);
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
