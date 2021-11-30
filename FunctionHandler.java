public class FunctionHandler {
    public static void functionHandle(int flag){
        Main.pointer++;
        if(!Main.vector.elementAt(Main.pointer).equals("LPar")){
            System.exit(-10);
        }
        else if(flag==1){
            String res=ExpAnalyzer.expAnalyze(Main.vector);
            Main.res.add("call void @putint(i32 "+res+")");
        }
        else if(flag==2){
            String res=ExpAnalyzer.expAnalyze(Main.vector);
            Main.res.add("call void @putch(i32 "+res+")");
        }
        Main.pointer++;
    }
}
