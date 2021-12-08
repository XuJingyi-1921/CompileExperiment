import java.util.Vector;

public class IfAnalyzer {
    public static void ifAnalyze(Vector<String> vector) {
        int endNum = 0, flag = 0;
        if (vector.elementAt(Main.pointer).equals("If")) {
            if(!vector.elementAt(Main.pointer-1).equals("Else")){
                Main.res.add("br label %block" + (Main.pointer));// I'm thinking
                Main.res.add("");
            }
            Main.res.add("block" + Main.pointer + ":");
            Main.pointer++;
            String s = LorExpAnalyzer.lorExpAnalyze(vector, false);
            if (!vector.elementAt(Main.pointer).equals("LBrace")) {
                System.exit(-40);
            } else {
                for (int i = Main.pointer; i < vector.size(); i++) {
                    if (vector.elementAt(i).equals("LBrace")) {
                        flag++;
                    } else if (vector.elementAt(i).equals("RBrace")) {
                        flag--;
                        if (flag == 0) {
                            if (vector.elementAt(i + 1).equals("Else")) {
                                if (vector.elementAt(i + 2).equals("If")) {
                                    i += 2;
                                    endNum = i;
                                    break;
                                } else if (vector.elementAt(i + 2).equals("LBrace")) {
                                    endNum = i + 2;
                                    break;
                                } else {
                                    System.exit(-50);
                                }
                            } else {
                                endNum = i + 1;
                                break;
                            }
                        }
                    }
                }
                Main.res.add("%" + Main.counter + " = icmp eq i32 " + s + ", 1");
                Main.res.add("br i1 %" + Main.counter + ", label %block" + Main.pointer + ", label %block" + endNum);//testing
                Main.counter++;
                Main.res.add("");
                Main.res.add("block" + Main.pointer + ":");
                BlockItemAnalyzer.blockItemAnalyze(vector);//现在pointer在‘}’+1处
                endNum = 0;
                flag = 0;
                if(!vector.elementAt(Main.pointer).equals("Else")){
                    endNum=Main.pointer;
                    Main.res.add("br label %block" + (endNum));// I'm thinking
                    Main.res.add("");
                    Main.res.add("block" + (endNum) + ":");
                }
                else {
                    for (int i = Main.pointer; i < vector.size(); i++) {
                        if (vector.elementAt(i).equals("LBrace")) {
                            flag++;
                        } else if (vector.elementAt(i).equals("RBrace")) {
                            flag--;
                            if (flag == 0) {
                                if (!vector.elementAt(i + 1).equals("Else")) {
                                    endNum=i+1;//最后一个else右括号结束
                                    break;
                                }
                            }
                        }
                    }
                    Main.res.add("br label %block" + (endNum));// I'm thinking
                    Main.res.add("");
                }
            }
        } else if (vector.elementAt(Main.pointer).equals("Else")) {
            if (vector.elementAt(Main.pointer + 1).equals("If")) {
                Main.pointer++;
                ifAnalyze(vector);
            } else if (vector.elementAt(Main.pointer + 1).equals("LBrace")) {
                Main.pointer++;
                Main.res.add("block "+Main.pointer+":");
                BlockItemAnalyzer.blockItemAnalyze(vector);
                Main.res.add("br label %block" + (Main.pointer));
                Main.res.add("");
               Main.res.add("block" + (Main.pointer) + ":");

            }
        }
    }
}
