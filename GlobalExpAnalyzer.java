import java.util.Vector;

public class GlobalExpAnalyzer {
    public static int expAnalyze(Vector<String> vector){
        int flag = 1;
        int res;
        Vector<String> stringVector = new Vector<>();
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
                Ident ident;
                    ident = BlockItemAnalyzer.findIdent(identName);
                    if (ident != null) {
                        if(!ident.infos.elementAt(0).isConst){
                            System.exit(-5);
                        }
                        stringVector.add(Integer.toString(ident.infos.elementAt(0).value));
                    } else System.exit(-6);

            }
            Main.pointer++;
        }
        res = GlobalCalculator.calculate(stringVector);
        return res;
    }
}
