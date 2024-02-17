import java.util.*;

public interface BooleanSearch {

    default List<String> doBooleanSearch(String request) {
        Stack<Object> stack = new Stack<>();
        String requestRPN = convertToRPN(request);

        for(String token : requestRPN.split(" ")){
            if(isOperator(token)){
                switch (token) {
                    case "NOT": {
                        Object a = stack.pop();
                        if (a instanceof String) {
                            String strA = (String) a;
                            stack.push(NOT(strA));
                        } else if (a instanceof List) {
                            List<String> lstA = (List<String>) a;
                            stack.push(NOT(lstA));
                        } else {
                            throw new IllegalArgumentException(token + " is from illegal type: " + token.getClass().getSimpleName());
                        }

                        break;
                    }
                    case "AND": {
                        Object a = stack.pop();
                        Object b = stack.pop();
                        if (a instanceof String) {
                            String strA = (String) a;
                            if (b instanceof String) {
                                String strB = (String) b;
                                stack.push(AND(strA, strB));
                            } else if (b instanceof List) {
                                List lstB = (List<String>) b;
                                stack.push(AND(strA, lstB));
                            } else {
                                throw new IllegalArgumentException(token + " is from illegal type: " + token.getClass().getSimpleName());
                            }

                        } else if (a instanceof List) {
                            List<String> lstA = (List<String>) a;
                            if (b instanceof String) {
                                String strB = (String) b;
                                stack.push(AND(lstA, strB));
                            } else if (b instanceof List) {
                                List lstB = (List<String>) b;
                                stack.push(AND(lstA, lstB));
                            } else {
                                throw new IllegalArgumentException(token + " is from illegal type: " + token.getClass().getSimpleName());
                            }
                        } else {
                            throw new IllegalArgumentException(token + " is from illegal type: " + token.getClass().getSimpleName());
                        }

                        break;
                    }
                    case "OR": {
                        Object a = stack.pop();
                        Object b = stack.pop();
                        if (a instanceof String) {
                            String strA = (String) a;
                            if (b instanceof String) {
                                String strB = (String) b;
                                stack.push(OR(strA, strB));
                            } else if (b instanceof List) {
                                List lstB = (List<String>) b;
                                stack.push(OR(strA, lstB));
                            } else {
                                throw new IllegalArgumentException(token + " is from illegal type: " + token.getClass().getSimpleName());
                            }

                        } else if (a instanceof List) {
                            List<String> lstA = (List<String>) a;
                            if (b instanceof String) {
                                String strB = (String) b;
                                stack.push(OR(lstA, strB));
                            } else if (b instanceof List) {
                                List lstB = (List<String>) b;
                                stack.push(OR(lstA, lstB));
                            } else {
                                throw new IllegalArgumentException(token + " is from illegal type: " + token.getClass().getSimpleName());
                            }
                        } else {
                            throw new IllegalArgumentException(token + " is from illegal type: " + token.getClass().getSimpleName());
                        }
                        break;
                    }
                }

            }else{
                stack.push(token);
            }
        }

        return (List<String>)stack.pop();
    }

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

        return String.join(" ", output);
    }

    private boolean isOperator(String request){
        return request.equals("AND") || request.equals("OR") || request.equals("NOT");
    }

    List<String> filesContainingWord(String word);

    private List<String> AND(String a, String b){
        return AND(filesContainingWord(a), filesContainingWord(b));
    }

    private List<String> OR(String a, String b){
        return OR(filesContainingWord(a), filesContainingWord(a));
    }

    List<String> NOT(String a);
    private List<String> AND(List<String> a, String b){
        return AND(a, filesContainingWord(b));
    }

    private List<String> OR(List<String> a, String b){
        return OR(a, filesContainingWord(b));
    }

    List<String> NOT(List<String> a);

    private List<String> AND(String a, List<String> b){
        return AND(b, a);
    }

    private List<String> OR(String a, List<String> b){
        return OR(b, a);
    }

    private List<String> AND(List<String> a, List<String> b){
        List<String> filesContainingBoth = new LinkedList<>(a);
        filesContainingBoth.retainAll(b);
        return filesContainingBoth;
    }

    private List<String> OR(List<String> a, List<String> b){
        List<String> filesContainingOne = new LinkedList<>(a);
        List<String> filesContainingOnlyB = new LinkedList<>(b);
        filesContainingOnlyB.retainAll(a);
        filesContainingOne.addAll(filesContainingOnlyB);
        return filesContainingOne;
    }
}