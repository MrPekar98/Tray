package org.aau.tray.store;

import org.aau.tray.core.NodeId;
import org.aau.tray.core.file.FileSet;
import org.aau.tray.store.data.Fetchable;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

// A 1-dimension table of pairs
// The first of a pair is the pattern ID and the second of a pair is the set of solution IDs of the pattern
public class DataTable extends Table<Pair<Integer, Set<NodeId>>>
{
    private List<Fetchable> files;
    private List<String> fileNames;

    public DataTable(FileSet data)
    {
        super(1);
        this.files = new ArrayList<>(data.size());
        this.fileNames = new ArrayList<>(data.size());

        for (Fetchable file : data)
        {
            this.files.add(file);
            this.fileNames.add(file.toString());
        }
    }

    @Override
    protected void abstractAdd(Tuple<Pair<Integer, Set<NodeId>>> record)
    {

    }

    @Override
    protected void abstractDelete(Tuple<Pair<Integer, Set<NodeId>>> record)
    {

    }

    @Override
    protected void abstractDelete(int row)
    {

    }

    @Override
    protected Pair<Integer, Set<NodeId>> abstractGet(int row)
    {
        return null;
    }

    @Override
    protected Pair<Integer, Set<NodeId>> abstractGet(Predicate predicate)
    {
        return null;
    }

    @Override
    protected int abstractSize()
    {
        return 0;
    }
}
