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

    public static void main(String[] args) throws Exception {

        Scanner kb;    // input Scanner for commands
        PrintStream ps;    // output file
        // If arguments are specified, read/write from/to files instead of Std IO.
        if (args.length != 0) {
            kb = new Scanner(new File(args[0]));
            ps = new PrintStream(new File(args[1]));
            System.setOut(ps);            // redirect output to ps
        } else {
            kb = new Scanner(System.in);// default from Stdin
            ps = System.out;            // default to Stdout
        }
        initialize();

        ArrayList<String> parse = parse(kb);
        if (parse != null && parse.size() == 2) {
            try {
                ArrayList<String> bfs = getWordLadderBFS(parse.get(0), parse.get(1));
                printLadder(bfs);

//                ArrayList<String> dfs = getWordLadderDFS(parse.get(0), parse.get(1));
//                printLadder(dfs);
            } catch (Exception e) {
                ps.printf("no word ladder can be found between %s and %s.\n", parse.get(0), parse.get(1));
            }
        }
    }

    public static Set<String> dictionary;
    public static HashMap<String, HashSet<String>> adjList;
    static Set<String> visited = new HashSet<>();
    public static void initialize() {
        dictionary = makeDictionary();
        adjList = makeAdjacency(dictionary);
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
                System.exit(0);
            }
            final String[] split = input.split("\\s+");
            if (split.length == 2) {
                return new ArrayList<String>() {{
                    add(split[0].toUpperCase());
                    add(split[1].toUpperCase());
                }};
            }
        }

        return null;
    }

    /**
     * @param start first word in the ladder
     * @param end last word in the latter
     * @return an ArrayList containing the WordLadder as determined by DFS algorithm
     */
    public static ArrayList<String> getWordLadderDFS(String start, String end) {
        //Accept non-all upper case input
        start = start.toUpperCase();
        end = end.toUpperCase();
        visited = new HashSet<>();
        if (adjList.containsKey(start) && adjList.containsKey(end)) {
            return getWordLadderDFSprivate(start, end, new ArrayList<>());
        }
        else {
            return new ArrayList<>();
        }
    }

    private static ArrayList<String> getWordLadderDFSprivate(String start, String end, ArrayList<String> result) {
        visited.add(start);
        result.add(start);

        if (start.equalsIgnoreCase(end)) {
            return result;
        }

        for (String s : adjList.get(start)) {
            if (!(visited.contains(s))) {
                ArrayList<String> al = getWordLadderDFSprivate(s, end, result);
                if (al.size() > 1) {
                    return al;
                }
            }
        }
        return new ArrayList();
    }

    /**
     * Gets a word ladder using BFS algorithm
     * @param start the first word of the word ladder
     * @param end the last word of the word ladder
     * @return the word ladder
     */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
        //Accept non-all upper case input
        start = start.toUpperCase();
        end = end.toUpperCase();
        //Start == End
        if (start.equals(end)) {
            ArrayList<String> returnList = new ArrayList<>();
            returnList.add(start);
        }

        ArrayList<String> ladder = new ArrayList<>();
        Map<String, String> previousNodes = new HashMap<>();

        visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        if (!adjList.containsKey(start)) {
            return new ArrayList<>(); //Start word cannot be found in dictionary
        }

        queue.add(start);

        while (!queue.isEmpty()) {
            String currentString = queue.poll();

            //End if found the end
            if (currentString.equals(end)) {
                break;
            }

            visited.add(currentString);

            //Go through each edge for a node
            //If not visited, add to queue and track previous nodes
            adjList.get(currentString).stream().filter(s -> !visited.contains(s)).forEach(s -> {
                queue.add(s);
                visited.add(s);
                previousNodes.put(s, currentString);
            });
        }

        //Check valid ladder
        if (!previousNodes.containsKey(end)) {
            return ladder;
        }

        //Build Ladder
        for (String node = end; node != null; node = previousNodes.get(node)) {
            ladder.add(node);
        }

        //Reverse since traversing from end to start
        Collections.reverse(ladder);

        return ladder;
    }

    /**
     * Reads files from input file and makes a dictionary
     * @return a Set containing all the words in the dictionary
     */
    public static Set<String> makeDictionary() {
        Set<String> words = new HashSet<>();
        Scanner infile = null;
        try {
			infile = new Scanner (new File("five_letter_words.txt"));
//            infile = new Scanner(new File("short_dict.txt"));
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
     * Make an Adjacency list to easily traverse words in the dictionary
     * @param inputSet the Dictionary file to make a list out of
     * @return The Adjacency list.
     */
    private static HashMap<String,HashSet<String>> makeAdjacency(Set<String> inputSet) {
        if (inputSet == null) {
            throw new NullPointerException("inputSet");
        }
        if (inputSet.size() == 0) {
            throw new IllegalArgumentException("inputSet length is 0");
        }

        HashMap<String,HashSet<String>> adjList = new HashMap<>();

        for (String keyString : inputSet) {
            HashSet<String> currentHS = new HashSet<>();
            currentHS.add(keyString);
            inputSet.stream().filter(otherString -> hammingDistanceOne(keyString, otherString)).forEach(otherString -> {
                currentHS.add(otherString);
                if (adjList.containsKey(otherString)) {
                    adjList.get(otherString).add(keyString);
                }
            });
            adjList.put(keyString, currentHS);
        }
        return adjList;
    }


    /**
     * This method compares 2 words and computes their hammingdistance
     *
     * @param start is the starting word of the word ladder
     * @param word  is a word from the dictionary we're comparing to
     * @return true if the difference between two words 1 letter, false otherwise
     */
    public static boolean hammingDistanceOne(String start, String word) {
        int dist = 0;
        if (start == null || word == null) {
            throw new NullPointerException();
        }
        if (start.length() != word.length()) { //strings must be the same length in order to work
            return false;
        }
        for (int x = 0; x < start.length(); x++) {
            if (start.charAt(x) != word.charAt(x)) {
                dist++;
            }
        }
        if (dist == 1) {
            return true;
        }
        return false;
    }

    /**
     * @param ladder WordLadder to output
     */
    public static void printLadder(ArrayList<String> ladder) {
        if (ladder == null || ladder.size() < 1) {
            throw new NullPointerException("ladder is null or less than 2");
        }

        String f = ladder.get(0).toLowerCase();
        if (ladder.size() == 1) {
            System.out.printf("a 0-rung word ladder exists between %s and %s.\n%s\n%s\n", f,f,f,f);
            return;
        }

        //Print first line
        int ladderLength = ladder.size() - 2; //Length excludes start and finish nodes.
        System.out.printf("a %d-rung word ladder exists between %s and %s.\n", ladderLength, f, ladder.get(ladder.size() - 1).toLowerCase());

        //Print all array elements
        for (int i = 0; i < ladder.size(); i++) {
            System.out.println(ladder.get(i).toLowerCase());
        }
    }
}
