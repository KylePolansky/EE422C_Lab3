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

		ArrayList<String> parse = parse(kb);
		if (parse != null && parse.size() == 2) {
			printLadder(getWordLadderBFS(parse.get(0), parse.get(1)));
		}
		
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
		String input = keyboard.nextLine();
		if (input != null && input.length() > 0) {
			if (input.equalsIgnoreCase("/quit")) {
				return new ArrayList<>();
			}
			String[] split = input.split(" ");
			if (split.length == 2){
				return new ArrayList<String>() {{add(split[0].toUpperCase());	add(split[1].toUpperCase()); }};
			}
		}

		return null;
	}
	
	static ArrayList<String> result = new ArrayList<String>();
	static Set <String> visited = new HashSet<String>();
	public static ArrayList<String> getWordLadderDFS(String start, String end) {


		Set<String> dict = makeDictionary();
		LinkedList<String>[] adjList = makeAdjacency(dict);
		int position = getPosition(adjList, start);
		String current = adjList[position].getFirst();
		
	
		//start of DFS code
		if(current == null){
			return new ArrayList();
		}
		if(current.equals(end)){
			return result;
		}
		
		visited.add(current);
		LinkedList<String> list = adjList[position];
		for(String s : list) {
			int startPos = getPosition(adjList,s);
			if(!(visited.contains(adjList[startPos]))){
				result.add(current);
				getWordLadderDFS(start,end);
			}
		}
	}

    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		ArrayList<String> ladder = new ArrayList<>();
		Map<String, String> previousNodes = new HashMap<>();
		LinkedList<String>[] adjList = makeAdjacency(makeDictionary());

		boolean[] visited = new boolean[adjList.length];
		Queue<String> queue = new LinkedList<>();

		int startIndex = getPosition(adjList,start);

		queue.add(adjList[startIndex].getFirst());
		while (!queue.isEmpty()) {
			String current = queue.poll();

			//End if found the end
			if (current.equalsIgnoreCase(end)) {
				break;
			}
			int currentPos = getPosition(adjList,current);
			visited[currentPos] = true;
			LinkedList<String> list = adjList[currentPos];

			//Go through each edge for a node
			for (String s : list) {
				int sPos = getPosition(adjList,s);
				String sString = adjList[sPos].getFirst();

				//If not visited, add to queue
				if (visited[sPos] == false) {
					queue.add(sString);
					visited[sPos] = true;
					previousNodes.put(sString, current);
				}
			}
		}

		//Build Ladder
		for(String node = end; node != null; node = previousNodes.get(node)) {
			ladder.add(node);
		}

		//Reverse since traversing from end to start
		Collections.reverse(ladder);

		return ladder;
	}

	private static int getPosition(LinkedList<String>[] graph, String word) {
		int startIndex = -1;

		for (int i = 0; i < graph.length ; i++) {
			if (graph[i].getFirst().equalsIgnoreCase(word)) {
				startIndex = i;
				break;
			}
		}

		return startIndex;
	}

	private static LinkedList<String>[] makeAdjacency(Set<String> inputSet){
		if (inputSet == null) {
			throw new NullPointerException("inputSet");
		}
		if (inputSet.size() == 0) {
			throw new IllegalArgumentException("inputSet length is 0");
		}

		LinkedList<String>[] adjList = new LinkedList[inputSet.size()];

		Object[] inputSetArray = inputSet.toArray();
		String[] inputArray = new String[inputSetArray.length];
		for (int i = 0; i < inputSetArray.length; i++) {
			inputArray[i] = (String) inputSetArray[i];
		}
		for (int i = 0; i < inputSet.size(); i++) {
			//Make new linked list for current element
			LinkedList<String> current = new LinkedList<>();
			String currentWord = inputArray[i];
			current.add(inputArray[i]);

			//Go through existing words and see if any match
			for (int x = 0; x < i; x++) {
				LinkedList<String> previous = adjList[x];
				if (hammingDistanceOne(current.getFirst(),previous.getFirst())) {
					//Match found, add to previous list and current list
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
		if (start == null || word == null) {
			throw new NullPointerException();
		}
		if (start.length() != word.length()){ //strings must be the same length in order to work
			return false;
		}
		for(int x = 0; x < start.length(); x++){
			if(start.charAt(x) != word.charAt(x)){
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
		System.out.printf("a %d-rung word ladder exists between %s and %s.\n", ladderLength, ladder.get(0), ladder.get(ladderLength-1));

		//Print all array elements
		for (int i = 0; i < ladder.size(); i++) {
			System.out.println(ladder.get(i));
		}
	}
	// TODO
	// Other private static methods here
}
