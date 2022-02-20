package org.aau.tray.dictionary;

import org.aau.tray.store.data.Disk;
import org.aau.tray.store.data.RAM;
import org.aau.tray.store.dictionary.TripleDictionaryMemory;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TripleDictionaryMemoryTest
{
    private Disk d = new Disk(new File("."));
    private TripleDictionaryMemory tMemRam, tMemDisk;
    private Tuple<Integer> triple1 = TupleFactory.tuple(1, 2, 3),
                            triple2 = TupleFactory.tuple(3, 5, 4),
                            triple3 = TupleFactory.tuple(4, 2, 1);

    @Before
    public void init()
    {
        this.tMemRam = new TripleDictionaryMemory(new RAM("test"));
        this.tMemDisk = new TripleDictionaryMemory(this.d);
        this.tMemRam.save(this.triple1, 0);
        this.tMemRam.save(this.triple2, 1);
        this.tMemRam.save(this.triple3, 2);
        this.tMemDisk.save(this.triple1, 0);
        this.tMemDisk.save(this.triple2, 1);
        this.tMemDisk.save(this.triple3, 2);
    }

    @After
    public void tearDown()
    {
        this.d.clear();
    }

    private void testIterator(TripleDictionaryMemory tdm, Pair<Tuple<Integer>, Integer> ... triples)
    {
        Iterator<Pair<Tuple<Integer>, Integer>> iter = tdm.iterator();
        List<Pair<Tuple<Integer>, Integer>> pairs = new ArrayList<>(triples.length);
        iter.forEachRemaining(pairs::add);

        for (Pair<Tuple<Integer>, Integer> triple : triples)
        {
            assertTrue(pairs.contains(triple));
        }
    }

    @Test
    public void testContains()
    {
        // RAM
        testIterator(this.tMemRam, Pair.create(this.triple1, 0),
                                    Pair.create(this.triple2, 1),
                                    Pair.create(this.triple3, 2));

        // Disk
        testIterator(this.tMemDisk, Pair.create(this.triple1, 0),
                                    Pair.create(this.triple2, 1),
                                    Pair.create(this.triple3, 2));
    }

    @Test
    public void testDelete()
    {
        // RAM
        this.tMemRam.delete(1);
        testIterator(this.tMemRam, Pair.create(this.triple1, 0), Pair.create(this.triple3, 2));

        // Disk
        this.tMemDisk.delete(1);    // Delete is not supported for disk
        testIterator(this.tMemDisk, Pair.create(this.triple1, 0),
                                    Pair.create(this.triple2, 1),
                                    Pair.create(this.triple3, 2));
    }

    @Test
    public void testResetDisk()
    {
        TripleDictionaryMemory copy = new TripleDictionaryMemory(this.d);
        testIterator(copy, Pair.create(this.triple1, 0), Pair.create(this.triple2, 1), Pair.create(this.triple3, 2));
    }
}
