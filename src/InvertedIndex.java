/*
* Class for creating an inverted index data structure for a given collection of files
* where every term holds information about every file it appears in
*/

import java.util.*;
import java.io.*;

public class InvertedIndex implements BooleanSearch{
    private final List<File> fileList;
    private TreeMap<String, List<String>> invertedIndex;
    public InvertedIndex(List<File> fileList){
        this.fileList = fileList;
        createInvertedIndex();
    }

    /**Creates inverted index for a given text file collection,
     * where to every word corresponds a list of files where this word appeared*/
    private void createInvertedIndex(){
        invertedIndex = new TreeMap<>();
        FileTokenizer fd;
        for(File file : fileList){
            fd = new FileTokenizer(file);
            TreeSet<String> uniqueWords = (TreeSet<String>) fd.getUniqueWords();
            for(String word : uniqueWords){
                List<String> currentList;
                if(invertedIndex.containsKey(word)){
                    // if word already is in inverted index, add to its list a new filename
                    currentList = invertedIndex.get(word);
                    currentList.add(file.getName());
                }else{
                    // if word isn't in the inverted index, add the word and the list with the first filename
                    currentList = new ArrayList<>();
                    currentList.add(file.getName());
                    invertedIndex.put(word, currentList);
                }
            }
        }
    }

    public void saveResultsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Inverted index:\n");
            for (Map.Entry<String, List<String>> entry : invertedIndex.entrySet()) {
                String line = entry.getKey() + " = " + entry.getValue();
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*Implementation of BooleanSearch interface methods*/

    /**@return list of all files containing both
     * @param a AND
     * @param b*/
    public List<String> AND(String a, String b) {
        List<String> filesContainingBoth = new LinkedList<>(invertedIndex.get(a));
        filesContainingBoth.retainAll(invertedIndex.get(b));
        return filesContainingBoth;
    }

    /**@return list of all files containing
     * @param a OR
     * @param b*/
    public List<String> OR(String a, String b){
        List<String> filesContainingOne = new LinkedList<>(invertedIndex.get(a));
        filesContainingOne.addAll(invertedIndex.get(b));

        // Convert the list to a set to remove duplicates
        Set<String> setWithoutDuplicates = new HashSet<>(filesContainingOne);

        // Convert the set back to a list
        return new LinkedList<>(setWithoutDuplicates);
    }

    /**@return list of all files NOT containing
     * @param a*/
    public List<String> NOT(String a){
        List<String> namesOfAllFiles = new LinkedList<>();
        for(File file : fileList){
            namesOfAllFiles.add(file.getName());
        }
        List<String> filesNotContaining = new LinkedList<>(namesOfAllFiles);
        List<String> filesContainingA = new LinkedList<>(invertedIndex.getOrDefault(a, Collections.emptyList()));

        filesNotContaining.removeAll(filesContainingA);

        return filesNotContaining;
    }

    @Override
    public List<String> AND(List<String> a, String b) {
        List<String> filesContainingBoth = new LinkedList<>(a);
        filesContainingBoth.retainAll(invertedIndex.get(b));
        return filesContainingBoth;
    }

    @Override
    public List<String> OR(List<String> a, String b) {
        List<String> filesContainingOne = new LinkedList<>(a);
        filesContainingOne.addAll(invertedIndex.get(b));
        Set<String> setWithoutDuplicates = new HashSet<>(filesContainingOne);
        return new LinkedList<>(setWithoutDuplicates);
    }

    @Override
    public List<String> NOT(List<String> a) {
        List<String> namesOfAllFiles = new LinkedList<>();
        for(File file : fileList)
            namesOfAllFiles.add(file.getName());

        List<String> filesNotContaining = new LinkedList<>(namesOfAllFiles);
        filesNotContaining.removeAll(a);
        return filesNotContaining;
    }


    @Override
    public String toString(){
        return invertedIndex.toString();
    }
}