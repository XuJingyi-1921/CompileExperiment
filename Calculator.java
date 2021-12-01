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
            if (vector.elementAt(i).matches("^-?[0-9]\\d*$") || vector.elementAt(i).charAt(0) == '_'
                    || Character.isLetter(vector.elementAt(i).charAt(0))) {//数字或变量，直接入栈
                number.push(vector.elementAt(i));
            } else if (vector.elementAt(i).equals("+") ||
                    vector.elementAt(i).equals("-") ||
                    vector.elementAt(i).equals("*") ||
                    vector.elementAt(i).equals("/") ||
                    vector.elementAt(i).equals("%") ||
                    vector.elementAt(i).equals("(") ||
                    vector.elementAt(i).equals(")")) {
                if (op.isEmpty()) {
                    op.push(vector.elementAt(i));
                } else {
                    switch (vector.elementAt(i)) {
                        case "+":
                        case "-":
                            while (!op.isEmpty() && !op.peek().equals("(")) {
                                calculateStack(number, op);
                            }
                            op.push(vector.elementAt(i));
                            break;
                        case "*":
                        case "/":
                        case "%":
                            while (!op.isEmpty() && !op.peek().equals("(") && !op.peek().equals("+") && !op.peek().equals("-")) {
                                String a, b;
                                switch (op.peek()) {
                                    case "*":
                                        op.pop();
                                        if (number.size() < 2) {
                                            System.exit(-1);
                                        }
                                        b = parser(number.pop());
                                        a = parser(number.pop());
                                        Main.res.add("%" + Main.counter + " = mul i32 " + a + " , " + b);//eg. %2 = mul i32 %1 , 10
                                        number.push("%" + Main.counter);//计算完的值压栈
                                        break;
                                    case "/":
                                        op.pop();
                                        if (number.size() < 2) {
                                            System.exit(-1);
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
                                            System.exit(-1);
                                        }
                                        b = parser(number.pop());
                                        a = parser(number.pop());
                                        Main.res.add("%" + Main.counter + " = srem i32 " + a + " , " + b);//eg. %2 = srem i32 %1 , 10
                                        number.push("%" + Main.counter);//计算完的值压栈
                                        Main.counter++;
                                        break;
                                }
                            }
                            op.push(vector.elementAt(i));
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
        }
        if (!op.isEmpty() || number.size() >= 2) {
            while (!op.isEmpty()) {
                calculateStack(number, op);
            }
        }
        if (number.size() != 1) {
            System.exit(-1);
        }
        if(number.peek().charAt(0)!='%'){
            return parser(number.peek());
        }
        else return number.peek();
    }

    private static void calculateStack(Stack<String> number, Stack<String> op) {
        String a, b;
        switch (op.peek()) {
            case "*":
                op.pop();
                if (number.size() < 2) {
                    System.exit(-1);
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
                    System.exit(-1);
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
                    System.exit(-1);
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
                    System.exit(-1);
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
                    System.exit(-1);
                }
                b = parser(number.pop());
                a = parser(number.pop());
                Main.res.add("%" + Main.counter + " = sub i32 " + a + " , " + b);//eg. %2 = sub i32 %1 , 10
                number.push("%" + Main.counter);//计算完的值压栈
                Main.counter++;
                break;
        }
    }

    public static String parser(String name) {
        if (name.matches("^-?[0-9]\\d*$")) {
            return name;
        } else if (name.charAt(0) == '%') {
            return name;//eg. i32 %1
        } else {
            Ident ident;
            ident = BlockItemAnalyzer.findIdent(name);
            if (ident != null) {
                int no = ident.no;
                Main.res.add("%"+Main.counter+" = load i32, i32* %"+no);
                ident.setNo(Main.counter);
                Main.counter++;
                return "%" + ident.no;  //eg. %1
            } else {
                System.exit(-9);
                return null;
            }
        }
    }
}

