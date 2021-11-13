import java.util.Vector;

public class ExpAnalyzer {//需要修改
    public static int expAnalyze(int i, Vector<String> vector){
        i++;
        int flag=1;
        int res;
        Vector<String>stringVector=new Vector<>();
        int pointer=i;//处理过+/-后再次分析，记录初始的i值
      while(i< vector.size()){
          if(vector.elementAt(i).equals("Semicolon")){//一条语句结束
              i++;
              break;
          }
          else if(vector.elementAt(i).equals("Plus")||vector.elementAt(i).equals("Minus")){// +/-
              if (vector.elementAt(i - 1).equals("RPar") ||
                      vector.elementAt(i - 1).charAt(0) == 'N' ||
                      vector.elementAt(i - 1).charAt(0) == 'I') {
                    if(vector.elementAt(i).equals("Plus")) stringVector.add("+");
                    else stringVector.add("-");
              }
              else if(vector.elementAt(i-1).equals("LPar")||
                      vector.elementAt(i-1).equals("Plus")||
                      vector.elementAt(i-1).equals("Minus")||
                      vector.elementAt(i-1).equals("Mult")||
                      vector.elementAt(i-1).equals("Mod")||
                      vector.elementAt(i-1).equals("Return")||
                      vector.elementAt(i-1).equals("Div")){
                  if(vector.elementAt(i).equals("Minus")){
                      flag*=-1;
                  }
                      vector.removeElementAt(i);
                  continue;
              }
              else{
                  System.exit(-1);
              }
          }
          else if(vector.elementAt(i).equals("LPar")){
              stringVector.add("(");
          }
          else if(vector.elementAt(i).equals("RPar")){
              stringVector.add(")");
          }
          else if(vector.elementAt(i).equals("Mult")){
              stringVector.add("*");
          }
          else if(vector.elementAt(i).equals("Div")){
              stringVector.add("/");
          }
          else if(vector.elementAt(i).equals("Mod")){
              stringVector.add("%");
          }
          else if(vector.elementAt(i).charAt(0)=='N'){//Number
              //numberAnalyze
              i=NumberAnalyzer.numberAnalyze(i,flag,vector,stringVector);
              flag=1;
          }
          i++;
      }
      //接下来是表达式求值 表达式已经存在stringVector里了
//        for(int ii=0;ii<stringVector.size();ii++){
//            System.out.print(stringVector.elementAt(ii)+" ");
//        }
        res=Calculator.calculate(stringVector);
        Main.res.add("ret i32 "+res);
        return i;//i已经是分号后面的字符
    }
}