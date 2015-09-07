package co.starsky.colortrap.util;

import java.util.*;

/**
 * @author alliecurry
 */
public final class Shuffle {

    private Shuffle() {
        throw new AssertionError();
    }

    /** Creates a randomized stack of the given int array. */
    public static Stack<Integer> shuffleInt(int[] shuffleMe) {
        return shuffleInt(shuffleMe, shuffleMe.length);
    }

    /** Creates a randomized stack of the given int array, re-using values until the given size is reached. */
    public static Stack<Integer> shuffleInt(int[] shuffleMe, int size) {
        Stack<Integer> intStack = new Stack<Integer>();

        // Convert int array to a stack equal to the given amount
        int i = 0;
        while(intStack.size() < size) {
            intStack.add(shuffleMe[i]);
            i = (i == shuffleMe.length - 1) ? 0 : i+1;
        }

        Collections.shuffle(intStack); // randomize
        return intStack;
    }

    /** Creates a randomized stack of the given int array, re-using values until the given size is reached. */
    public static Stack<Integer> shuffleInt(final int min, final int max, int size) {
        final List<Integer> list = new ArrayList<Integer>();
        int num = min;
        for (int i = 0; i < size; ++i) {
            list.add(num);
            num = (num == max) ? min : num + 1;
        }
        Stack<Integer> intStack = new Stack<Integer>();
        intStack.addAll(list);
        Collections.shuffle(intStack); // randomize
        return intStack;
    }

    /** Creates a randomized stack of the given String array. */
    public static LinkedList<String> shuffleString(String[] shuffleMe) {
        return shuffleString(shuffleMe, shuffleMe.length);
    }

    /** Creates a randomized stack of the given String array, re-using values until the given size is reached. */
    public static LinkedList<String> shuffleString(String[] shuffleMe, int size) {
        LinkedList<String> list = new LinkedList<String>();

        // Convert int array to a stack equal to the given amount
        int i = 0;
        while(list.size() < size) {
            list.add(shuffleMe[i]);
            i = (i == shuffleMe.length - 1) ? 0 : i+1;
        }

        Collections.shuffle(list); // randomize
        return list;
    }

}
