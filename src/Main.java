/*
* Based on the given collection of documents, build:
* "term-document" incidence matrix
* inverted index
*
* Justify the selected data storage structures
* in terms of their effectiveness when increasing data volumes.
* Perform a Boolean search on both structures. Compare the speed of execution of requests.
*/

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        File data = new File("data");
        List<File> fileList = new ArrayList<>();

        if(data.exists() && data.isDirectory()) {
            File[] dataFiles = data.listFiles();
            if(dataFiles != null)
                fileList.addAll(Arrays.asList(dataFiles));
        }

        InvertedIndex ii = new InvertedIndex(fileList);
        ii.saveResultsToFile("Inverted index");

        System.out.println(ii.doBooleanSearch("Harry"));
        System.out.println(ii.doBooleanSearch("Harry AND Potter"));
        System.out.println(ii.doBooleanSearch("Harry AND Potter NOT Horcrux"));
        System.out.println(ii.doBooleanSearch("NOT Luna AND Harry OR Bilbo"));
        System.out.println(ii.doBooleanSearch("Harry AND Luna OR Bilbo"));
        System.out.println(ii.doBooleanSearch("NOT ( Ron AND HARRY ) AND NOT Frodo"));
        System.out.println(ii.doBooleanSearch("Cedric"));
        System.out.println(ii.doBooleanSearch("Cedric OR Bilbo"));
        System.out.println(ii.doBooleanSearch("( Harry AND Luna NOT Cedric )"));
        System.out.println(ii.doBooleanSearch("Gandalf AND Aragorn"));
        System.out.println(ii.doBooleanSearch("( Gandalf AND Aragorn ) OR ( ( Harry AND Luna ) AND NOT Cedric )"));

       /* Scanner scanner = new Scanner(System.in);
        System.out.println("Print \"yes\" if you want to search your own phrase in data collection");
        String userAnswer = scanner.nextLine().toLowerCase();
        while(userAnswer.equals("yes")){
            System.out.print("Enter your request: ");
            String userRequest = scanner.nextLine();
            try{
                System.out.println(ii.doBooleanSearch(userRequest));
            }catch(IllegalArgumentException ex){
                ex.printStackTrace();
            }

            System.out.println("Print \"yes\" if you want to continue searching your own phrase in data collection");
            userAnswer = scanner.nextLine();
        }*/
    }
}