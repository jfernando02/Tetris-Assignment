package ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class GeneticAlgorithm {

    private static boolean spawn;
    private static boolean reproduce;
    private static double mutationStep = 0.2;
    private static double mutationRate = 0.05;
    public GeneticAlgorithm() {
        spawn = false;
        reproduce = false;
    }

    public static void main(String[] args) {
        if(spawn) {
            spawn("src/ai/DNA.json");
        }
        if(reproduce) {
            selectAndReproduce("src/ai/DNA.json");
        }
    }

    public static void spawn(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Double>> population = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < 48; i++) {
            Map<String, Double> DNA = new HashMap<>();
            DNA.put("id", (double) i);
            DNA.put("linesCleared", r.nextDouble());
            DNA.put("height", r.nextDouble() * -1);
            DNA.put("relativeHeight", r.nextDouble() * -1);
            DNA.put("maxHeight", r.nextDouble() * -1);
            DNA.put("holes", r.nextDouble() * -1);
            DNA.put("bumpiness", r.nextDouble() * -1);
            population.add(DNA);
        }

        try  {
            mapper.writeValue(new FileWriter(filename), population);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countWithFitness(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        int count = 0;

        try {
            // Read data from file
            List<Map<String, Double>> dnas = mapper.readValue(new File(filename),
                    new TypeReference<>() {});

            // Iterate over each DNA and increment count if 'fitness' key is present
            for(Map<String, Double> dna : dnas){
                if(dna.containsKey("fitness")) {
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static Map<String, Double> getFirstUnfitDNA(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Read data from file
            List<Map<String, Double>> dnas = mapper.readValue(new File(filename),
                    new TypeReference<>() {
                    });

            // Iterate over each DNA and check for the existence of 'fitness'
            for(Map<String, Double> dna : dnas){
                if(!dna.containsKey("fitness")){
                    // If 'fitness' does not exist, return it
                    return dna;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return an empty map if all DNAs have 'fitness' or in case of an exception
        return Collections.emptyMap();
    }

    public static void selectAndReproduce(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Double>> newPopulation = new ArrayList<>();
        Random r = new Random();
        try {
            // Read data from file
            List<Map<String, Double>> dnas = mapper.readValue(new File(filename), new TypeReference<List<Map<String, Double>>>() {});
            // Sort dnas by 'fitness' in descending order
            dnas.sort((dna1, dna2) -> Double.compare(dna2.get("fitness"), dna1.get("fitness")));
            // Select top 24
            List<Map<String, Double>> selectedDNAs = dnas.subList(0, Math.min(dnas.size(), 24));
            // Shuffle the list for random pairing
            Collections.shuffle(selectedDNAs);

            for (int i = 0; i < selectedDNAs.size(); i += 2) {
                Map<String, Double> parent1 = selectedDNAs.get(i);

                // Ensure there's a pair for each parent
                for (int j = 0; j < 2 && i + j < selectedDNAs.size(); j++) {
                    Map<String, Double> parent2 = selectedDNAs.get(i + j);
                    Map<String, Double> child = new HashMap<>();

                    // Generate child from parents
                    for (String key : parent1.keySet()) {
                        double parentValue = r.nextBoolean() ? parent1.get(key) : parent2.get(key);

                        // Apply mutation
                        if (r.nextDouble() < mutationRate) {
                            double mutation = (r.nextDouble() < 0.5) ? mutationStep : -mutationStep;  // mutation can be positive or negative
                            parentValue += parentValue * mutation;  // mutate the value by a certain percent
                        }
                        child.put(key, parentValue);
                    }

                    // Remove 'fitness' of child as it has not been evaluated yet
                    child.remove("fitness");

                    // Add to new population
                    newPopulation.add(child);
                }
            }

            // Write new population to file
            mapper.writeValue(new FileWriter(filename), newPopulation);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
