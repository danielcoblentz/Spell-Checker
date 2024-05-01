import java.io.*;
import java.util.Scanner;

/*note* most of the code below is from the first part of the mini-term project */

public class TrieSearch {
    public static void main(String[] args) {
        String dictionaryFileName = "dictionary.txt";
        String documentFileName = "testfile.txt";
        TST tst = new TST(); // initializes TST class to create a new object

        // Load words from a dictionary into the TST
        try (Scanner inputStream = new Scanner(new File(dictionaryFileName))) {
            while (inputStream.hasNextLine()) {
                String word = inputStream.nextLine().toLowerCase(); 
                tst.add(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary file not found"); 
            System.exit(0);
        }

        // Open and process a document file same as part 1
        try (Scanner inputStream = new Scanner(new File(documentFileName))) {
            int lineNumber = 0; 
            while (inputStream.hasNextLine()) {
                lineNumber++; 
                String line = inputStream.nextLine().toLowerCase();
                String[] words = line.split("\\W+");

                for (int i = 0; i < words.length; i++) {
                    if (!words[i].isEmpty() && !tst.contains(words[i])) {
                        System.out.println(words[i] + " is not found in the dictionary at line #" + lineNumber); // prints out the mispelled word and the corresponding line # to the user
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Document file not found"); 
            System.exit(0);
        }
    }
}

// implementation of  ternary search trie data structure.
class TST
{
    //initialized root
    Node root;

private class Node {
    char c;                      // the character stored in this node
    Node left, mid, right;       // pointers to left, middle, and right child nodes
    boolean end;                 //flag to indicate if this node marks the end of a worc


    // constructor for Node class including char c parameter
    public Node(char c) {
        this.c = c;
    }
}
 //  method to check if the trie contains a specific string
public boolean contains(String s)
{

    return contains(root, s.toLowerCase(), 0); // start the search from the root making the string lowercase
}
// Recursive method to check if a string is in the trie, navigating through nodes based on character comparison
private boolean contains(Node x, String s, int d)
{
    if (x == null) return false; // if node is null the string is not found
    char c = s.charAt(d);
    if (c < x.c) return contains(x.left, s, d); // character is less than current node's character then search left
    else if (c > x.c) return contains(x.right, s, d); // same as one above just searching right not left
    else if (d < s.length()-1) return contains(x.mid, s, d+1);  // character matches and not at the end of the string search the middle
    else return x.end;  // at the end of the string return true if it's marked as an end of a word
}
        //method to add a string to the trie.
    public void add(String s)
    { root = add(root, s.toLowerCase(), 0); } // start by adding from the root making the given string lowercase


        // recursive method to add a string to the trie creating new nodes as necessary in the trie
    private Node add(Node x, String s, int d)
    {
        char c = s.charAt(d);           // retrieve the character at the current depth in the string
        if (x == null) x = new Node(c);             // if there is no node create a new one with the current character
        if (c < x.c) x.left = add(x.left, s, d);            // character less than current nodes character add to left
        else if (c > x.c) x.right = add(x.right, s, d);             // character is grater than current node add to right
        else if (d < s.length()-1) x.mid = 	add(x.mid, s, d+1);             // character matches and not at the end of a string add to middle
        else x.end = true;          // mark as end of the string mark node as end of a word
        return x;       //return the modified node
    }

}
