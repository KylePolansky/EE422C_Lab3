/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Kyle Polansky>
 * <KPP446>
 * <16480>
 * <Nicole Muzquiz>
 * <ngm339>
 * <16460>
 * Slip days used: <0>
 * Git URL: https://github.com/KylePolansky/EE422C_Lab3
 * Fall 2016
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		
		// TODO methods to read in words, output ladder
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		// TO DO
		return null;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}

	private static LinkedList<String>[] makeAdjacency(Set<String> inputSet){
		if (inputSet == null) {
			throw new NullPointerException("inputSet");
		}
		if (inputSet.size() > 0) {
			throw new IllegalArgumentException("inputSet length is 0");
		}
		LinkedList<String>[] adjList = new LinkedList[inputSet.size()];

		String[] inputArray = (String[]) inputSet.toArray();
		for (int i = 0; i < inputSet.size(); i++) {
			//Make new linked list for current element
			LinkedList<String> current = new LinkedList<String>();
			String currentWord = inputArray[i];
			current.add(inputArray[i]);

			//Go through existing words and see if any match
			for (int x = 0; x < i; x++) {
				LinkedList<String> previous = adjList[x];
				if (hammingDistanceOne(current.getFirst(),previous.getFirst())) {
					previous.add(currentWord);
					current.add(previous.getFirst());
				}
			}
			adjList[i] = current;
		}

		return adjList;
	}
    
	public static Set<String> makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	/**
	 * This method compares 2 words and computes their hammingdistance
	 * @param start is the starting word of the word ladder
	 * @param word is a word from the dictionary we're comparing to
	 * @return true if the difference between two words 1 letter, false otherwise
	 */
	public static boolean hammingDistanceOne(String start, String word){
		int dist = 0;
		if (start.length() != word.length()){ //strings must be the same length in order to work
			return false;
		}
		for(int x = 0; x<start.length(); x++){
			if(start.charAt(0) != word.charAt(x)){
				dist++;
			}
		}
		if(dist == 1){
			return true;
		}
		return false;
	}

	/**
	 * @param ladder WordLadder to output
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder == null) {
			throw new NullPointerException("ladder is null");
		}
		if (ladder.size() < 2) {
			throw new IllegalArgumentException("ladder length is less than 2");
		}
		if (ladder.size() == 2 && !(ladder.get(0).equalsIgnoreCase(ladder.get(1)))) {
			System.out.printf("no word ladder can be found between %s and %s.", ladder.get(0), ladder.get(1));
		}
		
		//Print first line
		int ladderLength = ladder.size() - 2; //Length excludes start and finish nodes.
		System.out.printf("a %d-rung word ladder exists between smart and money.\n", ladderLength);

		//Print all array elements
		for (int i = 0; i < ladder.size(); i++) {
			System.out.println(ladder.get(i));
		}
	}
	// TODO
	// Other private static methods here
}
