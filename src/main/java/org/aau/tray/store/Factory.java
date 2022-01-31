package org.aau.tray.store;

import org.aau.tray.core.file.FileSet;
import org.aau.tray.store.data.Fetchable;
import org.aau.tray.store.index.BucketArray;
import org.aau.tray.store.index.Index;
import org.aau.tray.store.tupletable.TupleTable;

import java.util.List;

public class Factory
{
    public static FileSet fileSet(Fetchable ... files)
    {
        return new FileSet(files);
    }

    public static BucketArray bucketArray(int initialCapacity, FileSet data)
    {
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
