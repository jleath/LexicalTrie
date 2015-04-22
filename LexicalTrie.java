import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.LinkedList;

/** A Trie data structure that supports ordering by a user-defined
 *  lexicographic ordering.  Supports insertion, searching, and 
 *  retrieval of a Collection of the strings contained in the trie.
 *
 *  The lexicographic ordering can be passed into the constructor
 *  as such: abcdefghijklmnopqrstuvwxyz.
 *  
 *  If the zero argument constructor is used, the ordering will default
 *  to the standard ordering.
 *
 *  The order is maintained because the trie uses a LinkedHashMap to
 *  store characters in the trie.  This means that the insertion order
 *  is maintained.  Each new node of the trie is initialized with the
 *  custom ordering.
 *
 *  Because each node's hashmap is initialized with a mapping to null for
 *  each character in the given alphabet, using a hashmap structure doesn't
 *  really save any memory vs. using an array.  This could have also been
 *  done with a ternary search tree and a new Comparator, but I like this
 *  implementation better even though it is a little more wasteful.
 *
 *  @author Joshua Leath
 */

public class LexicalTrie {
    /** The root of this trie. */
    private Node root;
    /** The number of words stored in this trie. */
    private int numWords;
    /** An optional ordering for the trie to retrieve words in a given
     *  lexographical order. */
    private final String ordering;
    
    /** A constructor for use with unordered tries. */
    public LexicalTrie() {
        this(null);
    }

    /** A constructor for use with ordered tries, ORDERING should be a permutation
     *  of an alphabet that represents the desired ordering of its letters. */
    public LexicalTrie(String ordering) {
        root = null;
        numWords = 0;
        this.ordering = ordering;
    }

    /** If S is a full word or a prefix, in this trie and ISFULLWORD equals true, 
     * this will return true. If ISFULLWORD equals false, this will only return true
     * if S is stored in this trie as a full word and not just a prefix. */
    public boolean find(String s, boolean isFullWord) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return find(root, s, 0, isFullWord);
    }

    /** Returns true iff S is contained in this trie either as a prefix or a full word. */
    public boolean isPrefix(String s) {
        return find(s, false);
    }

    /** Inserts the string s into this trie as a full word. */
    public void insert(String s) {
        insert(s, false);
    }

    /** Inserts the string S into the trie as a prefix. */
    public void insertPrefix(String s) {
        insert(s, true);
    }

    /** Inserts the string S into the node N as a prefix. */
    public Node insertPrefix(Node n, String s) {
        return insert(n, s, 0, true); 
    }

    /** Returns a collection of all the strings stored in this trie. */
    public Collection<String> getStrings() {
        return getStrings(root, "");
    }

    /** Inserts S into the Trie, if ISPREFIX is true, inserts the word
     *  as a prefix, otherwise inserts S as a full word. */
    private void insert(String s, boolean isPrefix) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Cannot add null or empty string to Trie.");
        }
        root = insert(root, s, 0, isPrefix);
        numWords += 1;
    }

    /** Inserts the substring of S from positions [d, s.length()) into
     *  the node N and then returns N.  If ISPREFIX is true, inserts 
     *  the word as a prefix, otherwise inserts S as a full word. */
    private Node insert(Node n, String s, int d, boolean isPrefix) {
        if (n == null) {
            n = new Node();
        }
        if (d == s.length()) {
            n.exists = ! isPrefix;
            return n;
        }
        char toInsert = s.charAt(d);
        n.put(toInsert, insert(n.getNode(s.charAt(d)), s, d+1, isPrefix));
        return n;
    }

    /** Returns a collection of all the strings stored in this trie, with the
     *  string KEY prepended onto each one. */
    private Collection<String> getStrings(Node n, String key) {
        LinkedList<String> result = new LinkedList<String>();
        if (n == null) {
            return result;
        }
        if (numWords == 0) {
            return result;
        }
        for (char c : n.nodes.keySet()) {
            Node cNode = n.getNode(c);
            if (cNode != null && cNode.exists) {
                result.add(key + c); 
            }
            result.addAll(getStrings(n.getNode(c), key + c));
        }
        return result;
    }

    /** Returns true if the substring of S from [d, s.length()) is contained in the trie N. 
     *  If ISFULLWORD is false, it will return true whether S is stored as a prefix or a 
     *  full word.  Otherwise, it will return true only if S is stored as a full word. */
    private boolean find(Node n, String s, int d, boolean isFullWord) {
        if (n == null) {
            return false;
        }
        if (d == s.length()) {
            if (isFullWord) {
                return n.exists;
            }
            return true;
        }

        char toFind = s.charAt(d);
        if (n.contains(toFind)) {
            return find(n.getNode(toFind), s, d+1, isFullWord);
        }
        return false;
    }

    /** An inner class to represent a node in a trie. */
    private class Node {
        /** True iff this node is a terminal node in a trie, meaning that this
         *  node represents a character at the end of an existing string. */
        boolean exists;
        /** An ordered map from a character to another node. */
        LinkedHashMap<Character, Node> nodes;

        private Node() {
            exists = false;
            nodes = new LinkedHashMap<Character, Node>();
            if (ordering != null) {
                for (int i = 0; i < ordering.length(); i++) {
                    this.nodes.put(ordering.charAt(i), null);
                }
            }
        }

        /** Returns true iff this node contains a mapping for C, even if
         *  that mapping points to null. */
        private boolean contains(char c) {
            return nodes.containsKey(c);
        }

        /** Add a character to this nodes hashmap. */
        private void addChar(char c) {
            nodes.put(c, new Node());
        }

        /** Returns the node associated with C in this node's hashmap. */
        private Node getNode(char c) {
            return nodes.get(c);
        }

        /** Creates a new mapping from the character C to the node N. */
        private void put(char c, Node n) {
            nodes.put(c, n);
        }
    }
}
