import java.util.Objects;
import java.util.Vector;

public class BlockItemAnalyzer {
    public static void blockItemAnalyze(Vector<String> vector, boolean inCycle, int head, int tail) {
        if (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("LBrace")) {
                Main.pointer++;
                Main.level++;
                while (Main.pointer < vector.size()) {
                    if (vector.elementAt(Main.pointer).equals("Return")) {
                        ReturnAnalyzer.returnAnalyze(vector);//analyze return sentence
                    } else if (vector.elementAt(Main.pointer).equals("Ident(const)") ||
                            vector.elementAt(Main.pointer).equals("Ident(int)")) {//analyze declare sentence
                        DeclareAnalyzer.declareAnalyze(false);//declare ident
                    } else if (vector.elementAt(Main.pointer).equals("LBrace")) {//block declared
                        blockItemAnalyze(vector, false, 0, 0);
                    } else if (vector.elementAt(Main.pointer).equals("If") ||
                            vector.elementAt(Main.pointer).equals("Else")) {//if sentence
                        CycleAnalyzer.ifAnalyze(vector);
                    } else if (vector.elementAt(Main.pointer).equals("While")) {
                        CycleAnalyzer.whileAnalyze(vector);
                    } else if (vector.elementAt(Main.pointer).equals("Continue") ||
                            vector.elementAt(Main.pointer).equals("Break")) {
                            if (vector.elementAt(Main.pointer).equals("Break")) {
                                CycleAnalyzer.jumpAnalyze(1, head, tail);
                            } else {
                                CycleAnalyzer.jumpAnalyze(0, head, tail);
                            }

                    } else if (vector.elementAt(Main.pointer).charAt(0) == 'I' &&
                            vector.elementAt(Main.pointer + 1).equals("Assign")||
                            vector.elementAt(Main.pointer).charAt(0) == 'I' &&
                                    vector.elementAt(Main.pointer + 1).equals("LBracket")) {//give value sentence
                        Ident ident = findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                        if (ident == null) {
                            System.exit(-3);
                        }
                        boolean isConst = Objects.requireNonNull(ident).infos.elementAt(0).isConst;
                        boolean isArray= ident.infos.elementAt(0).isArray;
                        Assigner.assignValue(vector, isConst, isArray);//evaluate
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
                        ExpAnalyzer.expAnalyze(vector, false, false);//expAnalyzer
                    }
                    if (vector.elementAt(Main.pointer).equals("RBrace")) {
                        //去除全部在该层声明的变量
                        for (int i = 0; i < Main.identList.size(); i++) {
                            Ident ident = Main.identList.elementAt(i);
                            Vector<Info> infos = ident.infos;
                            if (infos.elementAt(0).level == Main.level) {
                                infos.removeElementAt(0);
                            }
                            if (ident.infos.size() == 0) {
                                Main.identList.remove(ident);
                                i--;
                            }
                        }
                        Main.pointer++;
                        Main.level--;
                        return;
                    }
                }
            }
        }
        System.exit(-3);
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
