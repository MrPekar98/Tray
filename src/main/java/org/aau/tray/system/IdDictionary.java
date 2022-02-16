package org.aau.tray.system;

import org.aau.tray.store.dictionary.ConcreteTripleDictionary;
import org.aau.tray.store.dictionary.NodeDictionary;
import org.aau.tray.store.dictionary.TriplePatternDictionary;

import java.util.List;

// TODO: Dictionaries should not contain tuple of nodes, but tuple of node IDs, and then use the node dictionary for the conversion
// TODO: Same for triple dictionary
// TODO: I think this ID dictionary component needs an entire rewrite. It is a pretty central elements. It won't harm the TupleTable, which uses this component.
// Order f is the fully concrete triple dictionary
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

    public TriplePatternDictionary getTriplePatternDictionary(String order)
    {
        int idx = this.orders.indexOf(order);
        return this.triplePatternDictionaries[idx];
    }
}
