package org.aau.tray.core;

import org.apache.commons.lang3.SerializationException;
import org.apache.jena.graph.BlankNodeId;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

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
        this.n = deserialize(serialized);
    }

    public Node getNode()
    {
        return this.n;
    }

    public String serialize()
    {
        if (this.n == null)
            throw new NullPointerException("Node is null (maybe because it could not be de-serialized)");

        if (n.isBlank())
            return n.getBlankNodeId().toString() + ";BLANK";

        else if (n.isLiteral())
            return n.getLiteral().getValue().toString() + ";LITERAL";

        else if (n.isURI())
            return n.getURI() + ";URI";

        else if (n.isVariable())
            return n.getName() + ";VARIABLE";

        else
            return n.toString() + ";OTHER";
    }

    private static Node deserialize(String serialized)
    {
        String[] split = serialized.split(";");

        if (split.length != 2)
            throw new SerializationException("Serialized node does not have a single ';' split");

        if (split[1].equals("BLANK"))
            return NodeFactory.createBlankNode(BlankNodeId.create(split[0]));

        else if (split[1].equals("LITERAL"))
            return NodeFactory.createLiteral(split[0]);

        else if (split[1].equals("URI"))
            return NodeFactory.createURI(split[0]);

        else if (split[1].equals("VARIABLE"))
            return NodeFactory.createVariable(split[0]);

        else
            return null;
    }
}
