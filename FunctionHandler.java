import java.util.Objects;

public class FunctionHandler {
    public static int functionHandle(int flag){
        Main.pointer++;
        if(!Main.vector.elementAt(Main.pointer).equals("LPar")){
            System.exit(-10);
        }
        else if(flag==1){//putint
            String res=ExpAnalyzer.expAnalyze(Main.vector,false, false);
            Main.res.add("call void @putint(i32 "+res+")");
        }
        else if(flag==2){//putch
            String res=ExpAnalyzer.expAnalyze(Main.vector,false, false);
            Main.res.add("call void @putch(i32 "+res+")");
        }
        else if(flag==3){//getarray
            Main.pointer++;
            String identName = Main.vector.elementAt(Main.pointer).substring(6, Main.vector.elementAt(Main.pointer).length() - 1);
            Info info = Objects.requireNonNull(BlockItemAnalyzer.findIdent(identName)).infos.elementAt(0);
            for(int i=Main.pointer;i<Main.vector.size();i++){
                if(Main.vector.elementAt(i).equals("RPar")){
                    Main.vector.add(i,"LBracket");
                    Main.vector.add(i+1,"Number(0)");
                    Main.vector.add(i+2,"RBracket");
                    break;
                }
            }
            Main.pointer++;
            String []position=ExpAnalyzer.getPositionString(info,Main.vector);
            //此时指针在右括号处
            int div= info.div;
            int count=Main.counter;
            int mount=Main.counter+1;
            getPointer(div,count,mount,info,position);
            Main.res.add("%x"+Main.counter+" = call i32 @getarray(i32* %x"+(Main.counter-1)+")");
            Main.counter++;
            return Main.counter-1;
        }
        else if(flag==4){//putarray
            Main.pointer++;
            String n=ExpAnalyzer.expAnalyze(Main.vector,false, false);
            Main.pointer++;
            String identName = Main.vector.elementAt(Main.pointer).substring(6, Main.vector.elementAt(Main.pointer).length() - 1);
            Info info = Objects.requireNonNull(BlockItemAnalyzer.findIdent(identName)).infos.elementAt(0);
            for(int i=Main.pointer;i<Main.vector.size();i++){
                if(Main.vector.elementAt(i).equals("RPar")){
                    Main.vector.add(i,"LBracket");
                    Main.vector.add(i+1,"Number(0)");
                    Main.vector.add(i+2,"RBracket");
                    break;
                }
            }
            Main.pointer++;
            String []position=ExpAnalyzer.getPositionString(info,Main.vector);
            //此时指针在右括号处
            int div= info.div;
            int count=Main.counter;
            int mount=Main.counter+1;
            getPointer(div,count,mount,info,position);
            Main.res.add("%x"+Main.counter+" = call i32 @putarray(i32 "+n+",i32* %x"+(Main.counter-1)+")");
            Main.counter++;
            return Main.counter-1;
        }
        Main.pointer++;
        return 0;
    }
    public static void getPointer(int div, int count, int mount, Info info, String  []position){
        if(div>1){
            Main.res.add("%x"+count+" = alloca i32");
            Main.res.add("store i32 1, i32* %x"+count);
            Main.counter++;
            Main.res.add("%x"+mount+" = alloca i32");
            Main.res.add("store i32 0, i32* %x"+mount);
            Main.counter++;
            for(int i=div-1;i>0;i--){
                //count*=info.divs[i];
                Main.res.add("%x"+Main.counter+" = load i32, i32* %x"+count);
                Main.counter++;
                Main.res.add("%x" + Main.counter + " = mul i32 %x" + (Main.counter-1) + " , " + info.divs[i]);
                Main.res.add("store i32 %x"+Main.counter+", i32* %x"+count);
                Main.counter++;

                //mount+=position[i-1]*count;
                Main.res.add("%x"+Main.counter+" = load i32, i32* %x"+count);
                Main.counter++;
                Main.res.add("%x" + Main.counter + " = mul i32 %x" + (Main.counter-1) + " , " + position[i-1]);
                Main.counter++;
                Main.res.add("%x"+Main.counter+" = load i32, i32* %x"+mount);
                Main.counter++;
                Main.res.add("%x" + Main.counter + " = add i32 %x" + (Main.counter-2) + " , %x" + (Main.counter-1));
                Main.res.add("store i32 %x"+Main.counter+", i32* %x"+mount);
                Main.counter++;
            }
            //mount+=position[div-1];//求转换为一维数组之后的下标
            Main.res.add("%x"+Main.counter+" = load i32, i32* %x"+mount);
            Main.counter++;
            Main.res.add("%x" + Main.counter + " = add i32 %x" + (Main.counter-1) + " , " + position[div-1]);
            Main.counter++;
            Main.res.add("%x"+Main.counter+" = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* %x"+info.no+", i32 0, i32 %x"+(Main.counter-1));
            Main.counter++;

        }
        else{
            Main.res.add("%x"+Main.counter+" = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* %x"+info.no+", i32 0, i32 0");
            Main.counter++;
        }
    }

}
