import java.util.Vector;

public class NumberAnalyzer {
    public static int numberAnalyze(int i,int flag, Vector<String> vector,Vector<String>vector2){
        if(vector.elementAt(i).equals("Number(0)")&&vector.elementAt(i+1).matches("Ident[(][xX][0-9a-fA-F]+[)]")) {//hexadecimal
            //handle
            int dec=Integer.parseInt(vector.elementAt(i+1).substring(7,vector.elementAt(i+1).length()-1),16);
            if(flag<0)dec*=-1;
            vector2.add(Integer.toString(dec));
            //Main.res.add("ret i32 "+dec);
            i+=1;
            return i;
        }
        else if(vector.elementAt(i).matches("Number[(]0[0-9]+[)]")){//octalNum
            //handle
            int dec=Integer.parseInt(vector.elementAt(i).substring(7,vector.elementAt(i).length()-1),8);
            if(flag<0)dec*=-1;
            vector2.add(Integer.toString(dec));
            //Main.res.add("ret i32 "+dec);
            return i;
        }
        else if(vector.elementAt(i).matches("Number[(][0-9]+[)]")) {//decimal
            int dec=Integer.parseInt(vector.elementAt(i).substring(7,vector.elementAt(i).length()-1));
            if(flag<0)dec*=-1;
            vector2.add(Integer.toString(dec));
            //Main.res.add("ret i32 "+vector.elementAt(i).substring(7,vector.elementAt(i).length()-1));
           return i;
        }
        else {
            System.exit(-1);
        }
        return i;
    }
}