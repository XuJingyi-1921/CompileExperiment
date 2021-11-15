import java.util.Vector;

public class BlockAnalyzer {
    public static int blockAnalyze(int i, Vector<String> vector){
        if(i+3<vector.size()){
            if(!vector.elementAt(i++).equals("LBrace")){//{
                System.exit(-1);
            }
            else{//analyze the return sentence.
                if(!vector.elementAt(i++).equals("Return")){
                    System.exit(-1);
                }
                else{
                    if(vector.elementAt(i).equals("Number(0)")&&vector.elementAt(i+1).matches("Ident[(][xX][0-9a-fA-F]+[)]")) {//hexadecimal
                        //handle
                        int dec=Integer.parseInt(vector.elementAt(i+1).substring(8,vector.size()-1),16);
                        Main.res.add("ret i32 "+dec);
                        i+=2;
                        if(vector.elementAt(i++).equals("Semicolon")){
                            System.exit(-1);
                        }
                        else{
                            if(vector.elementAt(i++).equals("RBrace")){
                                return i;
                            }
                            else System.exit(-1);
                        }
                    }
                    else if(vector.elementAt(i).matches("Number[(]0[0-9]+[)]")){//octalNum
                        //handle
                        int dec=Integer.parseInt(vector.elementAt(i).substring(7,vector.size()-1),8);
                        Main.res.add("ret i32 "+dec);
                        i++;
                        if(vector.elementAt(i++).equals("Semicolon")){
                            System.exit(-1);
                        }
                        else{
                            if(vector.elementAt(i++).equals("RBrace")){
                                return i;
                            }
                            else System.exit(-1);
                        }
                    }
                    else if(vector.elementAt(i).matches("Number[(][0-9]+[)]")) {//decimal
                        //handle
                        Main.res.add("ret i32 "+vector.elementAt(i));
                        i++;
                        if(vector.elementAt(i++).equals("Semicolon")){
                            System.exit(-1);
                        }
                        else{
                            if(vector.elementAt(i++).equals("RBrace")){
                                return i;
                            }
                            else System.exit(-1);
                        }
                    }
                    else {
                        System.exit(-1);
                    }
                }
            }
        }
        else {
           System.exit(-1);
        }
        return i;
    }
}