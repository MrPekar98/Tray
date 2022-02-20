package org.aau.tray.dictionary;

import org.aau.tray.store.data.Disk;
import org.aau.tray.store.data.RAM;
import org.aau.tray.store.dictionary.NodeDictionary;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class NodeDictionaryTest
{
    private Disk d = new Disk(new File("."));
    private NodeDictionary dictRAM, dictDisk;
    private Node n1 = NodeFactory.createLiteral("lit1"),
            n2 = NodeFactory.createVariable("var1"),
            n3 = NodeFactory.createLiteral("lit2"),
            n4 = NodeFactory.createVariable("var2");

    @Before
    public void init()
    {
        this.dictRAM = new NodeDictionary(new RAM("test"));
        this.dictDisk = new NodeDictionary(this.d);
        this.dictRAM.add(this.n1, 0);
        this.dictRAM.add(this.n2, 1);
        this.dictRAM.add(this.n3, 2);
        this.dictDisk.add(this.n1, 0);
        this.dictDisk.add(this.n2, 1);
        this.dictDisk.add(this.n3, 2);
    }

    @After
    public void tearDown()
    {
        this.d.clear();
    }

    @Test
    public void testContains()
    {
        // RAM
        assertTrue(this.dictRAM.contains(n1));
        assertTrue(this.dictRAM.contains(n2));
        assertTrue(this.dictRAM.contains(n3));
        assertFalse(this.dictRAM.contains(n4));

        // Disk
        assertTrue(this.dictDisk.contains(n1));
        assertTrue(this.dictDisk.contains(n2));
        assertTrue(this.dictDisk.contains(n3));
        assertFalse(this.dictDisk.contains(n4));
    }

    @Test
    public void testAdd()
    {
        // RAM
        this.dictRAM.add(this.n4, 3);
        assertTrue(this.dictRAM.contains(this.n4));

        // Disk
        this.dictDisk.add(this.n4, 3);
        assertTrue(this.dictDisk.contains(this.n4));
    }

    @Test
    public void testGet()
    {
        // RAM
        assertEquals((Integer) 0, this.dictRAM.get(this.n1));
        assertEquals((Integer) 1, this.dictRAM.get(this.n2));
        assertEquals((Integer) 2, this.dictRAM.get(this.n3));
        assertNull(this.dictRAM.get(this.n4));

        // Disk
        assertEquals((Integer) 0, this.dictDisk.get(this.n1));
        assertEquals((Integer) 1, this.dictDisk.get(this.n2));
        assertEquals((Integer) 2, this.dictDisk.get(this.n3));
        assertNull(this.dictDisk.get(this.n4));
    }

    @Test
    public void testRemove()
    {
        // RAM
        this.dictRAM.remove(this.n1);
        assertFalse(this.dictRAM.contains(this.n1));

        // Disk
        this.dictDisk.remove(this.n1);
        assertFalse(this.dictDisk.contains(this.n1));
    }

    @Test
    public void testResetDisk()
    {
        NodeDictionary copy = new NodeDictionary(this.d);
        assertTrue(copy.contains(n1));
        assertTrue(copy.contains(n2));
        assertTrue(copy.contains(n3));
    }
}
