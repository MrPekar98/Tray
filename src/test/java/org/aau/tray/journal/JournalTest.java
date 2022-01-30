package org.aau.tray.journal;

import org.aau.tray.system.journal.Journal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class JournalTest
{
    private final File f = new File("test-journal.dat");
    private final Journal journal = new Journal(Path.of(""), "test");

    @Before
    public void init() throws IOException
    {
        journal.check(List.of(1, 2, 3, 4, 5));
    }

    @After
    public void clean()
    {
        this.f.delete();
    }

    @Test
    public void testCheck()
    {
        assertTrue(this.f.exists());
    }

    @Test
    public void testRead()
    {
        try
        {
            assertEquals(List.of(1, 2, 3, 4, 5), journal.readLast());
        }

        catch (IOException exc)
        {
            fail(exc.getMessage());
        }
    }
}
