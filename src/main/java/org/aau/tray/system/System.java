package org.aau.tray.system;

import org.aau.tray.store.data.Disk;
import org.aau.tray.store.data.Fetchable;
import org.aau.tray.store.data.RAM;
import org.aau.tray.system.journal.Journal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class System
{
    private static final Properties probs = new Properties();
    private static final String CONFIG_FILE = "configuration.config";
    private static IdDictionary idDictionary;
    private static final String NODE_MEM = "node";
    private static final String TRIPLE_MEM = "triple";
    private static Journal dictionaryJournal, indexJournal;

    public static void start()
    {
        try
        {
            probs.load(ClassLoader.getSystemResourceAsStream(CONFIG_FILE));
            dictionaryJournal = new Journal(Path.of(probs.getProperty("journal")), "dictionary");
            indexJournal = new Journal(Path.of(probs.getProperty("journal")), "index");

            List<String> orders = indexOrders();
            Fetchable[] memory = new Fetchable[orders.size()];

            for (int i = 0; i < orders.size(); i++)
            {
                if (probs.getProperty("in_memory").equals("true"))
                    memory[i] = new Disk(new File(probs.getProperty("dictionary_path")), orders.get(i));

                else
                    memory[i] = new RAM(orders.get(i));
            }

            boolean isInMemory = probs.getProperty("in_memory").equals("true");
            idDictionary = new IdDictionary(orders, memory, isInMemory ? new RAM(TRIPLE_MEM) : new Disk(new File(probs.getProperty("dictionary_path")), TRIPLE_MEM),
                    probs.getProperty("in_memory").equals("true") ? new RAM(NODE_MEM) : new Disk(new File(probs.getProperty("dictionary_path")), NODE_MEM));
        }

        catch (IOException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
    }

    public static void stop()
    {
        probs.clear();
    }

    private static List<String> indexOrders()
    {
        String orders = probs.getProperty("order");
        return  List.of(orders.split(","));
    }

    public static IdDictionary getIdDictionary()
    {
        return idDictionary;
    }
}
