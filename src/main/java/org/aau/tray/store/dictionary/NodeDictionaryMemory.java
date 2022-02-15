package org.aau.tray.store.dictionary;

import org.aau.tray.core.NodeSerializer;
import org.aau.tray.store.data.Fetchable;
import org.aau.tray.store.data.RAM;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.graph.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodeDictionaryMemory implements DictionaryMemory<Node>
{
    private Fetchable mem;

    public NodeDictionaryMemory(Fetchable memory)
    {
        this.mem = memory;
    }

    @Override
    public void save(Node n, Integer id)
    {
        NodeSerializer s = new NodeSerializer(n);
        this.mem.write(id, s.serialize() + "--" + id);
    }

    @Override
    public void delete(Integer id)
    {
        if (this.mem instanceof RAM)
            this.mem.delete(id);
    }

    // TODO: This is not feasible. We cannot read everything into RAM.
    @Override
    public Iterator<Pair<Node, Integer>> iterator()
    {
        List<Pair<Node, Integer>> nodes = new ArrayList<>();
        int offset = 0;

        try
        {
            while (true)
            {
                Pair<Node, Integer> data = readNode(offset++);
                nodes.add(data);
            }
        }

        catch (Exception exc)
        {
            return nodes.iterator();
        }
    }

    private Pair<Node, Integer> readNode(int offset)
    {
        String[] data = this.mem.read(offset).split("--");
        NodeSerializer s = new NodeSerializer(data[0]);
        return Pair.create(s.getNode(), Integer.parseInt(data[1]));
    }
}
