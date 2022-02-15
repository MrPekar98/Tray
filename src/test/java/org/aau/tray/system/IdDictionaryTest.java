package org.aau.tray.system;

import org.aau.tray.store.data.Fetchable;
import org.junit.After;
import org.junit.Before;

import java.util.List;

public class IdDictionaryTest
{
    private IdDictionary dictionaryDisk, dictionaryInMemory;
    private List<String> orders = List.of("spo", "pos", "ops");

    @Before
    public void init()
    {
        this.dictionaryDisk = new IdDictionary(this.orders, new Fetchable[]{})
    }

    @After
    public void tearDown()
    {

    }
}
