import java.util.Vector;

public class FunctionAnalyzer {
    public static void functionHandle(Vector<String> vector, Vector<String> res, boolean isVoid) {
        Main.counter = 0;
        String name = vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1);
        String declare = "";
        Function function = new Function();
        function.name = name;
        function.isVoid=isVoid;
        Main.pointer++;
        if (!vector.elementAt(Main.pointer).equals("LPar")) {
            System.exit(-80);
        } else {
            Main.pointer++;
            if (isVoid) {
                declare += ("define dso_local void @" + name + "(");
            } else {
                declare += ("define dso_local i32 @" + name + "(");
            }
            Main.level++;
            while (!vector.elementAt(Main.pointer).equals("RPar")) {
                Main.pointer++;
                if (!vector.elementAt(Main.pointer + 1).equals("LBracket")) {//int param
                    declare += "i32 %x" + Main.counter;
                    function.paramsNum++;
                    function.params.add(0);
                    Ident ident;
                    {
                        boolean ifIdentIsNew = true;
                        if (BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1)) == null) {
                            ident = new Ident();
                        } else {
                            ident = BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                            ifIdentIsNew = false;//已经存在于变量表中了
                            assert ident != null;
                            if (ident.infos.elementAt(0).level == Main.level) {//在同一层重复声明变量
                                System.exit(-81);
                            }
                        }
                        ident.setName(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                        Info info;
                        info = new Info(false, Main.counter, Main.level);
                        info.isParam = true;
                        ident.infos.add(0, info);
                        Main.pointer++;
                        Main.counter++;
                        if (vector.elementAt(Main.pointer).equals("Quote")) {
                            declare += ", ";
                            Main.pointer++;
                            if (ifIdentIsNew) Main.identList.add(ident);
                        } else if (vector.elementAt(Main.pointer).equals("RPar")) {
                            if (ifIdentIsNew) Main.identList.add(ident);
                        }
                    }
                } else {
                    {
                        declare += "i32 %x" + Main.counter;
                        function.paramsNum++;
                        function.params.add(1);
                        Ident ident;
                        boolean ifIdentIsNew = true;
                        if (BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1)) == null) {
                            ident = new Ident();
                        } else {
                            ident = BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                            ifIdentIsNew = false;//已经存在于变量表中了
                            assert ident != null;
                            if (ident.infos.elementAt(0).level == Main.level) {//在同一层重复声明变量
                                System.exit(-81);
                            }
                        }
                        ident.setName(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                        Info info;
                        int[] divs = new int[10];
                        info = new Info(false, Main.counter, Main.level, 0, divs);
                        info.isParam = true;
                        info.isArray=true;
                        Main.pointer++;
                        Main.counter++;
                        while (!vector.elementAt(Main.pointer).equals("Quote")&&!vector.elementAt(Main.pointer).equals("RPar")){
                            Main.pointer++;
                            if(vector.elementAt(Main.pointer).equals("RBracket")){
                                info.div++;
                            }
                        }
                        ident.infos.add(0, info);
                        if (vector.elementAt(Main.pointer).equals("Quote")) {
                            declare += ", ";
                            Main.pointer++;
                            if (ifIdentIsNew) Main.identList.add(ident);
                        } else if (vector.elementAt(Main.pointer).equals("RPar")) {
                            if (ifIdentIsNew) Main.identList.add(ident);
                        }
                    }
                }
            }
            declare+=")";
            Main.level--;
            res.add(declare);
            res.add("{");
            Main.funcList.add(function);
            Main.pointer++;
            BlockItemAnalyzer.blockItemAnalyze(vector, false, 0, 0);//分析语句块代码
            res.add("}");
            res.add("");
            Main.counter=0;
        }
    }
    public static Function findFunction(String name){
        for(Function f :Main.funcList){
            if(f.name.equals(name))return f;
        }
        System.exit(-82);
        return null;
    }
}
