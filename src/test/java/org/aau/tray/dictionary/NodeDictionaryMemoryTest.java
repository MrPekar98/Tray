package org.aau.tray.dictionary;

import org.aau.tray.store.data.Disk;
import org.aau.tray.store.data.RAM;
import org.aau.tray.store.dictionary.NodeDictionaryMemory;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NodeDictionaryMemoryTest
{
    private Disk d = new Disk(new File("."));
    private NodeDictionaryMemory dictMemRam, dictMemDisk;
    private Node n1 = NodeFactory.createLiteral("lit1"),
            n2 = NodeFactory.createVariable("var1"),
            n3 = NodeFactory.createLiteral("lit2"),
            n4 = NodeFactory.createVariable("var2");

    @Before
    public void init()
    {
        this.dictMemRam = new NodeDictionaryMemory(new RAM("test"));
        this.dictMemDisk = new NodeDictionaryMemory(this.d);
        this.dictMemRam.save(this.n1, 0);
        this.dictMemRam.save(this.n2, 1);
        this.dictMemRam.save(this.n3, 2);
        this.dictMemDisk.save(this.n1, 0);
        this.dictMemDisk.save(this.n2, 1);
        this.dictMemDisk.save(this.n3, 2);
    }

    @After
    public void tearDown()
    {
        this.d.clear();
    }

    private void testIterator(NodeDictionaryMemory dictMem, Pair<Node, Integer> ... pairs)
    {
        Iterator<Pair<Node, Integer>> iter = dictMem.iterator();
        List<Pair<Node, Integer>> nodes = new ArrayList<>(pairs.length);
        iter.forEachRemaining(nodes::add);
        assertEquals(pairs.length, nodes.size());

        for (Pair<Node, Integer> pair : pairs)
        {
            assertTrue(nodes.contains(pair));
        }
    }

    @Test
    public void simpleTest()
    {
        // RAM
        testIterator(this.dictMemRam,
                Pair.create(this.n1, 0), Pair.create(this.n2, 1), Pair.create(this.n3, 2));

        // Disk
        testIterator(this.dictMemDisk,
                Pair.create(this.n1, 0), Pair.create(this.n2, 1), Pair.create(this.n3, 2));
    }

    @Test
    public void testDelete()
    {
        // RAM
        this.dictMemRam.delete(1);
        testIterator(this.dictMemRam, Pair.create(this.n1, 0), Pair.create(this.n3, 2));

        // Disk
        this.dictMemDisk.delete(1);
        testIterator(this.dictMemDisk,
                Pair.create(this.n1, 0), Pair.create(this.n2, 1), Pair.create(this.n3, 2));
    }

    @Test
    public void testResetDisk()
    {
        NodeDictionaryMemory copy = new NodeDictionaryMemory(this.d);
        testIterator(copy, Pair.create(this.n1, 0), Pair.create(this.n2, 1), Pair.create(this.n3, 2));
    }
}
