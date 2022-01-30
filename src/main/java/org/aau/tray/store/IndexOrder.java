package org.aau.tray.store;

import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;

public class IndexOrder
{
    // Counts number of variables in tuple of nodes
    public static int variables(Tuple<Node> t)
    {
        int count = 0;

        for (int i = 0; i < t.len(); i++)
        {
            if (t.get(i).isVariable())
                count++;
        }

        return count;
    }

    // Computes the string order of a tuple
    // Input tuple must be of length 3 and be of order SPO
    public static String order(Tuple<Node> t)
    {
        if ((t.get(0).isConcrete() && t.get(1).isConcrete() && t.get(2).isConcrete()) ||
            (!t.get(0).isConcrete() && !t.get(1).isConcrete() && !t.get(2).isConcrete()))
            return "spo";

        else if (t.get(0).isConcrete())
            return "spo";

        else if (t.get(1).isConcrete())
            return "pos";

        else if (t.get(2).isConcrete())
            return "ops";

        return "spo";
    }

    // Re-orders tuples according to string order
    public static Tuple<Node> reorder(String order, Tuple<Node> t)
    {
        Node[] nodes = new Node[3];

        for (int i = 0; i < t.len(); i++)
        {
            if (order.charAt(i) == 's')
                nodes[i] = t.get(0);

            else if (order.charAt(i) == 'p')
                nodes[i] = t.get(1);

            else if (order.charAt(i) == 'o')
                nodes[i] = t.get(2);
        }

        return TupleFactory.tuple(nodes);
    }
}
