package org.aau.tray.store.dictionary;

import org.aau.tray.store.data.Fetchable;
import org.aau.tray.store.data.RAM;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TriplePatternDictionaryMemory implements DictionaryMemory<Tuple<Integer>>
{
    private Fetchable mem;

    public TriplePatternDictionaryMemory(Fetchable memory)
    {
        this.mem = memory;
    }

    @Override
    public void save(Tuple<Integer> t, Integer id)
    {
        if (t.len() == 2 || t.len() == 3)
            this.mem.write(id, t.get(0) + "--" + t.get(1) + "--" + id);
    }

    @Override
    public void delete(Integer id)
    {
        if (this.mem instanceof RAM)
            this.mem.delete(id);
    }

    // TODO: This is not feasible. We cannot read everything into RAM.
    @Override
    public Iterator<Pair<Tuple<Integer>, Integer>> iterator()
    {
        List<Pair<Tuple<Integer>, Integer>> patterns = new ArrayList<>();
        int offset = 0;

        try
        {
            while (true)
            {
                Pair<Tuple<Integer>, Integer> p = readPattern(offset++);
                patterns.add(p);
            }
        }

        catch (Exception exc)
        {
            return patterns.iterator();
        }
    }

    private Pair<Tuple<Integer>, Integer> readPattern(int offset)
    {
        String[] data = this.mem.read(offset).split("--");
        Integer id = Integer.parseInt(data[2]);
        return Pair.create(TupleFactory.tuple(Integer.parseInt(data[0]), Integer.parseInt(data[1])), id);
    }
}