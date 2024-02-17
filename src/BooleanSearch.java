import java.util.*;

public interface BooleanSearch {

    /**@return set of filenames that satisfy the
     @request which is request to boolean search*/
    default Set<String> doBooleanSearch(String request){
        Stack<List<String>> stack = new Stack<>();
        String requestRPN = convertToRPN(request);
        for(String token : requestRPN.split(" ")){
            if(isOperator(token)){
                switch (token){
                    case "NOT" : {
                        stack.push(NOT(stack.pop()));
                        break;
                    }
                    case "AND" : {
                        stack.push(AND(stack.pop(), stack.pop()));
                        break;
                    }
                    case "OR" : {
                        stack.push(OR(stack.pop(), stack.pop()));
                        break;
                    }
                }
            }else{
                stack.push(filesContainingWord(token));
            }
        }

        return new HashSet<>(stack.pop());
    }

    /**@return
     * @param request converted to reverse polish notation*/
    private String convertToRPN(String request){
        List<String> output = new LinkedList<>();
        Stack<String> operators = new Stack<>();
        Map<String, Integer> precedence = Map.of("NOT", 3, "AND", 2, "OR", 1);
        String[] tokens = request.split(" ");

        for(String s : tokens){
            if(isOperator(s)){
                while (!operators.isEmpty() && precedence.get(s)
                        <= precedence.getOrDefault(operators.peek(), 0))
                    output.add(operators.pop());

                operators.push(s);
            }else if(s.equals("(")){
                operators.push(s);
            }else if(s.equals(")")){
                while (!operators.isEmpty() && !(operators.peek().equals("(")))
                    output.add(operators.pop());

                operators.pop();
            }else{
                output.add(s.toLowerCase(Locale.ROOT));
            }
        }
        while (!operators.isEmpty())
            output.add(operators.pop());

        System.out.println(output);
        return String.join(" ", output);
    }

    /**@return true if
     * @param token is boolean operator,
     * false otherwise*/
    private boolean isOperator(String token){
        return token.equals("AND") || token.equals("OR") || token.equals("NOT");
    }

    /**abstract method that needs to be implemented
     * @return list of names of files containing
     * @param word */
    List<String> filesContainingWord(String word);

    /**abstract method that needs to be implemented
     * @return list of names of files in data structure not containing
     * @param word */
    List<String> NOT(List<String> word);

    /**@return intersection of
     * @param a and
     * @param b */
    private List<String> AND(List<String> a, List<String> b){
        List<String> filesContainingBoth = new LinkedList<>(a);
        filesContainingBoth.retainAll(b);
        return filesContainingBoth;
    }

    /**@return union of
     * @param a and
     * @param b */
    private List<String> OR(List<String> a, List<String> b){
        List<String> filesContainingOne = new LinkedList<>(a);
        filesContainingOne.addAll(b);
        Set<String> withoutDuplicates = new HashSet<>(filesContainingOne);
        return new LinkedList<>(withoutDuplicates);
    }
}