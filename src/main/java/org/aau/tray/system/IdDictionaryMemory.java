package org.aau.tray.system;

import org.aau.tray.core.NodeSerializer;
import org.aau.tray.store.data.Fetchable;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;

import java.util.ArrayList;
import java.util.List;

public final class IdDictionaryMemory
{
    private List<String> orders;
    private Fetchable[] orderMem;
    private Fetchable nodeMem;

    IdDictionaryMemory(List<String> orders, Fetchable[] orderMemory, Fetchable nodeMemory)
    {
        if (orders.size() != orderMemory.length)
            throw new IllegalArgumentException("un-matching number of orders");

        this.orders = orders;
        this.orderMem = orderMemory;
        this.nodeMem = nodeMemory;
    }

    void save(String order, Tuple<Node> t, Integer id)
    {
        NodeSerializer s1 = new NodeSerializer(t.get(0)), s2 = new NodeSerializer(t.get(1));

        if (order.equals("f"))
        {
            NodeSerializer s3 = new NodeSerializer(t.get(2));
            int tripleIdx = this.orders.indexOf("f");
            this.orderMem[tripleIdx].write(id, s1.serialize() + "--" + s2.serialize() + "--" + s3.serialize() + "--" + id);
        }

        else
        {
            int orderIdx = this.orders.indexOf(order);
            this.orderMem[orderIdx].write(id, s1.serialize() + "--" + s2.serialize() + "--" + id);
        }
    }

    void delete(String order, Integer id)
    {
        int orderIdx = this.orders.indexOf(order);
        this.orderMem[orderIdx].delete(id);
    }

    String read(String order, int offset)
    {
        return this.orderMem[this.orders.indexOf(order)].read(offset);
    }

    void saveNode(Node n, Integer id)
    {
        NodeSerializer ser = new NodeSerializer(n);
        this.nodeMem.write(id, ser.serialize() + "--" + id);
    }

    void deleteNode(Integer id)
    {
        this.nodeMem.delete(id);
    }

    String readNode(int offset)
    {
        return this.nodeMem.read(offset);
    }

    List<String> getOrders()
    {
        return this.orders;
    }

    List<Pair<Tuple<Node>, Integer>> allTuples(String order)
    {
        List<Pair<Tuple<Node>, Integer>> tuples = new ArrayList<>();
        Integer idCounter = 0;

        try
        {
            while (true)
            {
                String data = read(order, idCounter++);
                String[] dataSections = data.split("--");
                NodeSerializer s1 = new NodeSerializer(dataSections[0]), s2 = new NodeSerializer(dataSections[1]);
                Tuple<Node> t;
                Integer id;

                if (order.equals("f"))
                {
                    NodeSerializer s3 = new NodeSerializer(dataSections[2]);
                    t = TupleFactory.tuple(s1.getNode(), s2.getNode(), s3.getNode());
                    id = Integer.parseInt(dataSections[3]);
                }

                else
                {
                    t = TupleFactory.tuple(s1.getNode(), s2.getNode());
                    id = Integer.parseInt(dataSections[2]);
                }

                tuples.add(Pair.create(t, id));
            }
        }

        catch (Exception exc)
        {
            return tuples;
        }
    }

    List<Pair<Node, Integer>> allNodes()
    {
        List<Pair<Node, Integer>> nodes = new ArrayList<>();
        Integer idCounter = 0;

        try
        {
            while (true)
            {
                String data = readNode(idCounter++);
                String[] sections = data.split("--");
                NodeSerializer ns = new NodeSerializer(sections[0]);
                Integer id = Integer.parseInt(sections[1]);
                nodes.add(Pair.create(ns.getNode(), id));
            }
        }

        catch (Exception exc)
        {
            return nodes;
        }
    }
}
