package org.aau.tray.store.data;

public interface Fetchable
{
    void clear();
    int write(int offset, String data);
    String read(int offset);
    int size();
    void delete(int offset);
}
