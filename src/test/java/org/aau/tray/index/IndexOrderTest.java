package org.aau.tray.index;

import org.aau.tray.store.IndexOrder;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexOrderTest
{
    private static Tuple<Node> triple1()
    {
        return TupleFactory.tuple(NodeFactory.createLiteral("literal"),
                                    NodeFactory.createURI("URI"),
                                    NodeFactory.createVariable("var"));
    }

    private static Tuple<Node> triple2()
    {
        return TupleFactory.tuple(NodeFactory.createBlankNode("blank"),
                                    NodeFactory.createURI("URI1"),
                                    NodeFactory.createURI("URI2"));
    }

    private static Tuple<Node> triple3()
    {
        return TupleFactory.tuple(NodeFactory.createURI("URI1"),
                                    NodeFactory.createURI("URI2"),
                                    NodeFactory.createBlankNode("blank"));
    }

    private static Tuple<Node> triple4()
    {
        return TupleFactory.tuple(NodeFactory.createVariable("var1"),
                                    NodeFactory.createURI("URI"),
                                    NodeFactory.createVariable("var2"));
    }

    @Test
    public void testVariables()
    {
        assertEquals(1, IndexOrder.variables(triple1()));
        assertEquals(0, IndexOrder.variables(triple2()));
        assertEquals(0, IndexOrder.variables(triple3()));
        assertEquals(2, IndexOrder.variables(triple4()));
    }

    @Test
    public void testOrder()
    {
        assertEquals("spo", IndexOrder.order(triple1()));
        assertEquals("spo", IndexOrder.order(triple2()));
        assertEquals("spo", IndexOrder.order(triple3()));
        assertEquals("pos", IndexOrder.order(triple4()));
    }

    @Test
    public void testReorder()
    {
        Tuple<Node> reordered1 = TupleFactory.tuple(NodeFactory.createURI("URI"),
                                                    NodeFactory.createVariable("var"),
                                                    NodeFactory.createLiteral("literal")),
                reordered2 = TupleFactory.tuple(NodeFactory.createURI("URI2"),
                                                NodeFactory.createURI("URI1"),
                                                NodeFactory.createBlankNode("blank")),
                reordered4 = TupleFactory.tuple(NodeFactory.createVariable("var2"),
                                                NodeFactory.createURI("URI"),
                                                NodeFactory.createVariable("var1"));

        assertEquals(reordered1, IndexOrder.reorder("pos", triple1()));
        assertEquals(reordered2, IndexOrder.reorder("ops", triple2()));
        assertEquals(triple3(), IndexOrder.reorder("spo", triple3()));
        assertEquals(reordered4, IndexOrder.reorder("ops", triple4()));
    }
}
