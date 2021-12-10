import java.util.Vector;

public class DeclareAnalyzer {
    static void declareAnalyze() {
        Vector<String> vector = Main.vector;
        if (vector.elementAt(Main.pointer).equals("Ident(const)")) {//定义常量
            Main.pointer++;
            if (!vector.elementAt(Main.pointer).equals("Ident(int)")) {
                System.exit(-30);
            } else Main.pointer++;
            declareIdent(vector,true);
        }
        else if (vector.elementAt(Main.pointer).equals("Ident(int)")) {//定义变量
            Main.pointer++;
            declareIdent(vector,false);
        }

    }

    private static void declareIdent(Vector<String> vector,boolean isConst) {
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).charAt(0) == 'I') {//Ident
                Ident ident = new Ident();
                ident.setConst(isConst);
                ident.setName(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                Main.res.add("%"+Main.counter+" = alloca i32");//eg. %1 = alloca i32
                ident.setNo(Main.counter);
                Main.pointer++;
                Main.counter++;
                switch (vector.elementAt(Main.pointer)) {
                    case "Assign": //声明后直接赋值语句
                        Main.pointer++;
                        String val = ExpAnalyzer.expAnalyze(vector, isConst);
                        Main.res.add("%" + Main.counter + " = alloca i32");
                        Main.res.add("store i32 " + val + " , i32* %" + Main.counter);
                        ident.setNo(Main.counter);
                        Main.counter++;
                        Main.identList.add(ident);

                        if (vector.elementAt(Main.pointer).equals("Quote")) {
                            Main.pointer++;
                        }
                        break;
                    case "Quote":
                        Main.pointer++;
                        Main.identList.add(ident);
                        break;
                    case "Semicolon":
                        Main.identList.add(ident);
                        break;
                }
            }
            if (vector.elementAt(Main.pointer).equals("Semicolon")) {
                Main.pointer++;
                break;
            }
        }
    }

}
