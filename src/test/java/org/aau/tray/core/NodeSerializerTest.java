package org.aau.tray.core;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeSerializerTest
{
    @Test
    public void testSerialization()
    {
        Node var = NodeFactory.createVariable("test"), uri = NodeFactory.createURI("uri.test"),
                blank = NodeFactory.createBlankNode("blank"), literal = NodeFactory.createLiteral("12345");

        assertEquals("test:VARIABLE", (new NodeSerializer(var)).serialize());
        assertEquals("uri.test:URI", (new NodeSerializer(uri)).serialize());
        assertEquals("blank:BLANK", (new NodeSerializer(blank)).serialize());
        assertEquals("12345:LITERAL", (new NodeSerializer(literal)).serialize());
    }

    @Test
    public void testDeserialization()
    {
        String var = "test:VARIABLE", uri = "uri.test:URI", blank = "blank:BLANK", literal = "12345:LITERAL";
        assertEquals(NodeFactory.createVariable("test"), (new NodeSerializer(var)).getNode());
        assertEquals(NodeFactory.createURI("uri.test"), (new NodeSerializer(uri)).getNode());
        assertEquals(NodeFactory.createBlankNode("blank"), (new NodeSerializer(blank)).getNode());
        assertEquals(NodeFactory.createLiteral("12345"), (new NodeSerializer(literal)).getNode());
    }
}
