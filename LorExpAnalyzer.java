import java.util.Vector;

public class LorExpAnalyzer {
    public static String lorExpAnalyze(Vector<String> vector, boolean isConst) {//条件语句表达式计算
        int flag = 1;
        String res;
        Vector<String> stringVector = new Vector<>();
        //处理过+/-后再次分析，记录初始的i值
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("LBrace")) {//判断语句结束
                break;
            } else if (vector.elementAt(Main.pointer).equals("Plus") || vector.elementAt(Main.pointer).equals("Minus")) {// +/-
                unaryOpHandle(vector, stringVector);
                Main.pointer++;
                continue;
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
            } else if (vector.elementAt(Main.pointer).equals("Lt")
                    || vector.elementAt(Main.pointer).equals("LE")
                    || vector.elementAt(Main.pointer).equals("Gt")
                    || vector.elementAt(Main.pointer).equals("GE")
                    || vector.elementAt(Main.pointer).equals("Eq")
                    || vector.elementAt(Main.pointer).equals("NEq")
                    || vector.elementAt(Main.pointer).equals("OR")
                    || vector.elementAt(Main.pointer).equals("AND")
                    || vector.elementAt(Main.pointer).equals("NOT")) {
                stringVector.add(vector.elementAt(Main.pointer));//逻辑运算相关运算符直接进入表达式
            } else if (vector.elementAt(Main.pointer).charAt(0) == 'N') {//Number
                NumberAnalyzer.numberAnalyze(flag, vector, stringVector);
                flag = 1;
            } else if (vector.elementAt(Main.pointer).charAt(0) == 'I' && !vector.elementAt(Main.pointer).equals("If")) {//Ident
                String identName = vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1);
                if (identName.equals("getint")) {
                    Main.pointer += 3;
                    Main.res.add("%" + Main.counter + " = call i32 @getint()");
                    Main.counter++;
                    return "%" + (Main.counter - 1);
                } else if (identName.equals("getch")) {
                    Main.pointer += 3;
                    Main.res.add("%" + Main.counter + " = call i32 @getch()");
                    Main.counter++;
                    return "%" + (Main.counter - 1);
                } else {
                    Ident ident;
                    ident = BlockItemAnalyzer.findIdent(identName);
                    if (ident != null) {
                        if (isConst && !ident.infos.elementAt(0).isConst) {
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

    public static void unaryOpHandle(Vector<String> vector, Vector<String> stringVector) {
        if (vector.elementAt(Main.pointer - 1).equals("RPar") ||
                vector.elementAt(Main.pointer - 1).charAt(0) == 'N' &&
                        vector.elementAt(Main.pointer - 1).charAt(1) == 'u' ||
                vector.elementAt(Main.pointer - 1).charAt(0) == 'I') {
            if (vector.elementAt(Main.pointer).equals("Plus")) stringVector.add("+");
            else stringVector.add("-");
        } else if (vector.elementAt(Main.pointer - 1).equals("LPar") ||
                vector.elementAt(Main.pointer - 1).equals("Plus") ||
                vector.elementAt(Main.pointer - 1).equals("Minus") ||
                vector.elementAt(Main.pointer - 1).equals("Mult") ||
                vector.elementAt(Main.pointer - 1).equals("Mod") ||
                vector.elementAt(Main.pointer - 1).equals("Return") ||
                vector.elementAt(Main.pointer - 1).equals("Div") ||
                vector.elementAt(Main.pointer - 1).equals("Assign") ||
                vector.elementAt(Main.pointer - 1).equals("Eq") ||
                vector.elementAt(Main.pointer - 1).equals("NEq") ||
                vector.elementAt(Main.pointer - 1).equals("Gt") ||
                vector.elementAt(Main.pointer - 1).equals("Lt") ||
                vector.elementAt(Main.pointer - 1).equals("GE") ||
                vector.elementAt(Main.pointer - 1).equals("LE") ||
                vector.elementAt(Main.pointer - 1).equals("AND") ||
                vector.elementAt(Main.pointer - 1).equals("OR")) {
            if (vector.elementAt(Main.pointer).equals("Minus")) {
                stringVector.add("-1");
                stringVector.add("*");
            }
        } else {
            System.exit(-7);
        }
    }
}
