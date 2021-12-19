import java.util.Vector;

public class CycleAnalyzer {
    public static void ifAnalyze(Vector<String> vector) {
        int endNum = 0, flag = 0;
        if (vector.elementAt(Main.pointer).equals("If")) {
            if (!vector.elementAt(Main.pointer - 1).equals("Else")) {
                Main.res.add("br label %block" + (Main.pointer));
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
                                endNum = i + 2;//if 后面没有else 这个语句块向下跳转 这里设置为右括号结束的第二个字符位置
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
                BlockItemAnalyzer.blockItemAnalyze(vector, Main.inCycle, Main.head, Main.tail);//现在pointer在‘}’+1处
                endNum = 0;
                flag = 0;
                if (!vector.elementAt(Main.pointer).equals("Else")) {//不是else 对应上面的
                    endNum = Main.pointer + 1;
                    Main.res.add("br label %block" + (endNum));
                    Main.res.add("");
                    Main.res.add("block" + (endNum) + ":");
                } else {
                    for (int i = Main.pointer; i < vector.size(); i++) {
                        if (vector.elementAt(i).equals("LBrace")) {
                            flag++;
                        } else if (vector.elementAt(i).equals("RBrace")) {
                            flag--;
                            if (flag == 0) {
                                if (!vector.elementAt(i + 1).equals("Else")) {
                                    endNum = i + 2;//最后一个else右括号结束 再加1
                                    break;
                                }
                            }
                        }
                    }
                    Main.res.add("br label %block" + (endNum));
                    Main.res.add("");
                }
            }
        } else if (vector.elementAt(Main.pointer).equals("Else")) {
            if (vector.elementAt(Main.pointer + 1).equals("If")) {
                Main.pointer++;
                ifAnalyze(vector);
            } else if (vector.elementAt(Main.pointer + 1).equals("LBrace")) {
                Main.pointer++;
                Main.res.add("block" + Main.pointer + ":");
                BlockItemAnalyzer.blockItemAnalyze(vector, Main.inCycle, Main.head, Main.tail);
                Main.res.add("br label %block" + (Main.pointer + 1));
                Main.res.add("");
                Main.res.add("block" + (Main.pointer + 1) + ":");

            }
        }
    }

    public static void whileAnalyze(Vector<String> vector) {
        int endNum = 0, flag = 0;
        if (vector.elementAt(Main.pointer).equals("While")) {
            Main.inCycle=true;
            int head = Main.pointer, tail;
            Main.res.add("br label %blockw" + (Main.pointer));
            Main.res.add("");
            Main.res.add("blockw" + Main.pointer + ":");
            Main.pointer++;//(
            String s = LorExpAnalyzer.lorExpAnalyze(vector, false);
            if (!vector.elementAt(Main.pointer).equals("LBrace")) {
                System.exit(-40);
            } else {
                for (int i = Main.pointer; i < vector.size(); i++) {
                    if (vector.elementAt(i).equals("LBrace")) {
                        flag++;
                    } else if (vector.elementAt(i).equals("RBrace")) {
                        flag--;
                        if (flag == 0) {//while语句终结,标记位是终结的右括号的位置
                            endNum = i;
                            break;
                        }
                    }
                }
                Main.res.add("%" + Main.counter + " = icmp eq i32 " + s + ", 1");
                Main.res.add("br i1 %" + Main.counter + ", label %blockw" + Main.pointer + ", label %blockw" + endNum);//
                tail = endNum;
                Main.counter++;
                Main.res.add("");
                Main.res.add("blockw" + Main.pointer + ":");
                int prev_pointer = Main.pointer;
                Main.head=head;Main.tail=tail;
                BlockItemAnalyzer.blockItemAnalyze(vector, Main.inCycle, head, tail);//现在pointer在‘}’+1处
                Main.res.add("br label %blockw" + head);
                Main.inCycle=false;
                Main.head=Main.tail=0;
                if (Main.pointer > prev_pointer) {
                    Main.res.add("br label %blockw" + (Main.pointer - 1));
                    Main.res.add("");
                    Main.res.add("blockw" + (Main.pointer - 1) + ":");//继续while语句后面的内容
                }
            }
        }
    }

    public static void jumpAnalyze(int flag, int head, int tail) {
        switch (flag) {
            case 0:
                Main.res.add("br label %blockw"+head);
                break;
            case 1:
                Main.res.add("br label %blockw"+tail);
                break;
        }
        Main.pointer+=2;
    }
}
