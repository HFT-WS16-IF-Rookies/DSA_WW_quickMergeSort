package de.hft.wiest_wolf;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Lukas Wiest
 * @author Erik Wolf
 */
public class QuickMergeSort
{
    @FunctionalInterface
    public interface ISort
    {
        abstract void sort(int[] values);
    }

    enum Algo
    {
        ARRAYSSORT("Arrays.sort",   Arrays::sort),
        QUICKSORT("Quicksort",      QuickMergeSort::quickSort),
        MERGESORT("Mergesort",      QuickMergeSort::mergeSort);

        private final String name;
        private final ISort sort;

        Algo(String name, ISort sort)
        {
            this.name = name;
            this.sort = sort;
        }

        public void sort(int[] values)
        {
            sort.sort(values);
        };

        @Override
        public String toString()
        {
            return name;
        }
    }

    public static void main(String[] args)
    {
        // TODO code application logic here
    }

    public static int[] generate(int size)
    {
        Random random = new Random();
        int[] result = new int[size];
        for (int i=0; i < size; i++)
            result[i] = random.nextInt();

        return result;
    }

    public static void quickSort(int[] input)
    {
        // Erik-TODO: implement quickSort
    }

    public static void mergeSort(int[] input)
    {
        // Lukas-TODO: implement mergeSort
    }
}
