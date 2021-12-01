import java.util.Vector;

public class ReturnAnalyzer {
    public static void returnAnalyze(Vector<String>vector){
        //analyze the return sentence.
        Main.pointer++;
        String res= ExpAnalyzer.expAnalyze(vector);
        Main.res.add("ret i32 " + res);
        if(!vector.elementAt(Main.pointer).equals("Semicolon")){
            System.exit(-22);
        }
        else {
            Main.pointer++;
        }
    }
}
