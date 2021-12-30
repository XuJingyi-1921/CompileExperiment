import java.util.Vector;

public class DeclareAnalyzer {
    static void declareAnalyze(Boolean isGlobal) {
        Vector<String> vector = Main.vector;
        if (vector.elementAt(Main.pointer).equals("Ident(const)")) {//定义常量
            Main.pointer++;
            if (!vector.elementAt(Main.pointer).equals("Ident(int)")) {
                System.exit(-30);
            } else Main.pointer++;
            declareIdent(vector, true, isGlobal);
        } else if (vector.elementAt(Main.pointer).equals("Ident(int)")) {//定义变量
            Main.pointer++;
            declareIdent(vector, false, isGlobal);
        }

    }

    private static void declareIdent(Vector<String> vector, boolean isConst, boolean isGlobal) {
        Boolean ifIdentIsNew = true;
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).charAt(0) == 'I') {//Ident
                Ident ident;
                if (BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1)) == null) {
                    ident = new Ident();
                } else {
                    ident = BlockItemAnalyzer.findIdent(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                    ifIdentIsNew = false;//已经存在于变量表中了
                    assert ident != null;
                    if (ident.infos.elementAt(0).level == Main.level) {//在同一层重复声明变量
                        System.exit(-31);
                    }
                }
                ident.setName(vector.elementAt(Main.pointer).substring(6, vector.elementAt(Main.pointer).length() - 1));
                Info info;
                if (vector.elementAt(Main.pointer + 1).equals("LBracket")) {//数组
                    Main.pointer++;
                    info = arrayAnalyze(vector, isConst);
                    info.isArray=true;
                    ident.infos.add(0, info);
                    Main.counter++;
                    int mount = 1;
                    for (int i = 0; i < info.div; i++) {
                        mount *= info.divs[i];
                    }//转化为一维数组 mount是元素总个数
                    info.length=mount;
                    if (isGlobal) {
                        //全局数组变量声明，此时pointer在;/,/=处，下一步处理赋值语句
                        if (isConst) {
                            if (vector.elementAt(Main.pointer).equals("Quote")
                                    || vector.elementAt(Main.pointer).equals("Semicolon")) {
                                Main.res.add("@" + (info.no) + " = dso_local constant [" + mount + " x i32] zeroinitializer");
                                for(int i=0;i<mount;i++){
                                    info.divValues.add(0);
                                }
                                Main.identList.add(ident);
                            } else if (vector.elementAt(Main.pointer).equals("Assign")) {
                                Assigner.assignArray(vector, info, true, true,mount);//developing
                                Main.identList.add(ident);
                            }
                        } else {
                            if (vector.elementAt(Main.pointer).equals("Quote")
                                    || vector.elementAt(Main.pointer).equals("Semicolon")) {
                                Main.res.add("@" + (info.no) + " = dso_local global [" + mount + " x i32] zeroinitializer");
                                for(int i=0;i<mount;i++){
                                    info.divValues.add(0);
                                }
                            } else if (vector.elementAt(Main.pointer).equals("Assign")) {
                                Assigner.assignArray(vector, info, true, false, mount);
                            }
                            Main.identList.add(ident);
                        }
                    } else {
                        //局部数组变量声明，分析过程同上
                        if (isConst) {
                            if (vector.elementAt(Main.pointer).equals("Quote")
                                    || vector.elementAt(Main.pointer).equals("Semicolon")) {
                                Main.res.add("%" + info.no + " = alloca [" + mount + " x i32]");
                                Main.res.add("%"+Main.counter+" = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* %"+info.no+",i32 0, i32 0");
                                Main.res.add("call void @memset(i32* %" + Main.counter + ", i32 0, i32 " + mount * 4 + ")");
                                Main.counter++;
                            } else if (vector.elementAt(Main.pointer).equals("Assign")) {
                                Main.res.add("%" + info.no + " = alloca [" + mount + " x i32]");
                                Assigner.assignArray(vector, info, false, true, mount);
                            }
                        } else {
                            if (vector.elementAt(Main.pointer).equals("Quote")
                                    || vector.elementAt(Main.pointer).equals("Semicolon")) {
                                Main.res.add("%" + info.no + " = alloca [" + mount + " x i32]");
                                Main.res.add("%"+Main.counter+" = getelementptr ["+info.length+" x i32], ["+info.length+" x i32]* %"+info.no+",i32 0, i32 0");
                                Main.res.add("call void @memset(i32* %" + Main.counter + ", i32 0, i32 " + mount * 4 + ")");
                                Main.counter++;
                            } else if (vector.elementAt(Main.pointer).equals("Assign")) {
                                Main.res.add("%" + info.no + " = alloca [" + mount + " x i32]");
                                Assigner.assignArray(vector, info, false, false, mount);
                            }
                        }
                        Main.identList.add(ident);
                    }
                } else {
                    info = new Info(isConst, Main.counter, Main.level);
                    ident.infos.add(0, info);
                    if (!isGlobal) {
                        Main.res.add("%" + Main.counter + " = alloca i32");//eg. %1 = alloca i32)
                    }
                    Main.pointer++;
                    Main.counter++;
                    if (isGlobal) {
                        if (vector.elementAt(Main.pointer).equals("Semicolon")) {
                            Main.res.add("@" + (Main.counter - 1) + " = dso_local global i32 0");
                            ident.infos.elementAt(0).value = 0;
                            Main.identList.add(ident);
                        } else if (vector.elementAt(Main.pointer).equals("Assign")) {
                            Main.pointer++;
                            int val = GlobalExpAnalyzer.expAnalyze(vector, false, false);
                            Main.res.add("@" + (Main.counter - 1) + " = dso_local global i32 " + val);
                            ident.infos.elementAt(0).value = val;
                            Main.identList.add(ident);
                            if (vector.elementAt(Main.pointer).equals("Quote")) {
                                Main.pointer++;
                            }
                        } else if (vector.elementAt(Main.pointer).equals("Quote")) {
                            ident.infos.elementAt(0).value = 0;
                            Main.identList.add(ident);
                            Main.pointer++;
                        }
                    } else {
                        switch (vector.elementAt(Main.pointer)) {
                            case "Assign": //声明后直接赋值语句
                                Main.pointer++;
                                String val = ExpAnalyzer.expAnalyze(vector, isConst);
                                Main.res.add("store i32 " + val + " , i32* %" + (ident.infos.elementAt(0).no));
                                if (ifIdentIsNew) Main.identList.add(ident);
                                if (vector.elementAt(Main.pointer).equals("Quote")) {
                                    Main.pointer++;
                                }
                                break;
                            case "Quote":
                                Main.pointer++;
                                if (ifIdentIsNew) Main.identList.add(ident);
                                break;
                            case "Semicolon":
                                if (ifIdentIsNew) Main.identList.add(ident);
                                break;
                        }
                    }
                }
            }
            if (vector.elementAt(Main.pointer).equals("Semicolon")) {
                Main.pointer++;
                break;
            }
        }
    }

    private static Info arrayAnalyze(Vector<String> vector, Boolean isConst) {
        int div = 0;
        int[] divs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        while (Main.pointer < vector.size()) {
            if (vector.elementAt(Main.pointer).equals("Quote")
                    || vector.elementAt(Main.pointer).equals("Semicolon")
                    || vector.elementAt(Main.pointer).equals("Assign")) {
                return new Info(isConst, Main.counter, Main.level, div, divs);
            }
            Main.pointer++;
            int val = GlobalExpAnalyzer.expAnalyze(vector, true, isConst);
            Main.pointer++;
            div++;//计算维数
            divs[div - 1] = val;//记录每一维的长度
        }
        return null;
    }
}
