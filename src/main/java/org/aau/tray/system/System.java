package org.aau.tray.system;

import org.aau.tray.system.journal.Journal;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class System
{
    private static final Properties probs = new Properties();
    private static final String CONFIG_FILE = "configuration.config";
    private static IdDictionary idDictionary;
    private static Journal dictionaryJournal, indexJournal;

    public static void start()
    {
        try
        {
            probs.load(ClassLoader.getSystemResourceAsStream(CONFIG_FILE));
            idDictionary = new IdDictionary(indexOrders());
            dictionaryJournal = new Journal(Path.of(probs.getProperty("journal")), "dictionary");
            indexJournal = new Journal(Path.of(probs.getProperty("journal")), "index");
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
