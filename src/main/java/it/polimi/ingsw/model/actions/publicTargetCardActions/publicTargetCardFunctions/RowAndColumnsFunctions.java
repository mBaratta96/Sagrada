package it.polimi.ingsw.model.actions.publicTargetCardActions.publicTargetCardFunctions;

import it.polimi.ingsw.model.Dice;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class RowAndColumnsFunctions {
    private static final List<BiFunction<Integer, ArrayList<Dice>, String>> transformToString = new ArrayList<>();
    private static final Map<Integer, Predicate<String>> filter = new HashMap<>();
    private static final List<Integer> multiplier = new ArrayList<>();
    private static final Map<Integer, List<Integer>> values = new HashMap<>();
    static{
        transformToString.add(0, (i, grid)  -> IntStream.range(0,5)
                                                                .mapToObj(j -> String.valueOf(grid.get(j+i).getColor()))
                                                                .collect(Collectors.joining()));

        transformToString.add(1, (i, grid)  ->  IntStream.range(0,4)
                                                        .mapToObj(j -> String.valueOf(grid.get(5*j+i).getColor()))
                                                        .collect(Collectors.joining()));

        transformToString.add(2, (i, grid)  ->  IntStream.range(0,5)
                                                        .mapToObj(j -> String.valueOf(grid.get(j+i).getFace()))
                                                        .collect(Collectors.joining()));

        transformToString.add(3, (i, grid)  ->  IntStream.range(0,4)
                                                        .mapToObj(j -> String.valueOf(grid.get(5*j+i).getFace()))
                                                        .collect(Collectors.joining()));
    }
    static {
        filter.put(0, s -> !s.contains("+") && (int) s.chars().distinct().count() == 5);
        filter.put(1, s -> !s.contains("+") && (int) s.chars().distinct().count() == 4);
        filter.put(2, s -> !s.contains("0") && (int) s.chars().distinct().count() == 5);
        filter.put(3, s -> !s.contains("0") && (int) s.chars().distinct().count() == 4);



    }
    static {
        multiplier.add(0, 6);
        multiplier.add(1, 5);
        multiplier.add(2, 5);
        multiplier.add(3, 4);
    }

    static {
        values.put(0, Arrays.asList(0,5,10,15));
        values.put(1, Arrays.asList(0,1,2,3,4));
        values.put(2, Arrays.asList(0,5,10,15));
        values.put(3, Arrays.asList(0,1,2,3,4));


    }

    /**
     * Returns the function that transform a row or a column of dice into a string.
     * @param id is used to recognize the card that's been utilized
     * @return a lambda function to transform a dice into a string
     */
    public static BiFunction<Integer, ArrayList<Dice>, String> getTransform(int id) {
        return transformToString.get(id);
    }

    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return a lambda function that filter a stream of strings
     */
    public static Predicate<String> getFilter(int id) {
        return filter.get(id);
    }

    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return a multiplier for the occurrences of strings that respect the constraint of the public target card
     */
    public static int getMultiplier(int id) {
        return multiplier.get(id);
    }

    /**
     *
     * @param id is used to recognize the card that's been utilized
     * @return a list of indexes that represent the position of the first dice of a row or a column
     */
    public static List<Integer> getValues(int id) {
        return values.get(id);
    }
}
