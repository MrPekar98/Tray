package org.aau.tray.system.journal;

import java.io.IOException;

public interface Checkpointer
{
    // Checkpointing method
    void check(Object obj) throws IOException;
    void clear();
    Object readLast() throws IOException;
}
