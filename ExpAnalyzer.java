import java.util.Vector;

public class ExpAnalyzer {

    public static String expAnalyze(Vector<String> vector,boolean isConst) {
        int flag = 1;
        String res;
        Vector<String> stringVector = new Vector<>();
        //处理过+/-后再次分析，记录初始的i值
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("Semicolon")) {//一条语句结束
                break;
            } else if (vector.elementAt(Main.pointer).equals("Quote") ||
                    vector.elementAt(Main.pointer).equals("RBrace") ||
                    vector.elementAt(Main.pointer).equals("RBracket")){
                break;
            } else if (vector.elementAt(Main.pointer).equals("Plus") || vector.elementAt(Main.pointer).equals("Minus")) {// +/-
                LorExpAnalyzer.unaryOpHandle(vector, stringVector);
            } else if (vector.elementAt(Main.pointer).equals("LPar")) {
                stringVector.add("(");
            } else if (vector.elementAt(Main.pointer).equals("RPar")) {
                stringVector.add(")");
            } else if (vector.elementAt(Main.pointer).equals("Mult")) {
                stringVector.add("*");
            } else if (vector.elementAt(Main.pointer).equals("Div")) {
                stringVector.add("/");
            } else if (vector.elementAt(Main.pointer).equals("Mod")) {
                stringVector.add("%");
            } else if (vector.elementAt(Main.pointer).charAt(0) == 'N') {//Number
                //numberAnalyze
                NumberAnalyzer.numberAnalyze(flag, vector, stringVector);
                flag = 1;
            } else if (vector.elementAt(Main.pointer).charAt(0) == 'I'&& !vector.elementAt(Main.pointer).equals("If")) {//Ident
                String identName = vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1);
                if (identName.equals("getint")) {
                    Main.pointer += 2;
                    Main.res.add("%" + Main.counter + " = call i32 @getint()");
                    Main.counter++;
                    String val = "%" + (Main.counter - 1);
                    stringVector.add(val);
                } else if (identName.equals("getch")) {
                    Main.pointer += 2;
                    Main.res.add("%" + Main.counter + " = call i32 @getch()");
                    Main.counter++;
                    String val = "%" + (Main.counter - 1);
                    stringVector.add(val);
                } else {
                    Ident ident;
                    ident = BlockItemAnalyzer.findIdent(identName);
                    if (ident != null) {
                        if(isConst && !ident.infos.elementAt(0).isConst){
                            System.exit(-5);
                        }
                        if(ident.infos.elementAt(0).isArray){
                           Info info=ident.infos.elementAt(0);
                           int div=info.div;
                           int []position=getPosition(info,vector);//求坐标
                            Main.pointer--;
                            int mount=0;
                            int count=1;
                            if(div>1){
                                for(int i=div-1;i>0;i--){
                                    count*=info.divs[i];
                                    mount+=position[i-1]*count;
                                }
                                mount+=position[div-1];//求转换为一维数组之后的下标
                            }
                            else{
                                mount=position[0];
                            }
                            //找到这个值，加入stringVector中
                            if(info.level==0){
                                Main.res.add("%"+Main.counter+" = getelementptr i32, i32* @"+info.no+", i32 "+mount);
                            }
                            else Main.res.add("%"+Main.counter+" = getelementptr i32, i32* %"+info.no+", i32 "+mount);
                            Main.counter++;
                            Main.res.add("%"+Main.counter+"= load i32, i32* %"+(Main.counter-1));
                            identName="%"+Main.counter;
                            Main.counter++;
                        }
                        stringVector.add(identName);
                    } else System.exit(-6);
                }
            }
            Main.pointer++;
        }
        //接下来是表达式求值 表达式已经存在stringVector里了
        if(stringVector.size()==0){
            Main.pointer++;
            return "";
        }
        res = Calculator.calculate(stringVector);
        return res;
    }
    public static int[] getPosition(Info info ,Vector<String>vector){
        int div= info.div;
        Main.pointer++;
        int i=0;
        int[] position= new int[10];
        while(Main.pointer<vector.size()){
            if(vector.elementAt(Main.pointer).equals("LBracket")){
                Main.pointer++;
                position[i]=GlobalExpAnalyzer.expAnalyze(vector,true,false);
                i++;
            }
            if(vector.elementAt(Main.pointer).equals("RBracket")){
                Main.pointer++;
                continue;
            }
            if(i<div)System.exit(-71);
            else return position;
        }
        System.exit(-72);
        return null;
    }
    public static String[] getPositionString(Info info ,Vector<String>vector){
        int div= info.div;
        //Main.pointer++;
        int i=0;
        String[] position= new String[10];
        while(Main.pointer<vector.size()){
            if(vector.elementAt(Main.pointer).equals("LBracket")){
                Main.pointer++;
                position[i]=ExpAnalyzer.expAnalyze(vector,info.isConst);
                i++;
            }
            if(vector.elementAt(Main.pointer).equals("RBracket")){
                Main.pointer++;
                continue;
            }
            if(i<div)System.exit(-71);
            else return position;
        }
        System.exit(-72);
        return null;
    }
}