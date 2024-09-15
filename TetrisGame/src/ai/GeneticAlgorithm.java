package ai;

import java.io.*;
import java.util.Random;

public class GeneticAlgorithm {
    public static int[] getDNA(String filename) {
        int[] constants = new int[5];
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            for (int i = 0; i < constants.length; i++) {
                line = br.readLine();
                if(line != null){
                    constants[i] = Integer.parseInt(line.trim());
                } else {
                    System.out.printf("No line available for constant at index %s in file %s", i, filename);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return constants;
    }

    public static int[] mutate(int[] DNA) {
        Random rand = new Random();

        // Choose a random index of the array
        int indexToMutate = 1 + rand.nextInt(DNA.length - 1);

        // Choose a number between 5 and -5
        // `nextInt(n)` generates numbers in the range 0 to n-1.
        int mutation = rand.nextInt(11) - 5;

        DNA[indexToMutate] += mutation;
        return DNA;
    }
}
