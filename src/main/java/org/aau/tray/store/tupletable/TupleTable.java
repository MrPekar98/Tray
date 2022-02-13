package org.aau.tray.store.tupletable;

import org.aau.tray.core.IdFactory;
import org.aau.tray.core.NodeId;
import org.aau.tray.store.IndexOrder;
import org.aau.tray.store.index.Index;
import org.aau.tray.system.System;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.graph.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TupleTable extends TupleTableBase
{
    public TupleTable(Index<Integer>[] indexOrders, List<String> orders)
    {
        super(indexOrders, orders);
    }

    @Override
    protected void add(Tuple<Node> t)
    {
        if (t.len() != 3)
            throw new IllegalArgumentException("Tuple is not a triple");

        for (int i = 0; i < super.indexes.length; i++)
        {
            String order = super.orders.get(i);
            Tuple<Node> reordered = IndexOrder.reorder(order, t);
            NodeId key;

            if (!System.getIdDictionary().containsTuple(order, reordered))
            {
                key = IdFactory.nodeId();
                System.getIdDictionary().addId(order, reordered, key.id());
            }

            else
                key = NodeId.make(System.getIdDictionary().getIdFromTuple(order, reordered));

            Integer solutionId;

            if (System.getIdDictionary().containsNode(t.get(2)))
                solutionId = System.getIdDictionary().getNodeId(t.get(2));

            else
            {
                solutionId = IdFactory.nodeId().id();
                System.getIdDictionary().addNode(t.get(2), solutionId);
            }

            super.indexes[i].add(key.id(), Set.of(NodeId.make(solutionId)));
        }

        System.getIdDictionary().addTriple(t, IdFactory.nodeId().id());
    }

    @Override
    protected void remove(Tuple<Node> t)
    {
        if (t.len() != 3)
            throw new IllegalArgumentException("Tuple is not a triple");

        for (int i = 0; i < super.indexes.length; i++)
        {
            String order = super.orders.get(i);
            Tuple<Node> reordered = IndexOrder.reorder(order, t);
            Integer key = System.getIdDictionary().getIdFromTuple(order, reordered);
            super.indexes[i].remove(key);
            System.getIdDictionary().removeId(order, reordered);
        }

        System.getIdDictionary().removeTriple(t);
    }

    @Override
    protected Iterator<Node> find(Tuple<Node> t)
    {
        if (t.len() != 3)
            throw new IllegalArgumentException("Tuple is not a triple");

        String order = IndexOrder.order(t);
        Tuple<Node> reordered = IndexOrder.reorder(order, t);
        int orderIndex = super.orders.indexOf(order);
        int key = System.getIdDictionary().getIdFromTuple(order, reordered);

        return super.indexes[orderIndex].find(key);
    }
}
