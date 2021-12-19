import java.util.Vector;

public class ExpAnalyzer {

    public static String expAnalyze(Vector<String> vector,boolean isConst) {
        int flag = 1;
        String res;
        Vector<String> stringVector = new Vector<>();
        //处理过+/-后再次分析，记录初始的i值
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("Semicolon")) {//一条语句结束
                break;
            } else if (vector.elementAt(Main.pointer).equals("Quote")) {
                break;
            } else if (vector.elementAt(Main.pointer).equals("Plus") || vector.elementAt(Main.pointer).equals("Minus")) {// +/-
                LorExpAnalyzer.unaryOpHandle(vector, stringVector);
            } else if (vector.elementAt(Main.pointer).equals("LPar")) {
                stringVector.add("(");
            } else if (vector.elementAt(Main.pointer).equals("RPar")) {
                stringVector.add(")");
            } else if (vector.elementAt(Main.pointer).equals("Mult")) {
                stringVector.add("*");
            } else if (vector.elementAt(Main.pointer).equals("Div")) {
                stringVector.add("/");
            } else if (vector.elementAt(Main.pointer).equals("Mod")) {
                stringVector.add("%");
            } else if (vector.elementAt(Main.pointer).charAt(0) == 'N') {//Number
                //numberAnalyze
                NumberAnalyzer.numberAnalyze(flag, vector, stringVector);
                flag = 1;
            } else if (vector.elementAt(Main.pointer).charAt(0) == 'I'&& !vector.elementAt(Main.pointer).equals("If")) {//Ident
                String identName = vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1);
                if (identName.equals("getint")) {
                    Main.pointer += 3;
                    Main.res.add("%" + Main.counter + " = call i32 @getint()");
                    Main.counter++;
                    String val = "%" + (Main.counter - 1);
                    stringVector.add(val);
                } else if (identName.equals("getch")) {
                    Main.pointer += 3;
                    Main.res.add("%" + Main.counter + " = call i32 @getch()");
                    Main.counter++;
                    String val = "%" + (Main.counter - 1);
                    stringVector.add(val);
                } else {
                    Ident ident;
                    ident = BlockItemAnalyzer.findIdent(identName);
                    if (ident != null) {
                        if(isConst && !ident.infos.elementAt(0).isConst){
                            System.exit(-5);
                        }
                        stringVector.add(identName);
                    } else System.exit(-6);
                }
            }
            Main.pointer++;
        }
        //接下来是表达式求值 表达式已经存在stringVector里了
        res = Calculator.calculate(stringVector);
        return res;
    }
}