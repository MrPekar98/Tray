package org.aau.tray.system;

import org.aau.tray.store.data.Disk;
import org.aau.tray.store.data.Fetchable;
import org.aau.tray.store.data.RAM;
import org.aau.tray.store.dictionary.ConcreteTripleDictionary;
import org.aau.tray.store.dictionary.NodeDictionary;
import org.aau.tray.store.dictionary.TriplePatternDictionary;
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
    private static Journal idDictionaryJournal, indexJournal;

    public static void start()
    {
        try
        {
            probs.load(ClassLoader.getSystemResourceAsStream(CONFIG_FILE));
            idDictionaryJournal = new Journal(Path.of(probs.getProperty("journal")), "dictionary");
            indexJournal = new Journal(Path.of(probs.getProperty("journal")), "index");

            String[] orders = indexOrders();
            Fetchable[] memory = new Fetchable[orders.length];

            for (int i = 0; i < orders.length; i++)
            {
                if (probs.getProperty("in_memory").equals("true"))
                    memory[i] = new Disk(new File(probs.getProperty("dictionary_path")), orders[i]);

                else
                    memory[i] = new RAM(orders[i]);
            }

            boolean isInMemory = probs.getProperty("in_memory").equals("true");
            NodeDictionary nodeDictionary = new NodeDictionary(isInMemory ? new RAM(NODE_MEM) :
                    new Disk(new File(probs.getProperty("dictionary_path")), NODE_MEM));
            ConcreteTripleDictionary ctDictionary = new ConcreteTripleDictionary(isInMemory ? new RAM(TRIPLE_MEM) :
                    new Disk(new File(probs.getProperty("dictionary_path")), TRIPLE_MEM), nodeDictionary);
            TriplePatternDictionary[] tpDictionaries = makeTriplePatternDictionaries(probs.getProperty("dictionary_path"),
                    orders, nodeDictionary);
            idDictionary = new IdDictionary(ctDictionary, tpDictionaries, orders);
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

    private static String[] indexOrders()
    {
        String orders = probs.getProperty("order");
        return  orders.split(",");
    }

    private static TriplePatternDictionary[] makeTriplePatternDictionaries(String memoryPath,
                                                                           String[] orders,
                                                                           NodeDictionary nodeDictionary)
    {
        TriplePatternDictionary[] tps = new TriplePatternDictionary[orders.length];

        for (int i = 0; i < orders.length; i++)
        {
            tps[i] = new TriplePatternDictionary(memoryPath != null ?
                     new Disk(new File(memoryPath), orders[i]) : new RAM(orders[i]),
                     nodeDictionary, orders[i]);
        }

        return tps;
    }

    public static IdDictionary getIdDictionary()
    {
        return idDictionary;
    }
}
