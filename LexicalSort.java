/** This program allows the user to define a lexicographic ordering for
 *  characters, and then sort a set of strings to match that ordering.
 *  The ordering should be entered into the standard input first,
 *  (ex. "abcdefghijklmnopqrstuvwxyz") and the strings to be sorted
 *  should follow.  This class sorts the strings by inserting them into
 *  a specialized trie structure that maintains a sorted order of the
 *  strings for retrieval using a LinkedHashMap. This is primarily just
 *  a testing client for the Trie structure that I have named LexicalTrie.  
 *  I'm sure somebody else has implemented something similar before, but I 
 *  can't find anything similar so I'm naming it myself. Deal with it.
 *  @author Joshua Leath
 */
import java.util.Scanner;

public class LexicalSort {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        if (! in.hasNext()) {
            throw new IllegalArgumentException("No words or alphabet in input.");
        }
        String alphabet = in.next();
        if (hasDuplicates(alphabet)) {
            throw new IllegalArgumentException("A letter appears multiple times " +
                    " in the alphabet.");
        }
        LexicalTrie dict = new LexicalTrie(alphabet);
        while (in.hasNext()) {
            dict.insert(in.next());
        }
        for (String s : dict.getStrings()) {
            System.out.println(s);
        }
        return;
    }

    /** Returns true if the string S has any duplicate characters,
     *  I'm not too worried about the runtime of this method because
     *  it is meant to be used only for alphabets and no sensible alphabet
     *  will have enough characters to cause issues here. */
    private static boolean hasDuplicates(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            for (int j = i + 1; j < s.length(); j++) {
                if (c == s.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }
}
