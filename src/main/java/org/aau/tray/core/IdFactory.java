package org.aau.tray.core;

public class IdFactory
{
    // TODO: This needs to be assigned the largest existing ID + 1
    private static int counter = 0;

    public static NodeId nodeId()
    {
        return new NodeId(counter++);
    }
}
