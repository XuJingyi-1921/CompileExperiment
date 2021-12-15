import java.util.Vector;

public class DeclareAnalyzer {
    static void declareAnalyze(Boolean isGlobal) {
        Vector<String> vector = Main.vector;
        if (vector.elementAt(Main.pointer).equals("Ident(const)")) {//定义常量
            Main.pointer++;
            if (!vector.elementAt(Main.pointer).equals("Ident(int)")) {
                System.exit(-30);
            } else Main.pointer++;
            declareIdent(vector, true, isGlobal);
        } else if (vector.elementAt(Main.pointer).equals("Ident(int)")) {//定义变量
            Main.pointer++;
            declareIdent(vector, false, isGlobal);
        }

    }

    private static void declareIdent(Vector<String> vector, boolean isConst, boolean isGlobal) {
        Boolean ifIdentIsNew = true;
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).charAt(0) == 'I') {//Ident
                Ident ident;
                if (BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1)) == null) {
                    ident = new Ident();
                } else {
                    ident = BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                    ifIdentIsNew = false;//已经存在于变量表中了
                    if (ident.infos.elementAt(0).level == Main.level) {//在同一层重复声明变量
                        System.exit(-31);
                    }
                }
                Info info = new Info(isConst, Main.counter, Main.level);
                ident.setName(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                ident.infos.add(0, info);
                if (!isGlobal) {
                    Main.res.add("%" + Main.counter + " = alloca i32");//eg. %1 = alloca i32)
                }
                Main.pointer++;
                Main.counter++;
                if (isGlobal) {
                    if (vector.elementAt(Main.pointer).equals("Semicolon")) {
                        Main.res.add("@" + (Main.counter - 1) + " = dso_local global i32 0");
                    } else if (vector.elementAt(Main.pointer).equals("Assign")) {
                        Main.pointer++;
                        if (vector.elementAt(Main.pointer).substring(0, 7).equals("Number(")) {
                            int val = Integer.parseInt(vector.elementAt(Main.pointer).substring(7, vector.elementAt(Main.pointer).length() - 1));
                            Main.res.add("@" + (Main.counter - 1) + " = dso_local global i32 " + val);
                            Main.pointer++;
                            Main.identList.add(ident);
                            if (vector.elementAt(Main.pointer).equals("Quote")) {
                                Main.pointer++;
                            } else if (vector.elementAt(Main.pointer).equals("Quote")) {
                                Main.identList.add(ident);
                                Main.pointer++;
                            }
                        }
                    }
                } else {
                    switch (vector.elementAt(Main.pointer)) {
                        case "Assign": //声明后直接赋值语句
                            Main.pointer++;
                            String val = ExpAnalyzer.expAnalyze(vector, isConst);
                            //Main.res.add("%" + Main.counter + " = alloca i32");
                            Main.res.add("store i32 " + val + " , i32* %" + (Main.counter - 1));
                            if (ifIdentIsNew) Main.identList.add(ident);
                            if (vector.elementAt(Main.pointer).equals("Quote")) {
                                Main.pointer++;
                            }
                            break;
                        case "Quote":
                            Main.pointer++;
                            if (ifIdentIsNew) Main.identList.add(ident);
                            break;
                        case "Semicolon":
                            if (ifIdentIsNew) Main.identList.add(ident);
                            break;
                    }
                }
            }
            if (vector.elementAt(Main.pointer).equals("Semicolon")) {
                Main.pointer++;
                break;
            }
        }
    }

}
