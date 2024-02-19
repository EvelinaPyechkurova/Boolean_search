/*
 * Class for creating an incidence matrix data structure for a given collection of files
 * where information about content of files is stored in form of matrix */

import java.io.*;
import java.util.*;

public class IncidenceMatrix implements BooleanSearch{
    private final List<File> fileList;
    private List<String> words;
    private int fileCount;
    private int wordCount;
    private String[][] incidenceMatrix;

    public IncidenceMatrix(List<File> fileList){
        this.fileList = fileList;
        createIncidenceMatrix();
    }

   /**Creates incidence matrix index for a given text file collection*/
    private void createIncidenceMatrix(){
        FileTokenizer ft = new FileTokenizer(fileList);

        words = new ArrayList<>(ft.getUniqueWords());
        fileCount = fileList.size();
        wordCount = words.size();

        incidenceMatrix = new String[fileCount+1][wordCount+1];

        for(int i = 1; i < wordCount+1; i++){
            incidenceMatrix[0][i] = words.get(i-1);
        }
        for(int i = 1; i < incidenceMatrix.length; i++){
            File currentFile = fileList.get(i-1);
            incidenceMatrix[i][0] = currentFile.getName();
            ft = new FileTokenizer(currentFile);
            Set<String> currentFileWords = ft.getUniqueWords();
            for(int j = 1; j < incidenceMatrix[i].length; j++){
                if(currentFileWords.contains(incidenceMatrix[0][j]))
                    incidenceMatrix[i][j] = "1";
                else
                    incidenceMatrix[i][j] = "0";
            }
        }
    }

    /**@return list of all files containing
     * @param word*/
    @Override
    public List<String> filesContainingWord(String word) {
        List<String> filesContainingWord = new ArrayList<>();
        int wordIndex = words.indexOf(word);

        if(wordIndex == -1)
            return filesContainingWord;

        // Adjusting index for the incidenceMatrix, as words list in matrix starts from 1
        wordIndex += 1;

        for(int i = 1; i < fileCount+1; i++){
            if(incidenceMatrix[i][wordIndex].equals("1"))
                filesContainingWord.add(incidenceMatrix[i][0]);
        }
        return filesContainingWord;
    }

    /**@return list of all files NOT containing elements of
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
    public void saveResultsToFile(String filename){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(toString());
            writer.close();
        }catch(FileNotFoundException ex){
            System.out.println(filename + " not found");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < wordCount; i++){
            for (String[] matrix : incidenceMatrix)
                result.append(matrix[i]).append(" ");
            result.append("\n");
        }
        return result.toString();
    }
}
