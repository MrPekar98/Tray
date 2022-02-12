package org.aau.tray.core;

import org.apache.jena.graph.Node;

import java.io.Serializable;

public class NodeSerializer implements Serializable
{
    private Node n;

    public NodeSerializer(Node n)
    {
        this.n = n;
    }

    public NodeSerializer(String serialized)
    {

    }

    public Node getNode()
    {
        return this.n;
    }

    public String serialize()
    {
        if (n.isBlank())
            return n.getBlankNodeId().toString() + ":BLANK";

        else if (n.isLiteral())
            return n.getLiteral().toString() + ":LITERAL";

        else if (n.isURI())
            return n.getURI() + ":URI";

        else if (n.isVariable())
            return n.getName() + ":VARIABLE";

        else
            return n.toString() + ":OTHER";
    }

    private static void deserialize(String serialized)
    {

    }
}
