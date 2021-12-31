import java.util.Stack;
import java.util.Vector;

/*
1、设操作数栈和运算符栈，
设表达式结束的标志是字符#，运算符栈底初始化为#，约定#运算符的优先级最小（这样做的目的是在当两个#相遇时就可以确定表达式扫描结束了）。

2）、若当前扫描到的是操作数则果断将此数压栈进操作数栈，如果当前是符号栈则将该操作符和栈顶操作符进行优先级比较
如果低于栈顶优先级则将操作符栈顶元素弹出并弹出两个操作数进行运算，运算完毕将结果压入栈中。
如果当前符号的优先级高于栈顶优先级则将此运算符入栈。

3）循环操作2直到输入的表达式运算结束（运算符栈底的#和输入的表达式的#相遇）
此时如若操作数栈中只剩一个数字则表示运算成功，此数就是表达式的结果，如果不止一个数则表示输入的表达式有误
*/
public class Calculator {//计算表达式的值，这里单独拎出来封装成一个类了

    public static String calculate(Vector<String> vector) {
        Stack<String> number = new Stack<>();//操作数栈
        Stack<String> op = new Stack<>();//运算符栈
        int count = 0;
        for (int i = 0; i < vector.size(); i++) {
            if (vector.elementAt(i).equals("(")) count++;
            else if (vector.elementAt(i).equals(")")) count--;
        }
        if (count != 0) System.exit(-1);
        for (int i = 0; i < vector.size(); i++) {
            if (vector.elementAt(i).equals("+") ||
                    vector.elementAt(i).equals("-") ||
                    vector.elementAt(i).equals("*") ||
                    vector.elementAt(i).equals("/") ||
                    vector.elementAt(i).equals("%") ||
                    vector.elementAt(i).equals("Lt") ||
                    vector.elementAt(i).equals("LE") ||
                    vector.elementAt(i).equals("Gt") ||
                    vector.elementAt(i).equals("GE") ||
                    vector.elementAt(i).equals("Eq") ||
                    vector.elementAt(i).equals("NEq") ||
                    vector.elementAt(i).equals("OR") ||
                    vector.elementAt(i).equals("AND") ||
                    vector.elementAt(i).equals("NOT") ||
                    vector.elementAt(i).equals("(") ||
                    vector.elementAt(i).equals(")")) {
                if (op.isEmpty()) {
                    op.push(vector.elementAt(i));
                } else {
                    switch (vector.elementAt(i)) {
                        case "OR":
                            while (!op.isEmpty() && !op.peek().equals("(")) {
                                calculateStack(number, op);
                            }
                            op.push(vector.elementAt(i));
                            break;
                        case "AND":
                            while (!op.isEmpty() && !op.peek().equals("(") && !op.peek().equals("OR")) {
                                calculateStack(number, op);
                            }
                            op.push(vector.elementAt(i));
                            break;
                        case "Eq":
                        case "NEq":
                            while (!op.isEmpty() && !op.peek().equals("(")
                                    && !op.peek().equals("OR") && !op.peek().equals("AND")) {
                                calculateStack(number, op);
                            }
                            op.push(vector.elementAt(i));
                            break;
                        case "Lt":
                        case "LE":
                        case "Gt":
                        case "GE":
                            while (!op.isEmpty() && !op.peek().equals("(") &&
                                    !op.peek().equals("OR") && !op.peek().equals("AND")
                                    && !op.peek().equals("Eq") && !op.peek().equals("NEq")
                            ) {
                                calculateStack(number, op);
                            }
                            op.push(vector.elementAt(i));
                            break;
                        case "+":
                        case "-":
                            while (!op.isEmpty() && !op.peek().equals("(") &&
                                    !op.peek().equals("OR") && !op.peek().equals("AND")
                                    && !op.peek().equals("Eq") && !op.peek().equals("NEq")
                                    && !op.peek().equals("Lt") && !op.peek().equals("LE")
                                    && !op.peek().equals("Gt") && !op.peek().equals("GE")
                            ) {
                                calculateStack(number, op);
                            }
                            op.push(vector.elementAt(i));
                            break;
                        case "*":
                        case "/":
                        case "%":
                            while (!op.isEmpty() && !op.peek().equals("(")
                                    && !op.peek().equals("OR") && !op.peek().equals("AND")
                                    && !op.peek().equals("Eq") && !op.peek().equals("NEq")
                                    && !op.peek().equals("Lt") && !op.peek().equals("LE")
                                    && !op.peek().equals("Gt") && !op.peek().equals("GE")
                                    && !op.peek().equals("+") && !op.peek().equals("-")) {
                                calculateStack(number, op);
                            }
                            op.push(vector.elementAt(i));
                            break;
                        case "NOT":
                            while (!op.isEmpty() && !op.peek().equals("(")
                                    && !op.peek().equals("OR") && !op.peek().equals("AND")
                                    && !op.peek().equals("Eq") && !op.peek().equals("NEq")
                                    && !op.peek().equals("Lt") && !op.peek().equals("LE")
                                    && !op.peek().equals("Gt") && !op.peek().equals("GE")
                                    && !op.peek().equals("+") && !op.peek().equals("-")
                                    && !op.peek().equals("*") && !op.peek().equals("/")
                                    && !op.peek().equals("%")
                            ) {
                                String a;
                                while (op.peek().equals("NOT")) {//其实就是calculate stack
                                    op.pop();
                                    if(number.size()<1){
                                        System.exit(-11);
                                    }
                                    a=parser(number.pop());
                                    Main.res.add("%" + Main.counter + " = xor i32 " + a +", 1 ");
                                    number.push("%" + Main.counter);//计算完的值压栈
                                    Main.counter++;
                                }
                                op.push(vector.elementAt(i));
                            }
                            break;
                        case "(":
                            op.push("(");
                            break;
                        case ")":
                            if (op.search("(") == -1) {
                                System.exit(-8);
                            }
                            while (!op.peek().equals("(")) {
                                calculateStack(number, op);
                            }
                            op.pop();
                            break;
                    }
                }
            }
            else if (vector.elementAt(i).matches("^-?[0-9]\\d*$") || vector.elementAt(i).charAt(0) == '_'
                    ||vector.elementAt(i).charAt(0) == '%'
                    || Character.isLetter(vector.elementAt(i).charAt(0))) {//数字或变量，直接入栈
                number.push(vector.elementAt(i));
            }
        }
        if (!op.isEmpty() || number.size() >= 2) {
            while (!op.isEmpty()) {
                calculateStack(number, op);
            }
        }
        if (number.size() != 1) {
            System.exit(-10);
        }
        if (number.peek().charAt(0) != '%') {
            return parser(number.peek());
        } else return number.peek();
    }

    private static void calculateStack(Stack<String> number, Stack<String> op) {
        String a, b;
        switch (op.peek()) {
            case "*":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-2);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = mul i32 " + a + " , " + b);//eg. %2 = mul i32 %1 , 10
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
            case "/":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-13);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = sdiv i32 " + a + " , " + b);//eg. %2 = sdiv i32 %1 , 10
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
            case "%":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-14);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = srem i32 " + a + " , " + b);//eg. %2 = srem i32 %1 , 10
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
            case "+":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = add i32 " + a + " , " + b);//eg. %2 = add i32 %1 , 10
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
            case "-":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = sub i32 " + a + " , " + b);//eg. %2 = sub i32 %1 , 10
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
            case "Lt"://相应的逻辑运算
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = icmp slt i32 " + a + " , " + b);//eg. %2 = icmp lt i32 %1 , 10
                Main.counter++;
                Main.res.add("%" + Main.counter + " = zext i1 " + "%"+(Main.counter-1)+" to i32");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
                    /*%cmp = icmp lt i32 %0, %1
                    %conv = zext i1 %cmp to i32*/
            case "Gt":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = icmp sgt i32 " + a + " , " + b);//eg. %2 = icmp lt i32 %1 , 10
                Main.counter++;
                Main.res.add("%" + Main.counter + " = zext i1 " + "%"+(Main.counter-1)+" to i32");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
                    /*%cmp = icmp gt i32 %0, %1
                    %conv = zext i1 %cmp to i32*/
            case "LE":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = icmp sle i32 " + a + " , " + b);//eg. %2 = icmp lt i32 %1 , 10
                Main.counter++;
                Main.res.add("%" + Main.counter + " = zext i1 " + "%"+(Main.counter-1)+" to i32");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
                    /*%cmp = icmp le i32 %0, %1
                    %conv = zext i1 %cmp to i32*/
            case "GE":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = icmp sge i32 " + a + " , " + b);//eg. %2 = icmp lt i32 %1 , 10
                Main.counter++;
                Main.res.add("%" + Main.counter + " = zext i1 " + "%"+(Main.counter-1)+" to i32");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
                    /*%cmp = icmp ge i32 %0, %1
                    %conv = zext i1 %cmp to i32*/
            case "Eq":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = icmp eq i32 " + a + " , " + b);//eg. %2 = icmp lt i32 %1 , 10
                Main.counter++;
                Main.res.add("%" + Main.counter + " = zext i1 " + "%"+(Main.counter-1)+" to i32");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
                    /* %cmp = icmp eq i32 %0, %1
                    %conv = zext i1 %cmp to i32 */
            case "NEq":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = icmp ne i32 " + a + " , " + b);//eg. %2 = icmp lt i32 %1 , 10
                Main.counter++;
                Main.res.add("%" + Main.counter + " = zext i1 " + "%"+(Main.counter-1)+" to i32");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
                    /* %cmp = icmp ne i32 %0, %1
                    %conv = zext i1 %cmp to i32 */
            case "AND":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = icmp ne i32 " + b + " , 0");
                Main.counter++;
                Main.res.add("%" + Main.counter + " = icmp ne i32 " + a + " , 0");
                Main.counter++;
                Main.res.add("%" + Main.counter + " = and i1 " + (Main.counter-1) + " , " +(Main.counter-2));//eg. %3 = and i32 %1 , %2
                Main.counter++;
                Main.res.add("%" + Main.counter + " = zext i1 " + "%"+(Main.counter-1)+" to i32");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
            case "OR":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-11);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = icmp ne i32 " + b + " , 0");
                Main.counter++;
                Main.res.add("%" + Main.counter + " = icmp ne i32 " + a + " , 0");
                Main.counter++;
                Main.res.add("%" + Main.counter + " = or i1 " + (Main.counter-1) + " , " +(Main.counter-2));//eg. %3 = or i32 %1 , %2
                Main.counter++;
                Main.res.add("%" + Main.counter + " = zext i1 " + "%"+(Main.counter-1)+" to i32");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
            case "NOT":
                op.pop();
                if(number.size()<1){
                    System.exit(-11);
                }
                a=parser(number.pop());
                Main.res.add("%" + Main.counter + " = xor i32 " + a +", 1 ");
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
        }
    }

    public static String parser(String name) {//
        if (name.matches("^-?[0-9]\\d*$")) {
            return name;
        } else if (name.charAt(0) == '%') {
            return name;//eg. i32 %1
        } else {
            Ident ident;
            ident = BlockItemAnalyzer.findIdent(name);
            if (ident != null) {
                int no = ident.infos.elementAt(0).no;
                if(ident.infos.elementAt(0).level==0){
                    Main.res.add("%" + Main.counter + " = load i32, i32* @" + no);
                }
                else {
                    Main.res.add("%" + Main.counter + " = load i32, i32* %" + no);
                }
                Main.counter++;
                return "%" + (Main.counter-1);
            } else {
                System.exit(-9);
                return null;
            }
        }
    }
}

