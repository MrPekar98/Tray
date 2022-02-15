package org.aau.tray.store.dictionary;

import org.apache.jena.atlas.lib.Pair;

public interface DictionaryMemory<E> extends Iterable<Pair<E, Integer>>
{
    void save(E element, Integer id);
    void delete(Integer id);
}
