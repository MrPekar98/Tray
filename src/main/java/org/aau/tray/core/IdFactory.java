package org.aau.tray.core;

import org.aau.tray.system.System;

public class IdFactory
{
    // TODO: Find a way to initialize this to the highest ID + 1
    private static int counter = 0;

    public static NodeId nodeId()
    {
        return new NodeId(counter++);
    }
}
