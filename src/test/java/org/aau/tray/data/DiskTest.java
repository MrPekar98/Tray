package org.aau.tray.data;

import org.aau.tray.store.data.Disk;
import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class DiskTest
{
    private static final File PATH = new File("./");

    @After
    public void cleanup()
    {
        (new Disk(PATH)).clear();
    }

    @Test
    public void testWrite()
    {
        Disk d = new Disk(PATH);
        assertEquals(9, d.write(0, "Test 1"));
        assertEquals(9, d.write(1, "Test 2"));
        assertEquals(10, d.write(2, "Test 22"));
        assertEquals(10, d.write(3, "Test 32"));
        assertEquals(10, d.write(2, "Test 12"));
    }

    @Test
    public void testRead()
    {
        Disk d = new Disk(PATH);
        d.write(0, "Test 1");
        d.write(1, "Test 2");
        d.write(2, "Test 22");
        d.write(3, "Test 32");
        d.write(2, "Test 12");

        assertEquals("Test 1", d.read(0));
        assertEquals("Test 2", d.read(1));
        assertEquals("Test 22", d.read(3));
        assertEquals("Test 32", d.read(4));
        assertEquals("Test 12", d.read(2));
    }

    @Test
    public void testLoad()
    {
        Disk d = new Disk(PATH);
        d.write(0, "Test 1");
        d.write(1, "Test 2");
        d.write(2, "Test 22");
        d.write(3, "Test 32");
        d.write(2, "Test 12");

        Disk dCopy = new Disk(PATH);
        assertEquals("Test 1", dCopy.read(0));
        assertEquals("Test 2", dCopy.read(1));
        assertEquals("Test 22", dCopy.read(3));
        assertEquals("Test 32", dCopy.read(4));
        assertEquals("Test 12", dCopy.read(2));
    }
}
