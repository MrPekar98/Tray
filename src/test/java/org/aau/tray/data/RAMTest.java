package org.aau.tray.data;

import org.aau.tray.store.data.RAM;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RAMTest
{
    @Test
    public void testWrite()
    {
        RAM ram = new RAM("test");
        assertEquals(6, ram.write(0, "Test 1"));
        assertEquals(6, ram.write(1, "Test 2"));
        assertEquals(7, ram.write(2, "Test 22"));
        assertEquals(7, ram.write(3, "Test 32"));
        assertEquals(7, ram.write(2, "Test 12"));
    }

    @Test
    public void testRead()
    {
        RAM ram = new RAM("test");
        ram.write(0, "Test 1");
        ram.write(1, "Test 2");
        ram.write(2, "Test 22");
        ram.write(3, "Test 32");
        ram.write(2, "Test 12");

        assertEquals("Test 1", ram.read(0));
        assertEquals("Test 2", ram.read(1));
        assertEquals("Test 22", ram.read(3));
        assertEquals("Test 32", ram.read(4));
        assertEquals("Test 12", ram.read(2));
    }
}
