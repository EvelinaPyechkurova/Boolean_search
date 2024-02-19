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

        // Creating file collection
        File data = new File("data");
        List<File> fileList = new ArrayList<>();

        if(data.exists() && data.isDirectory()) {
            File[] dataFiles = data.listFiles();
            if(dataFiles != null)
                fileList.addAll(Arrays.asList(dataFiles));
        }

        // Creating inverted index
        InvertedIndex ii = new InvertedIndex(fileList);
        ii.saveResultsToFile("Inverted index");

        // Creating incidence matrix
        IncidenceMatrix im = new IncidenceMatrix(fileList);
        im.saveResultsToFile("Incidence matrix");

        // Testing boolean search on inverted index and igncidence matrix
        String[] requests = {"Harry", "Cedric", "Harry NOT Cedric", "Harry AND Potter", "Harry AND Potter NOT Horcrux",
                "NOT Luna AND Harry OR Bilbo", "Harry AND Luna OR Bilbo", "NOT ( Ron AND HARRY ) NOT Frodo",
                "Cedric OR Bilbo", "( Harry AND Luna NOT Cedric )", "Gandalf AND Aragorn",
                "( Gandalf AND Aragorn ) OR ( ( Harry AND Luna ) NOT Cedric )"};


        for(String request : requests) {
            Set<String> result = ii.doBooleanSearch(request);
            if (!result.equals(im.doBooleanSearch(request))) {
                System.out.println("ERROR! Results of boolean search on same request using different data " +
                        "structures are different!");
                break;
            }else {
                System.out.println("REQUEST: "+request + "\nRESULT: " + result);
            }
        }

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