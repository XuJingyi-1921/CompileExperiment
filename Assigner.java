import java.util.Objects;
import java.util.Vector;

public class Assigner {
    public static Vector<Integer> arrayVal;
    public static Vector<String> arrayVal2 ;

    public static void assignValue(Vector<String> vector, boolean isConst,boolean isArray) {
        //这部分处理的是形如a=value的表达式，将identList中a的值更新

        String identName = vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1);
        Main.pointer++;
        if (vector.elementAt(Main.pointer).equals("Assign")) {
            Main.pointer++;
            String res = ExpAnalyzer.expAnalyze(vector, isConst);
            Ident ident = BlockItemAnalyzer.findIdent(identName);
            if (ident != null) {
                if (ident.infos.elementAt(0).isConst) System.exit(-4);
                else {
                    if (ident.infos.elementAt(0).level == 0) {
                        Main.res.add("store i32 " + res + ", i32* @" + ident.infos.elementAt(0).no);
                    } else
                        Main.res.add("store i32 " + res + ", i32* %" + ident.infos.elementAt(0).no);
                }
            } else System.exit(-5);
            Main.pointer++;
        }else if (isArray){
            Info info = Objects.requireNonNull(BlockItemAnalyzer.findIdent(identName)).infos.elementAt(0);
            String []position=ExpAnalyzer.getPositionString(info,vector);
            Main.pointer++;
            int div= info.div;
            int count=Main.counter;
            int mount=Main.counter+1;
            if(div>1){
                Main.res.add("%"+count+" = alloca i32");
                Main.res.add("store i32 1, i32* %"+count);
                Main.counter++;
                Main.res.add("%"+mount+" = alloca i32");
                Main.res.add("store i32 0, i32* %"+mount);
                Main.counter++;
                for(int i=div-1;i>0;i--){
                    //count*=info.divs[i];
                    Main.res.add("%"+Main.counter+" = load i32, i32* %"+count);
                    Main.counter++;
                    Main.res.add("%" + Main.counter + " = mul i32 %" + Main.counter + " , " + info.divs[i]);
                    Main.res.add("store i32 %"+Main.counter+", i32* %"+count);
                    Main.counter++;

                    //mount+=position[i-1]*count;
                    Main.res.add("%"+Main.counter+" = load i32, i32* %"+count);
                    Main.counter++;
                    Main.res.add("%" + Main.counter + " = mul i32 %" + Main.counter + " , " + position[i-1]);
                    Main.counter++;
                    Main.res.add("%"+Main.counter+" = load i32, i32* %"+mount);
                    Main.counter++;
                    Main.res.add("%" + Main.counter + " = add i32 %" + (Main.counter-2) + " , %" + (Main.counter-1));
                    Main.res.add("store i32 %"+Main.counter+", i32* %"+mount);
                    Main.counter++;
                }
                //mount+=position[div-1];//求转换为一维数组之后的下标
                Main.res.add("%"+Main.counter+" = load i32, i32* %"+mount);
                Main.counter++;
                Main.res.add("%" + Main.counter + " = add i32 %" + (Main.counter-1) + " , " + position[div-1]);
                Main.res.add("store i32 %"+Main.counter+", i32* %"+mount);
                Main.counter++;
            }
            else{
                mount--;
                Main.res.add("%"+mount+" = alloca i32");
                Main.res.add("store i32 "+position[0]+", i32* %"+mount);
                Main.counter++;
            }
            //找到这个值，加入stringVector中
            Main.res.add("%"+Main.counter+" = load i32, i32* %"+mount);
            Main.counter++;
            if(info.level==0){
                Main.res.add("%" + Main.counter + " = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* @" + info.no + ", i32 %" + (Main.counter - 1));
            }
            else {
                Main.res.add("%" + Main.counter + " = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* %" + info.no + ", i32 %" + (Main.counter - 1));
            }
            int p=Main.counter;
            Main.counter++;
           String res = ExpAnalyzer.expAnalyze(vector,isConst);
           Main.res.add("store i32 " + res + ", i32* %" + p);

        }
    }

    public static void assignArray(Vector<String> vector, Info info, Boolean isGlobal, Boolean isConst, int mount) {
        arrayVal= new Vector<>();
        arrayVal2=new Vector<>();
        Main.pointer++;//此时pointer在=号后的第一个，是{
        String s;
        if (isGlobal) {//全局数组，初值可计算，注意判断isConst
            handle(info.div, info.div,info.divs, vector, true, isConst);//此时数组中的全部元素已经存储在arrayVal中了
            if(arrayVal.size()==0){
                for(int i=0;i<mount;i++){
                    arrayVal.add(0);
                }
            }
            info.divValues = arrayVal;
            if (isConst) {
                s = ("@" + (info.no) + " = dso_local constant [" + mount + " x i32] [");
            } else {
                s = ("@" + (info.no) + " = dso_local global [" + mount + " x i32] [");
            }
            for (int i = 0; i < mount; i++) {
                if (i < mount - 1)
                    s += " i32 " + arrayVal.elementAt(i) + ",";
                else
                    s += " i32 " + arrayVal.elementAt(i) + "]";
            }
            Main.res.add(s);
        } else if (isConst) {//类似上一情况  这里我的const和变量写成一样的了
            handle(info.div, info.div,info.divs, vector, false, true);
            if(arrayVal.size()==0){
                for(int i=0;i<mount;i++){
                    arrayVal.add(0);
                }
            }
            Main.res.add("%" + info.no + " = alloca [" + mount + " x i32]");
            Main.res.add("%" + Main.counter + " = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* %" + info.no + ", i32 0");
            Main.res.add("call void @memset(i32* %" + Main.counter + ", i32 0, i32 " + mount * 4 + ")");
            for (int i = 0; i < mount; i++, Main.counter++) {
                Main.res.add("%" + Main.counter + " = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* " + info.no + ", i32 " + i);
                Main.res.add("store i32 " + arrayVal.elementAt(i) + ", i32* %" + Main.counter);
                info.divValues.add(arrayVal.elementAt(i));
            }

        } else {//局部数组，isConst时也要计算出来，非常量可以不是已知数字
            handle(info.div, info.div, info.divs, vector, false, false);//数组中的全部元素已经存储在arrayVal2中了
            if(arrayVal2.size()==0){
                for(int i=0;i<mount;i++){
                    arrayVal2.add("0");
                }
            }
            Main.res.add("%" + info.no + " = alloca [" + mount + " x i32]");
            Main.res.add("%" + Main.counter + " = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* %" + info.no + ", i32 0");
            Main.res.add("call void @memset(i32* %" + Main.counter + ", i32 0, i32 " + mount * 4 + ")");
            Main.counter++;
            for (int i = 0; i < mount; i++, Main.counter++) {
                Main.res.add("%" + Main.counter + " = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* " + info.no + ", i32 " + i);
                Main.res.add("store i32 " + arrayVal2.elementAt(i) + ", i32* %" + Main.counter);
            }
        }
    }

    public static void handle(int div, int totaldiv, int[] divs, Vector<String> vector, Boolean isGlobal, Boolean isConst) {
        if (div == 1) {
            Main.pointer++;// 跳过 {
            for (int ii = 0; ii < divs[totaldiv - 1]; ii++) {
                if (isGlobal || isConst) {
                    if (vector.elementAt(Main.pointer).equals("Semicolon") && arrayVal.size() < divs[totaldiv - 1]) {
                        for (int t = 0; t < divs[totaldiv - 1] - arrayVal.size(); t++) arrayVal.add(0);
                        Main.pointer--;
                        break;
                    } else {
                        int val = GlobalExpAnalyzer.expAnalyze(vector, true, isConst);
                        arrayVal.add(val);
                        if(ii<divs[totaldiv-1]-1)Main.pointer++;//跳过 ","
                    }
                } else {//可以不是已知的值
                    if (vector.elementAt(Main.pointer).equals("Semicolon") && arrayVal2.size() < divs[totaldiv - 1]) {
                        for (int t = 0; t < divs[totaldiv - 1] - arrayVal2.size(); t++) arrayVal2.add("0");
                        Main.pointer--;
                        break;
                    }
                    else {
                        String val = ExpAnalyzer.expAnalyze(vector, false);
                        arrayVal2.add(val);
                        if(ii<divs[totaldiv-1]-1)Main.pointer++;//跳过 ","
                    }
                }
            }
            Main.pointer++;// 跳过 }
        } else {
            Main.pointer++;//跳过 {
            if(vector.elementAt(Main.pointer).equals("RBrace")){
                Main.pointer++;
                return;
            }
            for (int ii = 0; ii < divs[totaldiv - div]; ii++) {
                handle(div - 1, totaldiv,divs, vector, isGlobal, isConst);
                if(ii<divs[totaldiv-div]-1)Main.pointer++;//跳过 ","
            }
            Main.pointer++;
        }//最后pointer在}的下一个位置
    }
}
