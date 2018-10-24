package de.hft.wiest_wolf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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

    public static Date date = new Date();

    public static void main(String[] args)
    {
        // TODO code application logic here
    }

    public static void benchmark(int size)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String resultFile = "DSA_WW_MicroBench_" + df.format(date);
        long[] runs;
        int[] generated = generate(size);
        int[] sorted = generated.clone();
        Arrays.sort(sorted);
        int[] range;

        for (Algo alg: Algo.values())
        {
            System.out.println("algorithm:\t" + alg);
            System.out.println("array-size:\t" + size);
            System.out.println("needed Time:");

            runs = new long[5];
            long avg;

            for (int i=0; i < runs.length; i++)
            {
                range = generated.clone();
                runs[i] = System.nanoTime();
                alg.sort(range);
                runs[i] = System.nanoTime() - runs[i];
                System.out.print("run " + i + ":\t\t" + (runs[i] / 1_000_000_000d) + "\tseconds");
                if (!Arrays.equals(sorted, range))
                    System.out.print("\terror: wrong sorted!");
                System.out.println();
            }
            System.out.println("\n\n");
            avg = Arrays.asList(runs).stream().mapToInt(Integer::intValue).sum() / runs.length;

            try
            {
                File file = new File(resultFile + "_" + alg + "_avg.csv");
                if (!file.exists())
                {
                    file.createNewFile();
                }
                PrintWriter pw = new PrintWriter(new FileOutputStream(file, true), true);
                pw.println(size + "\t" + (avg / 1000000000d));
                pw.close();

                file = new File(resultFile + "_" + alg + "_single.csv");
                if (!file.exists())
                {
                    file.createNewFile();
                }
                pw = new PrintWriter(new FileOutputStream(file, true), true);
                for(long l: runs)
                    pw.println(size + "\t" + (l / 1_000_000_000d));
                pw.println();
                pw.close();
            }
            catch (IOException e)
            {
                System.err.println("Couldn't save runtime for algorithm: " + alg + " for size: " + size);
                e.printStackTrace();
            }
        }
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
