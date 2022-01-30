package org.aau.tray.store;

import org.aau.tray.store.data.Disk;
import org.aau.tray.store.data.Fetchable;
import org.aau.tray.store.data.RAM;
import org.aau.tray.store.index.BucketArray;
import org.aau.tray.store.index.Index;
import org.aau.tray.store.tupletable.TupleTable;

import java.util.List;

public class Factory
{
    public static BucketArray bucketArray(int initialCapacity, boolean mem)
    {
        Fetchable data = mem ? new RAM() : new Disk();
        return new BucketArray(initialCapacity, data);
    }

    public static TupleTable tupleTable(Index<Integer>[] indexes, List<String> orders)
    {
        return new TupleTable(indexes, orders);
    }

    public static Graph defaultGraph(boolean mem)
    {
        if (mem)
            return new GraphMem();

        return new GraphNative();
    }

    public static Graph namedGraphs()
    {
        return new NamedGraphs();
    }
}
