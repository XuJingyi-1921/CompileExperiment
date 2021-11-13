import java.util.Vector;

public class ReturnAnalyzer {
    public static int returnAnalyze(int i, Vector<String>vector){
        //analyze the return sentence.
        i++;
        if(!vector.elementAt(i).equals("Return")){
            System.exit(-1);
        }
        else{
           i=ExpAnalyzer.expAnalyze(i,vector);
        }
        return i;
    }
}
