package org.aau.tray.store.dictionary;

public interface Dictionary<K, V>
{
    boolean contains(K key);
    void add(K key, V value);
    void remove(K key);
    V get(K key);
}
