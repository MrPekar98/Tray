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

public class TriplePatternDictionaryMemory
{
    private Disk d = new Disk(new File("."));
    private TripleDictionaryMemory tRam, tDisk;
    private Tuple<Integer> triple1 = TupleFactory.tuple(1, 2, 3),
                            triple2 = TupleFactory.tuple(3, 5, 4),
                            triple3 = TupleFactory.tuple(4, 2, 1);

    @Before
    public void init()
    {
        this.tRam = new TripleDictionaryMemory(new RAM("test"));
        this.tDisk = new TripleDictionaryMemory(this.d);
        this.tRam.save(this.triple1, 0);
        this.tRam.save(this.triple2, 1);
        this.tRam.save(this.triple3, 2);
        this.tDisk.save(this.triple1, 0);
        this.tDisk.save(this.triple2, 1);
        this.tDisk.save(this.triple3, 2);
    }

    @After
    public void tearDown()
    {
        this.d.clear();
    }

    private void testIterator(TripleDictionaryMemory tdMem, Pair<Tuple<Integer>, Integer> ... pairs)
    {
        Iterator<Pair<Tuple<Integer>, Integer>> iter = tdMem.iterator();
        List<Pair<Tuple<Integer>, Integer>> l = new ArrayList<>(pairs.length);
        iter.forEachRemaining(l::add);

        for (Pair<Tuple<Integer>, Integer> pair : pairs)
        {
            assertTrue(l.contains(pair));
        }
    }

    @Test
    public void testContains()
    {
        // RAM
        testIterator(this.tRam, Pair.create(this.triple1, 0),
                        Pair.create(this.triple2, 1),
                        Pair.create(this.triple3, 2));

        // Disk
        testIterator(this.tDisk, Pair.create(this.triple1, 0),
                        Pair.create(this.triple2, 1),
                        Pair.create(this.triple3, 2));
    }

    @Test
    public void testDelete()
    {
        // RAM
        this.tRam.delete(1);
        testIterator(this.tRam, Pair.create(this.triple1, 0), Pair.create(this.triple3, 2));

        // Disk
        this.tDisk.delete(1);   // Delete from disk is not supported
        testIterator(this.tDisk, Pair.create(this.triple1, 0),
                        Pair.create(this.triple2, 1),
                        Pair.create(this.triple3, 2));
    }

    @Test
    public void testResetDisk()
    {
        TripleDictionaryMemory copy = new TripleDictionaryMemory(this.d);
        testIterator(copy, Pair.create(this.triple1, 0),
                        Pair.create(this.triple2, 1),
                        Pair.create(this.triple3, 2));
    }
}
