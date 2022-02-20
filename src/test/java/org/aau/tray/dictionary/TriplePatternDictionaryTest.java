package org.aau.tray.dictionary;

import org.aau.tray.store.data.Disk;
import org.aau.tray.store.data.RAM;
import org.aau.tray.store.dictionary.NodeDictionary;
import org.aau.tray.store.dictionary.TriplePatternDictionary;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class TriplePatternDictionaryTest
{
    private Disk dictDisk = new Disk(new File("."), "dict"), nodes = new Disk(new File("."), "nodes");
    private TriplePatternDictionary tpDictRam, tpDictDisk;
    private Node n11 = NodeFactory.createBlankNode("b1"),
                    n12 = NodeFactory.createURI("uri1"),
                    n13 = NodeFactory.createVariable("var1"),
                    n21 = NodeFactory.createBlankNode("b2"),
                    n22 = NodeFactory.createURI("uri2"),
                    n23 = NodeFactory.createVariable("var2"),
                    n31 = NodeFactory.createBlankNode("b3"),
                    n32 = NodeFactory.createURI("uri3"),
                    n33 = NodeFactory.createVariable("var3");
    private Tuple<Node> triple1 = TupleFactory.tuple(this.n11, this.n12, this.n13),
                        triple2 = TupleFactory.tuple(this.n21, this.n22, this.n23),
                        triple3 = TupleFactory.tuple(this.n31, this.n32, this.n33);

    @Before
    public void init()
    {
        NodeDictionary ndRam = new NodeDictionary(new RAM("ram"));
        NodeDictionary ndDisk = new NodeDictionary(this.nodes);
        ndRam.add(this.n11, 0);
        ndRam.add(this.n12, 1);
        ndRam.add(this.n13, 2);
        ndRam.add(this.n21, 3);
        ndRam.add(this.n22, 4);
        ndRam.add(this.n23, 5);
        ndRam.add(this.n31, 6);
        ndRam.add(this.n32, 7);
        ndRam.add(this.n33, 8);
        ndDisk.add(this.n11, 0);
        ndDisk.add(this.n12, 1);
        ndDisk.add(this.n13, 2);
        ndDisk.add(this.n21, 3);
        ndDisk.add(this.n22, 4);
        ndDisk.add(this.n23, 5);
        ndDisk.add(this.n31, 6);
        ndDisk.add(this.n32, 7);
        ndDisk.add(this.n33, 8);

        this.tpDictRam = new TriplePatternDictionary(new RAM("test"), ndRam, "spo");
        this.tpDictDisk = new TriplePatternDictionary(this.dictDisk, ndDisk, "spo");
        this.tpDictRam.add(this.triple1, 0);
        this.tpDictRam.add(this.triple2, 1);
        this.tpDictDisk.add(this.triple1, 0);
        this.tpDictDisk.add(this.triple2, 1);
    }

    @After
    public void tearDown()
    {
        this.dictDisk.clear();
        this.nodes.clear();
    }

    @Test
    public void testGetOrder()
    {
        // RAM
        assertEquals("spo", this.tpDictRam.getOrder());

        // Disk
        assertEquals("spo", this.tpDictDisk.getOrder());
    }

    @Test
    public void testContains()
    {
        // RAM
        assertTrue(this.tpDictRam.contains(this.triple1));
        assertTrue(this.tpDictRam.contains(this.triple2));
        assertFalse(this.tpDictRam.contains(this.triple3));

        // Disk
        assertTrue(this.tpDictDisk.contains(this.triple1));
        assertTrue(this.tpDictDisk.contains(this.triple2));
        assertFalse(this.tpDictDisk.contains(this.triple3));
    }

    @Test
    public void testAdd()
    {
        // RAM
        this.tpDictRam.add(this.triple3, 2);
        assertTrue(this.tpDictRam.contains(this.triple3));

        // Disk
        this.tpDictDisk.add(this.triple3, 2);
        assertTrue(this.tpDictDisk.contains(this.triple3));
    }

    @Test
    public void testRemove()
    {
        // RAM
        this.tpDictRam.remove(this.triple2);
        assertFalse(this.tpDictRam.contains(this.triple2));

        // Disk
        this.tpDictDisk.remove(this.triple2);   // Removal from disk is not supported
        assertFalse(this.tpDictDisk.contains(this.triple2));
    }

    @Test
    public void testGet()
    {
        // RAM
        assertEquals((Integer) 0, this.tpDictRam.get(this.triple1));
        assertEquals((Integer) 1, this.tpDictRam.get(this.triple2));
        assertNull(this.tpDictRam.get(this.triple3));

        // Disk
        assertEquals((Integer) 0, this.tpDictDisk.get(this.triple1));
        assertEquals((Integer) 1, this.tpDictDisk.get(this.triple2));
        assertNull(this.tpDictDisk.get(this.triple3));
    }

    @Test
    public void testResetDisk()
    {
        TriplePatternDictionary copy = new TriplePatternDictionary(this.dictDisk, this.tpDictDisk.getNodeDictionary(), this.tpDictDisk.getOrder());
        assertTrue(copy.contains(this.triple1));
        assertTrue(copy.contains(this.triple2));
        assertEquals((Integer) 0, copy.get(this.triple1));
        assertEquals((Integer) 1, copy.get(this.triple2));
    }
}
