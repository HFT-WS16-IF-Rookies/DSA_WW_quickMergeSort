package de.hft.wiest_wolf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

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
        if (args.length == 0)
        {
            final int[] TESTSIZE = {100_000};
            for (int size: TESTSIZE)
                benchmark(size);
        }
        else
        {
            Arrays.stream(args[0].split(","))
                    .map(s -> Integer.valueOf(s.replace("_", "")))
                    .collect(Collectors.toList())
                    .stream()
                    .forEach(QuickMergeSort::benchmark);
        }
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
                System.out.print("run " + i + ":\t\t" + (runs[i] / 1_000_000d) + "\tms");
                if (!Arrays.equals(sorted, range))
                    System.out.print("\terror: wrong sorted!");
                System.out.println();
            }
            System.out.println("\n\n");
            avg = Arrays.stream(runs).map(Long::valueOf).sum() / runs.length;

            try
            {
                File file = new File(resultFile + "_" + alg + "_avg.csv");
                if (!file.exists())
                {
                    file.createNewFile();
                }
                PrintWriter pw = new PrintWriter(new FileOutputStream(file, true), true);
                pw.println(size + "\t" + (avg / 1_000_000d));
                pw.close();

                file = new File(resultFile + "_" + alg + "_single.csv");
                if (!file.exists())
                {
                    file.createNewFile();
                }
                pw = new PrintWriter(new FileOutputStream(file, true), true);
                for(long l: runs)
                    pw.println(size + "\t" + (l / 1_000_000d));
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
        quickSort(input, 0, input.length-1);
    }
    
    public static void quickSort(int[] input, int low, int high)
    {
        if(low < high)
        {
            int pi = partition(input, low, high);
            quickSort(input, low, pi - 1);
            quickSort(input, pi + 1, high);
        }
    }
    
    public static int partition (int[] input, int low, int high)
    {
        int pivot = input[high];
        int i = (low-1);
        for (int j=low; j<high; j++)
        {
            if (input[j] <= pivot) 
            {
                i++;
                int temp = input[i];
                input[i] = input[j];
                input[j] = temp;
            }
        }
        int temp = input[i+1]; 
        input[i+1] = input[high]; 
        input[high] = temp;
        return i+1;
    }
    
    public static void mergeSort(int[] input)
    {
        int[] from = input;
        int[] to = new int[input.length];
        int loops = 0;
        int partLengths = 1;
        int leftPointer;
        int rightPointer;
        int writePointer;
        int leftBorder;
        int rightBorder;

        for (;partLengths < input.length; partLengths *=2, loops++)
        {
            writePointer = 0;
            leftPointer = 0;
            leftBorder = partLengths-1;
            rightPointer = partLengths;
            rightBorder = (2*partLengths)-1;
            if (rightBorder >= input.length)
                rightBorder = input.length-1;

            for(;writePointer < input.length;)
            {
                for (;leftPointer <= leftBorder && rightPointer <= rightBorder;)
                {
                    to[writePointer++] = (from[leftPointer] <= from[rightPointer]) ?
                        from[leftPointer++] :
                        from[rightPointer++];
                }

                for(;leftPointer < input.length && leftPointer <= leftBorder;)
                    to[writePointer++] = from[leftPointer++];

                for (;rightPointer < input.length && rightPointer <= rightBorder;)
                    to[writePointer++] = from[rightPointer++];

                for(;leftPointer >= input.length && rightPointer >= input.length && writePointer < input.length;)
                    to[writePointer] = from[writePointer++];

                leftPointer += partLengths;
                leftBorder += 2*partLengths;
                rightPointer += partLengths;
                rightBorder += 2*partLengths;
                if (rightBorder >= input.length)
                    rightBorder = input.length-1;
            }

            int[] tmp = from;
            from = to;
            to = tmp;
        }
        if (loops % 2 != 0)
            System.arraycopy(from, 0, input, 0, input.length);

    }
}
