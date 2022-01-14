import java.util.Objects;
import java.util.Vector;

public class ExpAnalyzer {

    public static String expAnalyze(Vector<String> vector, boolean isConst, boolean isParams) {
        int flag = 1;
        String res;
        Vector<String> stringVector = new Vector<>();
        //处理过+/-后再次分析，记录初始的i值
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("Semicolon")) {//一条语句结束
                break;
            } else if (vector.elementAt(Main.pointer).equals("Quote") ||
                    vector.elementAt(Main.pointer).equals("RBrace") ||
                    vector.elementAt(Main.pointer).equals("RBracket") ||
                    (vector.elementAt(Main.pointer).equals("RPar") && isParams)) {
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
                if (identName.equals("getint")) {
                    Main.pointer += 2;
                    Main.res.add("%x" + Main.counter + " = call i32 @getint()");
                    Main.counter++;
                    String val = "%x" + (Main.counter - 1);
                    stringVector.add(val);
                } else if (identName.equals("getch")) {
                    Main.pointer += 2;
                    Main.res.add("%x" + Main.counter + " = call i32 @getch()");
                    Main.counter++;
                    String val = "%x" + (Main.counter - 1);
                    stringVector.add(val);
                } else if (identName.equals("getarray")) {
                    int counter = FunctionHandler.functionHandle(3);
                    stringVector.add("%x" + counter);
                } else if (identName.equals("putarray")) {
                    FunctionHandler.functionHandle(4);
                } else if (vector.elementAt(Main.pointer + 1).equals("LPar")) {
                    Function function = FunctionAnalyzer.findFunction(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                    Main.pointer += 2;//现在pointer在(的下一个
                    String s = paramsHandle(function, vector);
                    if(function.isVoid){
                        Main.res.add(s);
                    }
                    else{
                        stringVector.add(s);
                    }
                } else {
                    Ident ident;
                    ident = BlockItemAnalyzer.findIdent(identName);
                    if (ident != null) {
                        if (ident.infos.elementAt(0).isParam&&!ident.infos.elementAt(0).isArray) {
                            identName = "%x" + ident.infos.elementAt(0).no;
                        } else if (isConst && !ident.infos.elementAt(0).isConst) {
                            System.exit(-5);
                        }
                        if (ident.infos.elementAt(0).isArray) {
                            Info info = Objects.requireNonNull(BlockItemAnalyzer.findIdent(identName)).infos.elementAt(0);
                            Main.pointer++;
                            String[] position = ExpAnalyzer.getPositionString(info, vector);
                            Main.pointer--;
                            int div = info.div;
                            int count = Main.counter;
                            int mount = Main.counter + 1;
                            if (div > 1) {
                                Main.res.add("%x" + count + " = alloca i32");
                                Main.res.add("store i32 1, i32* %x" + count);
                                Main.counter++;
                                Main.res.add("%x" + mount + " = alloca i32");
                                Main.res.add("store i32 0, i32* %x" + mount);
                                Main.counter++;
                                for (int i = div - 1; i > 0; i--) {
                                    //count*=info.divs[i];
                                    Main.res.add("%x" + Main.counter + " = load i32, i32* %x" + count);
                                    Main.counter++;
                                    Main.res.add("%x" + Main.counter + " = mul i32 %x" + (Main.counter - 1) + " , " + info.divs[i]);
                                    Main.res.add("store i32 %x" + Main.counter + ", i32* %x" + count);
                                    Main.counter++;

                                    //mount+=position[i-1]*count;
                                    Main.res.add("%x" + Main.counter + " = load i32, i32* %x" + count);
                                    Main.counter++;
                                    Main.res.add("%x" + Main.counter + " = mul i32 %x" + (Main.counter - 1) + " , " + position[i - 1]);
                                    Main.counter++;
                                    Main.res.add("%x" + Main.counter + " = load i32, i32* %x" + mount);
                                    Main.counter++;
                                    Main.res.add("%x" + Main.counter + " = add i32 %x" + (Main.counter - 2) + " , %x" + (Main.counter - 1));
                                    Main.res.add("store i32 %x" + Main.counter + ", i32* %x" + mount);
                                    Main.counter++;
                                }
                                //mount+=position[div-1];//求转换为一维数组之后的下标
                                Main.res.add("%x" + Main.counter + " = load i32, i32* %x" + mount);
                                Main.counter++;
                                Main.res.add("%x" + Main.counter + " = add i32 %x" + (Main.counter - 1) + " , " + position[div - 1]);
                                Main.res.add("store i32 %x" + Main.counter + ", i32* %x" + mount);
                                Main.counter++;
                            } else {
                                mount--;
                                Main.res.add("%x" + mount + " = alloca i32");
                                Main.res.add("store i32 " + position[0] + ", i32* %x" + mount);
                                Main.counter++;
                            }
                            //找到这个值，加入stringVector中
                            Main.res.add("%x" + Main.counter + " = load i32, i32* %x" + mount);
                            Main.counter++;
                            if (info.level == 0) {
                                Main.res.add("%x" + Main.counter + " = getelementptr [" + info.length + " x i32], [" + info.length + " x i32]* @x" + info.no + ",i32 0, i32 %x" + (Main.counter - 1));
                            } else {
                                Main.res.add("%x" + Main.counter + " = getelementptr [" + info.length + " x i32], [" + info.length + " x i32]* %x" + info.no + ",i32 0, i32 %x" + (Main.counter - 1));
                            }
                            Main.counter++;
                            Main.res.add("%x" + Main.counter + " = load i32, i32* %x" + (Main.counter - 1));
                            identName = "%x" + Main.counter;
                            Main.counter++;
                        }
                        stringVector.add(identName);
                    } else System.exit(-6);
                }
            }
            Main.pointer++;
        }
        //接下来是表达式求值 表达式已经存在stringVector里了
        if (stringVector.size() == 0) {
            Main.pointer++;
            return "";
        }
        res = Calculator.calculate(stringVector);
        return res;
    }

    public static int[] getPosition(Info info, Vector<String> vector) {
        int div = info.div;
        int flag = 0;
        Main.pointer++;
        int i = 0;
        int[] position = new int[10];
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("LBracket")) {
                flag++;
                Main.pointer++;
                position[i] = GlobalExpAnalyzer.expAnalyze(vector, true, false);
                i++;
            }
            if (vector.elementAt(Main.pointer).equals("RBracket")) {
                flag--;
                if (flag == 0) {
                    Main.pointer++;
                    continue;
                } else {
                    return position;
                }
            }
            if (i < div) System.exit(-71);
            else return position;
        }
        System.exit(-72);
        return null;
    }

    public static String[] getPositionString(Info info, Vector<String> vector) {
        int flag = 0;
        int div = info.div;
        //Main.pointer++;
        int i = 0;
        String[] position = new String[10];
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("LBracket")) {
                flag++;
                Main.pointer++;
                position[i] = ExpAnalyzer.expAnalyze(vector, info.isConst, false);
                i++;
            }
            if (vector.elementAt(Main.pointer).equals("RBracket")) {
                flag--;
                if (flag == 0) {
                    Main.pointer++;
                    continue;
                } else {
                    return position;
                }
            }
            if (i < div) System.exit(-71);
            else return position;
        }
        System.exit(-72);
        return null;
    }

    public static String paramsHandle(Function function, Vector<String> vector) {
        int i = 0;
        int res=0;
        String s;
        if (function.isVoid) {
           s = " call void @" + function.name + "(";
        }
        else {
            s = "%x"+Main.counter +" = call i32 @" + function.name + "(";
            res=Main.counter;
            Main.counter++;
        }
        while (!vector.elementAt(Main.pointer).equals("RPar")) {
            if (function.params.elementAt(i) == 0) {//int param
                s += ExpAnalyzer.expAnalyze(vector, false, true);
            } else {
                Ident ident = BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                if (ident == null) {
                    System.exit(-83);
                } else {
                    Info info = ident.infos.elementAt(0);
                        Main.res.add("%x" + Main.counter + " = getelementptr [" + info.length + " x i32], [" + info.length + " x i32]* %x" + info.no + ",i32 0, i32 0");
                        s += "%x" + Main.counter;
                        Main.counter++;
                }
                while(!vector.elementAt(Main.pointer).equals("Quote")&&!vector.elementAt(Main.pointer).equals("RPar")){
                    Main.pointer++;
                }
            }
            i++;
            if(vector.elementAt(Main.pointer).equals("Quote")){
                s+=", ";
                Main.pointer++;
            }
        }
        s+=")";
        if(!function.isVoid){
            Main.res.add(s);
        }
        return (function.isVoid? s: "%x"+res);
    }
}