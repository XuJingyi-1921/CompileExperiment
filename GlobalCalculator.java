import java.util.Stack;
import java.util.Vector;

public class GlobalCalculator {
    public static int calculate(Vector<String>vector){
        int count=0;
        for(int i=0;i< vector.size();i++){
            if(vector.elementAt(i).equals("(")) count++;
            else if (vector.elementAt(i).equals(")")) count--;
        }
        if(count!=0)System.exit(-1);
        Stack<String> number=new Stack<>();//操作数栈
        Stack<String>op=new Stack<>();//运算符栈
        for(int i=0;i<vector.size();i++){
            if(vector.elementAt(i).matches("^-?[0-9]\\d*$")){//数字，直接入栈
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
