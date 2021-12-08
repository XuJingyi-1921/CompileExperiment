import java.io.FileNotFoundException;
import java.util.Vector;

public class Main {
    public static Vector<String> res = new Vector<>();
    public static Vector<String> vector = new Vector<>();
    public static Vector<Ident> identList = new Vector<>();
    public static Vector<Block> blockList = new Vector<>();
    public static int pointer;
    public static int counter = 1;

    public static void main(String[] args) throws FileNotFoundException {
        Lexer.lexer(vector);//词法分析，将全部token存入向量中。
        if (vector.size() < 6) {
            System.exit(-1);
        } else {
            pointer = 0;
            if (!vector.elementAt(pointer).equals("Ident(int)")) {
                System.exit(-1);
            } else {
                pointer++;
                if (!vector.elementAt(pointer).equals("Ident(main)")) {
                    System.exit(-1);
                } else {
                    Main.pointer++;
                    if (!(vector.elementAt(pointer).equals("LPar") &&
                            vector.elementAt(pointer + 1).equals("RPar"))) {
                        System.exit(-1);
                    } else {
                        pointer += 2;
                        addBraces();
                        blockAnalyze();
                        res.add("declare i32 @getint()\n" +
                                "declare i32 @getarray(i32*)\n" +
                                "declare i32 @getch()\n" +
                                "declare void @putint(i32)\n" +
                                "declare void @putch(i32)\n" +
                                "declare void @putarray(i32,i32*)");
                        res.add("define dso_local i32 @main()");
                        res.add("{");
                        BlockItemAnalyzer.blockItemAnalyze(vector);//分析语句块代码
                        res.add("}");
                        for (int l = 0; l < res.size(); l++) {
                            System.out.println(res.elementAt(l));
                        }
                        System.exit(0);

                    }

                }
            }
        }
    }
    public static void blockAnalyze(){
        int p=pointer;
        int stage=0;
        for(;p<vector.size();p++){
            if(vector.elementAt(p).equals("LBrace")){
                stage++;
                Block b=new Block();
                b.location=b.blockNum=p;
                b.stage=stage;
                blockList.add(b);
            }
            else if (vector.elementAt(p).equals("RBrace")){
                stage--;
            }
        }
    }
    public static void addBraces(){
        int i=pointer;
        int flag=0;
        for(;i<vector.size();i++){
            if(vector.elementAt(i).equals("If")&&vector.elementAt(i+1).equals("LPar")){
                flag=1;
                i++;
                while (flag!=0){
                    i++;
                    if(vector.elementAt(i).equals("LPar"))flag++;
                    else if(vector.elementAt(i).equals("RPar"))flag--;
                }
                if(!vector.elementAt(i).equals("LBrace")){
                    vector.add(i+1,"LBrace");
                    while (!vector.elementAt(i).equals("Semicolon"))i++;
                    vector.add(i+1,"RBrace");
                }
            }
            else if(vector.elementAt(i).equals("Else")&&!vector.elementAt(i+1).equals("If")){
                if(!vector.elementAt(i+2).equals("LBrace")) {
                    vector.add(i + 3, "LBrace");
                    while (!vector.elementAt(i).equals("Semicolon"))i++;
                    vector.add(i+1,"RBrace");
                }
            }
        }
    }
}
