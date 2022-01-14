import java.util.Vector;

public class BlockItemAnalyzer {
    public static void blockItemAnalyze(Vector<String> vector) {
        if (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("LBrace")) {
                Main.res.add("{");
                Main.pointer++;
                while (Main.pointer < vector.size()) {
                    if (vector.elementAt(Main.pointer).equals("Return")) {
                        ReturnAnalyzer.returnAnalyze(vector);//analyze return sentence
                    } else if (vector.elementAt(Main.pointer).equals("Ident(const)") ||
                            vector.elementAt(Main.pointer).equals("Ident(int)")) {//analyze declare sentence
                        DeclareAnalyzer.declareAnalyze();//declare ident, developing now
                    } else if (vector.elementAt(Main.pointer).charAt(0) == 'I' &&
                            vector.elementAt(Main.pointer + 1).equals("Assign")) {//give value sentence
                        Assigner.assignValue(vector);//evaluate(developing now)
                    } else if (vector.elementAt(Main.pointer).equals("Ident(putint)") ||
                            vector.elementAt(Main.pointer).equals("Ident(putch)")) {
                        switch (vector.elementAt(Main.pointer)) {
                            case "Ident(putint)":
                                FunctionHandler.functionHandle(1);
                                break;
                            case "Ident(putch)":
                                FunctionHandler.functionHandle(2);
                                break;
                        }
                    } else {
                        ExpAnalyzer.expAnalyze(vector);//expAnalyzer
                    }
                    if (vector.elementAt(Main.pointer).equals("RBrace")) {
                        Main.res.add("}");
                        Main.pointer++;
                        return;
                    }
                }
            }
        }
        System.exit(-1);
    }

    static Ident findIdent(String identName) {//find if there is an ident declared
        for (Ident ident : Main.identList) {
            if (ident.name.equals(identName)) {
                return ident;
            }
        }
        return null;//there isn't
    }
}
