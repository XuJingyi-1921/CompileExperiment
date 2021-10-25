import java.util.Vector;

public class BlockAnalyzer {
    public static int blockAnalyze(int i, Vector<String> vector){
        if(i+3<vector.size()){
            if(!vector.elementAt(i).equals("LBrace")){//{
                System.exit(-1);
            }
            else{
                //analyze return sentence
                i=ExpAnalyzer.expAnalyze(i,vector);
                if(vector.elementAt(i).equals("RBrace")){
                    Main.res.add("}");
                    i++;
                    return i;
                }
                else System.exit(-1);
            }
        }
        else {
           System.exit(-1);
        }
        return i;
    }
}
