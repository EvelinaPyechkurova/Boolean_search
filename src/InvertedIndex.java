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

    /*Implementation of BooleanSearch interface methods*/

    /**@return list of all files containing elements of
     * @param word*/
    @Override
    public List<String> filesContainingWord(String word) {
        return new LinkedList<>(invertedIndex.get(word));
    }

    /**@return list of all files NOT containing
     * @param a*/
    @Override
    public List<String> NOT(List<String> a) {
        List<String> namesOfAllFiles = new LinkedList<>();
        for(File file : fileList)
            namesOfAllFiles.add(file.getName());

        List<String> filesNotContaining = new LinkedList<>(namesOfAllFiles);
        filesNotContaining.removeAll(a);
        return filesNotContaining;
    }

    /**Saves Inverted Index data structure to file
     * @param filename */
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


    @Override
    public String toString(){
        return invertedIndex.toString();
    }
}