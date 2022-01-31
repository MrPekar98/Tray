package org.aau.tray.store.index;

import org.aau.tray.core.NodeId;
import org.aau.tray.core.file.FileSet;
import org.aau.tray.store.DataTable;
import org.aau.tray.store.Table;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.atlas.lib.tuple.TupleFactory;
import org.apache.jena.graph.Node;

import java.util.*;

// Indexing over triple patterns with two concrete nodes
// Triple patterns with other than two concrete nodes should consult the ID dictionary first
public class BucketArray extends IndexBase
{
    private int capacity;
    private List<Set<NodeId>> array;
    private Table<Pair<Integer, Set<NodeId>>> table;

    public BucketArray(int initialCapacity, FileSet data)
    {
        this.capacity = Math.max(data.size(), initialCapacity);
        this.array = new ArrayList<>(Math.max(data.size(), initialCapacity));
        this.table = new DataTable(data);
        Collections.fill(this.array, null);
        load();
    }

    // TODO: Load from table content
    private void load()
    {

    }

    private void save(Integer id, Set<NodeId> solutions)
    {
        this.table.add(TupleFactory.tuple(Pair.create(id, solutions)));
    }

    @Override
    protected boolean abstractAdd(Integer id, Set<NodeId> solutions)
    {
        if (id >= this.array.size())
            resize();

        else if (id < 0)
            throw new IllegalArgumentException("IDs cannot be negative");

        save(id, solutions);

        if (this.array.get(id) != null)
        {
            this.array.get(id).addAll(solutions);
            return false;
        }

        this.array.add(id, solutions);
        return true;
    }

    private void resize()
    {
        this.array.addAll(Collections.nCopies(this.array.size() + this.capacity, null));
    }

    // TODO: Remove from disk or RAM too
    @Override
    protected boolean abstractRemove(Integer id)
    {
        if (id < 0)
            throw new IllegalArgumentException("IDs cannot be negative");

        else if (id >= this.array.size())
            throw new IllegalArgumentException("ID too high");

        else if (this.array.get(id) == null)
            return false;

        this.array.remove(id.intValue());
        this.array.add(id, null);
        return true;
    }

    @Override
    protected Iterator<Node> abstractFind(Integer id)
    {
        if (id < 0)
            throw new IllegalArgumentException("IDs cannot be negative");

        else if (id >= this.array.size())
            throw new IllegalArgumentException("ID too high");

        return null;
    }
}
