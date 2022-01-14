import java.util.Vector;

public class MainAnalyzer {
    public static void mainAnalyze(Vector<String> vector, Vector<String> res) {
        Main.pointer++;
        if (!(vector.elementAt(Main.pointer).equals("LPar") &&
                vector.elementAt(Main.pointer + 1).equals("RPar"))) {
            System.exit(-1);
        } else {
            Main.pointer += 2;
            res.add("define dso_local i32 @main()");
            res.add("{");
            BlockItemAnalyzer.blockItemAnalyze(vector, false, 0, 0);//分析语句块代码
            res.add("}");
            for (int l = 0; l < res.size(); l++) {
                if (res.elementAt(l).length() > 7) {
                    if (res.elementAt(l).startsWith("block") && res.elementAt(l + 1).equals("}")) {
                        continue;
                    }
                    else if(res.elementAt(l).startsWith("br")&&res.elementAt(l-1).startsWith("br")){
                        continue;
                    }
                    else if(res.elementAt(l).startsWith("br")&&res.elementAt(l-1).startsWith("{")){
                        continue;
                    }
                }
                System.out.println(res.elementAt(l));
            }
            System.exit(0);
        }
    }
}