package org.aau.tray.system;

import org.aau.tray.store.dictionary.Dictionary;
import org.aau.tray.store.dictionary.TripleDictionary;
import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.graph.Node;

// TODO: Dictionaries should not contain tuple of nodes, but tuple of node IDs, and then use the node dictionary for the conversion
// TODO: Same for triple dictionary
// TODO: I think this ID dictionary component needs an entire rewrite. It is a pretty central elements. It won't harm the TupleTable, which uses this component.
// Order f is the fully concrete triple dictionary
public class IdDictionary
{
    private TripleDictionary tripleDictionary;

    public IdDictionary(TripleDictionary tripleDictionary)
    {
        this.tripleDictionary = tripleDictionary;
    }

    public Dictionary<Node, Integer> nodeDictionary()
    {
        return this.tripleDictionary.getNodeDictionary();
    }

    public Dictionary<Tuple<Node>, Integer> tripleDictionary()
    {
        return this.tripleDictionary;
    }
}
