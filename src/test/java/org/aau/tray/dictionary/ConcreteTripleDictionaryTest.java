package org.aau.tray.dictionary;

import org.aau.tray.store.data.Disk;
import org.aau.tray.store.data.RAM;
import org.aau.tray.store.dictionary.ConcreteTripleDictionary;
import org.aau.tray.store.dictionary.NodeDictionary;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ConcreteTripleDictionaryTest
{
    private Disk d = new Disk(new File(".")), dDict = new Disk(new File("."), "dict");
    private ConcreteTripleDictionary dictRam, dictDisk;
    private Node n11 = NodeFactory.createBlankNode("b1"),
                    n12 = NodeFactory.createURI("uri1"),
                    n13 = NodeFactory.createLiteral("lit1"),
                    n21 = NodeFactory.createBlankNode("b2"),
                    n22 = NodeFactory.createURI("uri2"),
                    n23 = NodeFactory.createLiteral("lit2"),
                    n31 = NodeFactory.createBlankNode("b3"),
                    n32 = NodeFactory.createURI("uri3"),
                    n33 = NodeFactory.createLiteral("lit3");
    private Tuple<Node> triple1 = TupleFactory.tuple(this.n11, this.n12, this.n13),
                        triple2 = TupleFactory.tuple(this.n21, this.n22, this.n23),
                        triple3 = TupleFactory.tuple(this.n31, this.n32, this.n33);

    @Before
    public void init()
    {
        RAM r = new RAM("test");
        NodeDictionary nodesDisk = new NodeDictionary(this.dDict);
        NodeDictionary nodesRam = new NodeDictionary(new RAM("nodes"));
        nodesDisk.add(this.n11, 0);
        nodesDisk.add(this.n12, 1);
        nodesDisk.add(this.n13, 2);
        nodesDisk.add(this.n21, 3);
        nodesDisk.add(this.n22, 4);
        nodesDisk.add(this.n23, 5);
        nodesDisk.add(this.n31, 6);
        nodesDisk.add(this.n32, 7);
        nodesDisk.add(this.n33, 8);
        nodesRam.add(this.n11, 0);
        nodesRam.add(this.n12, 1);
        nodesRam.add(this.n13, 2);
        nodesRam.add(this.n21, 3);
        nodesRam.add(this.n22, 4);
        nodesRam.add(this.n23, 5);
        nodesRam.add(this.n31, 6);
        nodesRam.add(this.n32, 7);
        nodesRam.add(this.n33, 8);

        this.dictRam = new ConcreteTripleDictionary(r, nodesRam);
        this.dictDisk = new ConcreteTripleDictionary(this.d, nodesDisk);
        this.dictRam.add(this.triple1, 0);
        this.dictRam.add(this.triple2, 1);
        this.dictDisk.add(this.triple1, 0);
        this.dictDisk.add(this.triple2, 1);
    }

    @After
    public void tearDown()
    {
        this.d.clear();
        this.dDict.clear();
    }

    @Test
    public void testContains()
    {
        // RAM
        assertTrue(this.dictRam.contains(this.triple1));
        assertTrue(this.dictRam.contains(this.triple2));
        assertFalse(this.dictRam.contains(this.triple3));

        // Disk
        assertTrue(this.dictDisk.contains(this.triple1));
        assertTrue(this.dictDisk.contains(this.triple2));
        assertFalse(this.dictDisk.contains(this.triple3));
    }

    @Test
    public void testAdd()
    {
        // RAM
        this.dictRam.add(this.triple3, 2);
        assertTrue(this.dictRam.contains(this.triple3));

        // Disk
        this.dictDisk.add(this.triple3, 2);
        assertTrue(this.dictRam.contains(this.triple3));
    }

    @Test
    public void testRemove()
    {
        // RAM
        this.dictRam.remove(this.triple1);
        assertFalse(this.dictRam.contains(this.triple1));

        // Disk
        this.dictDisk.remove(this.triple1);     // But it is not deleted from disk as it's not supported
        assertFalse(this.dictDisk.contains(this.triple1));
    }

    @Test
    public void testGet()
    {
        // RAM
        assertEquals((Integer) 0, this.dictRam.get(this.triple1));
        assertEquals((Integer) 1, this.dictRam.get(this.triple2));
        assertNull(this.dictRam.get(this.triple3));

        // Disk
        assertEquals((Integer) 0, this.dictDisk.get(this.triple1));
        assertEquals((Integer) 1, this.dictDisk.get(this.triple2));
        assertNull(this.dictDisk.get(this.triple3));
    }

    @Test
    public void testResetDisk()
    {
        ConcreteTripleDictionary copy = new ConcreteTripleDictionary(this.d, this.dictDisk.getNodeDictionary());
        assertTrue(copy.contains(this.triple1));
        assertTrue(copy.contains(this.triple2));
        assertEquals((Integer) 0, copy.get(this.triple1));
        assertEquals((Integer) 1, copy.get(this.triple2));
    }
}
