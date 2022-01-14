import java.util.Vector;

public class GlobalExpAnalyzer {
    public static int expAnalyze(Vector<String> vector, Boolean isArray, Boolean isConstArray) {
        int flag = 1;
        int res;
        Vector<String> stringVector = new Vector<>();
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("Semicolon")) {//一条语句结束
                break;
            } else if (vector.elementAt(Main.pointer).equals("Quote")
                    || vector.elementAt(Main.pointer).equals("RBracket")
                    || vector.elementAt(Main.pointer).equals("RBrace")) {// ]
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
            } else if (vector.elementAt(Main.pointer).charAt(0) == 'I' && !vector.elementAt(Main.pointer).equals("If")) {//Ident
                String identName = vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1);
                Ident ident;
                ident = BlockItemAnalyzer.findIdent(identName);
                if (ident != null) {
                    if (!ident.infos.elementAt(0).isConst && isArray && isConstArray) {
                        System.exit(-5);
                    }
                    if(ident.infos.elementAt(0).isArray){
                        Info info=ident.infos.elementAt(0);
                        int div=info.div;
                        int []position=ExpAnalyzer.getPosition(info,vector);//求坐标
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
                        Main.res.add("%x"+Main.counter+" = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* @x"+info.no+",i32 0, i32 "+mount);
                        Main.counter++;
                        Main.res.add("%x"+Main.counter+"= load i32, i32* %x"+(Main.counter-1));
                        identName="%x"+Main.counter;
                        Main.counter++;
                        stringVector.add(identName);
                    } else {
                        stringVector.add(Integer.toString(ident.infos.elementAt(0).value));
                    }
                } else System.exit(-6);

            }
            Main.pointer++;
        }
        res = GlobalCalculator.calculate(stringVector);
        return res;
    }
}
