import java.util.Vector;

public class Assigner {
    public static void assignValue(Vector<String>vector){
        //这部分处理的是形如a=value的表达式，将identList中a的值更新
        String identName= vector.elementAt(Main.pointer).substring(6,vector.elementAt(Main.pointer).length()-1);
        Main.pointer++;
        if(vector.elementAt(Main.pointer).equals("Assign")){
            Main.pointer++;
            String res=ExpAnalyzer.expAnalyze(vector);
            Ident ident = BlockItemAnalyzer.findIdent(identName);
            if(ident!=null){
                if(ident.isConst) System.exit(-4);
                else {
                    Main.res.add("%"+Main.counter+" = alloca i32");
                    Main.res.add("store "+res+", i32* %"+Main.counter);
                    ident.setNo(Main.counter);
                    Main.counter++;
                }
            }
            else System.exit(-5);
            Main.pointer++;
        }
    }
}
