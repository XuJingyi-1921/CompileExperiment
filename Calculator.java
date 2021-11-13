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
    public static int calculate(Vector<String>vector){
        Stack<String>number=new Stack<>();//操作数栈
        Stack<String>op=new Stack<>();//运算符栈
        for(int i=0;i<vector.size();i++){
            if(vector.elementAt(i).matches("^-?[1-9]\\d*$")){//数字，直接入栈
                number.push(vector.elementAt(i));
            }
            else if(vector.elementAt(i).equals("+")||
                    vector.elementAt(i).equals("-")||
                    vector.elementAt(i).equals("*")||
                    vector.elementAt(i).equals("/")||
                    vector.elementAt(i).equals("%")||
                    vector.elementAt(i).equals("(")||
                    vector.elementAt(i).equals(")")){
                if(op.isEmpty()){
                    op.push(vector.elementAt(i));
                }
                else{
                    if(vector.elementAt(i).equals("+")||vector.elementAt(i).equals("-")){
                        while(!op.isEmpty()&&!op.peek().equals("(")){
                            calculateStack(number, op);
                        }
                        op.push(vector.elementAt(i));
                    }
                    else if(vector.elementAt(i).equals("*")||vector.elementAt(i).equals("/")||
                            vector.elementAt(i).equals("%")){
                        while(!op.isEmpty()&&!op.peek().equals("(")&&!op.peek().equals("+")&&!op.peek().equals("-")){
                            int a,b,c;
                            switch (op.peek()){
                                case "*":
                                    op.pop();
                                    if(number.size()<2){System.exit(-1);}
                                    b=Integer.parseInt(number.pop());
                                    a=Integer.parseInt(number.pop());
                                    c=a*b;
                                    number.push(Integer.toString(c));
                                    break;
                                case "/":
                                    op.pop();
                                    if(number.size()<2){System.exit(-1);}
                                    b=Integer.parseInt(number.pop());
                                    a=Integer.parseInt(number.pop());
                                    c=a/b;
                                    number.push(Integer.toString(c));
                                    break;
                                case "%":
                                    op.pop();
                                    if(number.size()<2){System.exit(-1);}
                                    b=Integer.parseInt(number.pop());
                                    a=Integer.parseInt(number.pop());
                                    c=a%b;
                                    number.push(Integer.toString(c));
                                    break;
                            }
                        }
                        op.push(vector.elementAt(i));
                    }
                    else if(vector.elementAt(i).equals("(")) {
                        op.push("(");
                    }
                    else if(vector.elementAt(i).equals(")")){
                        if(op.search("(")==-1){
                            System.exit(-8);
                        }
                        while(!op.peek().equals("(")){
                            calculateStack(number, op);
                        }
                        op.pop();
                    }
                }
            }
        }
        if(!op.isEmpty()||number.size()>=2){
            while(!op.isEmpty()){
                calculateStack(number,op);
            }
        }
        if(number.size()!=1){
            System.exit(-1);
        }
        return Integer.parseInt(number.peek());
    }

    private static void calculateStack(Stack<String> number, Stack<String> op) {
        int a,b,c;
        switch (op.peek()){
            case "*":
                op.pop();
                if(number.size()<2){System.exit(-1);}
                b=Integer.parseInt(number.pop());
                a=Integer.parseInt(number.pop());
                c=a*b;
                number.push(Integer.toString(c));
                break;
            case "/":
                op.pop();
                if(number.size()<2){System.exit(-1);}
                b=Integer.parseInt(number.pop());
                a=Integer.parseInt(number.pop());
                c=a/b;
                number.push(Integer.toString(c));
                break;
            case "%":
                op.pop();
                if(number.size()<2){System.exit(-1);}
                b=Integer.parseInt(number.pop());
                a=Integer.parseInt(number.pop());
                c=a%b;
                number.push(Integer.toString(c));
                break;
            case "+":
                op.pop();
                if(number.size()<2){System.exit(-1);}
                b=Integer.parseInt(number.pop());
                a=Integer.parseInt(number.pop());
                c=a+b;
                number.push(Integer.toString(c));
                break;
            case "-":
                op.pop();
                if(number.size()<2){System.exit(-1);}
                b=Integer.parseInt(number.pop());
                a=Integer.parseInt(number.pop());
                c=a-b;
                number.push(Integer.toString(c));
                break;
        }
    }
}
