import java.io.FileNotFoundException;
import java.util.Vector;

public class Main {
    public static Vector<String> res = new Vector<>();
    public static Vector<String> vector = new Vector<>();
    public static void main(String[] args) throws FileNotFoundException {
        Lexer.lexer(vector);//词法分析，将全部token存入向量中。
        if(vector.size()<6){
            System.exit(-1);
        }
        else{
            int i=0;
            if(!vector.elementAt(i).equals("Ident(int)")){
                System.exit(-1);
            }
            else{
                i++;
                if(!vector.elementAt(i).equals("Ident(main)")){
                    System.exit(-1);
                }
                else{
                    i++;
                    if(!(vector.elementAt(i).equals("LPar")&&
                            vector.elementAt(i+1).equals("RPar"))){
                        System.exit(-1);
                    }
                    else{
                        i+=2;
                        res.add("define dso_local i32 @main()");
                        //分析语句块代码
                        BlockAnalyzer.blockAnalyze(i,vector);
                        for(int l=0;l<res.size();l++){
                            System.out.println(res.elementAt(l));
                        }
                        System.exit(0);

                    }

                }
            }
        }
    }
}
