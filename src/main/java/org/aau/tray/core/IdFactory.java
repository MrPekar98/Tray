package org.aau.tray.core;

import org.aau.tray.system.System;

public class IdFactory
{
    private static int counter = System.getIdDictionary().highestId().id() + 1;

    public static NodeId nodeId()
    {
        return new NodeId(counter++);
    }
}
