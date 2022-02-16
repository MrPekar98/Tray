package org.aau.tray.store.dictionary;

import org.aau.tray.store.data.Fetchable;
import org.aau.tray.store.data.RAM;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.atlas.lib.tuple.TupleFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TripleDictionaryMemory implements DictionaryMemory<Tuple<Integer>>
{
    private Fetchable mem;

    public TripleDictionaryMemory(Fetchable memory)
    {
        this.mem = memory;
    }

    @Override
    public void save(Tuple<Integer> t, Integer id)
    {
        if (t.len() == 3)
            this.mem.write(id, t.get(0) + "--" + t.get(1) + "--" + t.get(2) + "--" + id);
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
        List<Pair<Tuple<Integer>, Integer>> triples = new ArrayList<>();
        int offset = 0;

        try
        {
            while (true)
            {
                Pair<Tuple<Integer>, Integer> p = readTriple(offset++);
                triples.add(p);
            }
        }

        catch (Exception exc)
        {
            return triples.iterator();
        }
    }

    private Pair<Tuple<Integer>, Integer> readTriple(int offset)
    {
        String[] data = this.mem.read(offset).split("--");
        Integer id = Integer.parseInt(data[3]);

        return Pair.create(TupleFactory.tuple(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2])), id);
    }
}
