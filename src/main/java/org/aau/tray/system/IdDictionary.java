package org.aau.tray.system;

import org.aau.tray.store.dictionary.ConcreteTripleDictionary;
import org.aau.tray.store.dictionary.NodeDictionary;
import org.aau.tray.store.dictionary.TriplePatternDictionary;

import java.util.List;

// Collection of dictionaries necessary for indexing
public class IdDictionary
{
    private ConcreteTripleDictionary tripleDictionary;
    private TriplePatternDictionary[] triplePatternDictionaries;
    private List<String> orders;

    public IdDictionary(ConcreteTripleDictionary tripleDictionary, TriplePatternDictionary[] triplePatternDictionaries, String[] orders)
    {
        this.tripleDictionary = tripleDictionary;
        this.triplePatternDictionaries = triplePatternDictionaries;
        this.orders = List.of(orders);
    }

    public NodeDictionary nodeDictionary()
    {
        return this.tripleDictionary.getNodeDictionary();
    }

    public ConcreteTripleDictionary tripleDictionary()
    {
        return this.tripleDictionary;
    }

    public TriplePatternDictionary triplePatternDictionary(String order)
    {
        int idx = this.orders.indexOf(order);
        return this.triplePatternDictionaries[idx];
    }
}
