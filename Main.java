import java.io.FileNotFoundException;
import java.util.Vector;

public class Main {
    public static Vector<String> res = new Vector<>();
    public static Vector<String> vector = new Vector<>();
    public static Vector<Ident> identList = new Vector<>();
    public static Vector<Function> funcList = new Vector<>();
    public static int pointer = 0;
    public static int counter = 0;//递增的命名号码
    public static int level = 0;//当前层数
    public static boolean inCycle = false;
    public static Vector<Integer>head=new Vector<>();
    public static Vector<Integer>tail=new Vector<>();

    public static void main(String[] args) throws FileNotFoundException {
        Lexer.lexer(vector);//词法分析，将全部token存入向量中。
        addBraces();
        res.add("declare i32 @getint()\n" +
                "declare i32 @getarray(i32*)\n" +
                "declare i32 @getch()\n" +
                "declare void @putint(i32)\n" +
                "declare void @putch(i32)\n" +
                "declare void @memset(i32*, i32, i32)\n"+
                "declare void @putarray(i32,i32*)");
        while (pointer < vector.size()) {
            if (!vector.elementAt(pointer).equals("Ident(int)") &&
                    !vector.elementAt(pointer).equals("Ident(const)") &&
                    !vector.elementAt(pointer).equals("Ident(void)")) {
                System.exit(-1);
            }
            if(vector.elementAt(pointer).equals("Ident(void)")){
                pointer++;
                FunctionAnalyzer.functionHandle(vector,res,true);
            }
            else if(vector.elementAt(pointer).equals("Ident(int)")||
                    vector.elementAt(pointer).equals("Ident(const)")){
                if (vector.elementAt(pointer + 1).equals("Ident(main)")) {
                    pointer++;
                    counter = 1;
                    MainAnalyzer.mainAnalyze(vector, res);
                } else if(vector.elementAt(pointer + 2).equals("LPar")){
                    pointer++;
                    FunctionAnalyzer.functionHandle(vector,res,false);
                }
                else {
                    DeclareAnalyzer.declareAnalyze(true);
                }
            }

        }
    }

    public static void addBraces() {
        int i = pointer;
        int flag = 0;
        for (; i < vector.size(); i++) {
            if ((vector.elementAt(i).equals("If") || vector.elementAt(i).equals("While"))&& vector.elementAt(i + 1).equals("LPar")) {
                flag = 1;
                i++;
                int ii = i;
                while (flag != 0) {
                    ii++;
                    if (vector.elementAt(ii).equals("LPar")) flag++;
                    else if (vector.elementAt(ii).equals("RPar")) flag--;
                }
                if (!vector.elementAt(ii + 1).equals("LBrace")) {
                    vector.add(ii + 1, "LBrace");
                    while (!vector.elementAt(ii).equals("Semicolon")) ii++;
                    vector.add(ii + 1, "RBrace");
                }
            } else if (vector.elementAt(i).equals("Else") && !vector.elementAt(i + 1).equals("If")) {
                if (!vector.elementAt(i + 1).equals("LBrace")) {
                    vector.add(i + 1, "LBrace");
                    int ii = i;
                    while (!vector.elementAt(ii).equals("Semicolon")) ii++;
                    vector.add(ii + 1, "RBrace");
                }
            }
        }
    }
}
