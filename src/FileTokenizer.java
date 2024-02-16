/*
* Class for creating a dictionary consisting of lowercase unique words in alphabetical order
* for given File
* */

import java.io.*;
import java.util.*;

public class FileTokenizer {
    private int wordCount;
    private int uniqueWordsCount;
    private File file;
    private List<File> fileList;
    private List<String> wordsList; // List of all normalized words in a file
    private Set<String> uniqueWordsSet; // Set of all unique normalized words in a file

    /**Constructor, takes single file as a parameter*/
    public FileTokenizer(File file){
        this.file = file;
        analyzeFile();
    }

    /**Constructor, takes collection of txt files as a parameter*/
    public FileTokenizer(List<File> fileList){
        this.fileList = fileList;
        analyzeCollection();
    }

    /**@return Set of normalized unique words found in a given file(collection)*/
    public Set<String> getUniqueWords(){
        return uniqueWordsSet;
    }

    /**@return List of all words found in a given file(collection) and normalized */
    public List<String> getWordsList(){
        return wordsList;
    }

    /**@return number of all words found in a given file(collection)*/
    public int getWordCount(){
        wordCount = wordsList.size();
        return wordCount;
    }

    /**@return number of unique words found in a given file(collection)*/
    public int getUniqueWordsCount(){
        uniqueWordsCount = uniqueWordsSet.size();
        return uniqueWordsCount;
    }

    /** Analyzes given file:
     * Creates list of all words from a given file,
     * creates set of unique words in the file
     */
    private void analyzeFile() {
        wordsList = new ArrayList<>();

        readWords(file);
        uniqueWordsSet = new TreeSet<>(wordsList);
    }

    /** Analyzes given file collection:
     * Creates list of all words from a given collection,
     * creates set of unique words in the collection
     */
    private void analyzeCollection() {
        wordsList = new ArrayList<>();
        for (File file : fileList) {
            readWords(file);
        }
        uniqueWordsSet = new TreeSet<>(wordsList);
    }

    /** @return List of all words read from the input file */
    private void readWords(File file){
        StringTokenizer tokenizer;

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            String delimiters = ",;:()|.!?\"”“‘‘—’'\f1234567890/&*-\\[]{}_\t\n\r ";
            while((line = reader.readLine()) != null){
                tokenizer = new StringTokenizer(line, delimiters);
                while (tokenizer.hasMoreTokens())
                    wordsList.add(tokenizer.nextToken().toLowerCase(Locale.ROOT));
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return uniqueWordsSet.toString();
    }
}
