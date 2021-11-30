import java.util.Vector;

public class NumberAnalyzer {
    public static void numberAnalyze(int flag, Vector<String> vector, Vector<String>vector2){
        if(vector.elementAt((Main.pointer)).equals("Number(0)")&&vector.elementAt((Main.pointer)+1).matches("Ident[(][xX][0-9a-fA-F]+[)]")) {//hexadecimal
            //handle
            int dec=Integer.parseInt(vector.elementAt((Main.pointer)+1).substring(7,vector.elementAt(Main.pointer+1).length()-1),16);
            if(flag<0)dec*=-1;
            vector2.add(Integer.toString(dec));
            //Main.res.add("ret i32 "+dec);
            Main.pointer+=1;
        }
        else if(vector.elementAt(Main.pointer).matches("Number[(]0[0-9]+[)]")){//octalNum
            //handle
            int dec=Integer.parseInt(vector.elementAt(Main.pointer).substring(7,vector.elementAt(Main.pointer).length()-1),8);
            if(flag<0)dec*=-1;
            vector2.add(Integer.toString(dec));
            //Main.res.add("ret i32 "+dec);
        }
        else if(vector.elementAt(Main.pointer).matches("Number[(][0-9]+[)]")) {//decimal
            int dec=Integer.parseInt(vector.elementAt(Main.pointer).substring(7,vector.elementAt(Main.pointer).length()-1));
            if(flag<0)dec*=-1;
            vector2.add(Integer.toString(dec));
            //Main.res.add("ret i32 "+vector.elementAt(i).substring(7,vector.elementAt(i).length()-1));
        }
        else {
            System.exit(-1);
        }
    }
}