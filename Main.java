import java.io.FileNotFoundException;
import java.util.Vector;

public class Main {
    public static Vector<String> res = new Vector<>();
    public static Vector<String> vector = new Vector<>();
    public static Vector<Ident> identList = new Vector<>();
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
                        res.add("declare i32 @getint()\n" +
                                "declare i32 @getarray(i32*)\n" +
                                "declare i32 @getch()\n" +
                                "declare void @putint(i32)\n" +
                                "declare void @putch(i32)\n" +
                                "declare void @putarray(i32,i32*)");
                        res.add("define dso_local i32 @main()");
                        BlockItemAnalyzer.blockItemAnalyze(vector);//分析语句块代码
                        for (int l = 0; l < res.size(); l++) {
                            System.out.println(res.elementAt(l));
                        }
                        System.exit(0);

                    }

                }
            }
        }
    }
}
