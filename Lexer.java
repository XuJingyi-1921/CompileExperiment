import java.util.Hashtable;
import java.util.Scanner;
public class Lexer {
    static Hashtable chars=new Hashtable();
    public static void printToken(String s){
        //System.out.println(s);
        if(chars.get(s)!=null){
            String res= (String) chars.get(s);
            System.out.println(res);
        }
        else{
            if(Character.isDigit(s.charAt(0))){
                System.out.println("Number("+s+")");
            }
            else{
                System.out.println("Ident("+s+")");
                }
        }
    }
    public static void main(String[] args) {
        String TOKEN="";
        char CHAR;
        chars.put("if","If");
        chars.put("else","Else");
        chars.put("while","While");
        chars.put("break","Break");
        chars.put("continue","Continue");
        chars.put("return","Return");
        chars.put("=","Assign");
        chars.put(";","Semicolon");
        chars.put("(","LPar");
        chars.put(")","RPar");
        chars.put("{","LBrace");
        chars.put("}","RBrace");
        chars.put("+","Plus");
        chars.put("*","Mult");
        chars.put("/","Div");
        chars.put("<","Lt");
        chars.put(">","Gt");
        chars.put("==","Eq");
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNextLine()){
           String LINE= scanner.nextLine();
            CHAR=LINE.charAt(0);
           for (int i=0;i<LINE.length();){
               if(Character.isDigit(CHAR)){//数字开头 可能是无符号整数
                   TOKEN+=CHAR;
                   while(true){
                       i++;//指针向后移动一位
                       if(i>=LINE.length()){
                           printToken(TOKEN);
                           // 已经读完这一行了
                           break;
                       }
                       else{//没有读完
                           CHAR=LINE.charAt(i);
                           if(Character.isDigit(CHAR)){
                               TOKEN+=CHAR;
                           }
                           else{
                               printToken(TOKEN);
                               break;
                           }
                       }
                   }
                   TOKEN="";//初始化TOKEN
               }
               if(Character.isLetter(CHAR)||CHAR=='_'){//非数字 可能是标识符或关键字
                   TOKEN+=CHAR;
                    while(true){
                        i++;
                        if(i>=LINE.length()){
                            Lexer.printToken(TOKEN);
                            // 已经读完这一行了
                            break;
                        }
                        else{//没有读完
                            CHAR=LINE.charAt(i);
                            if(Character.isLetter(CHAR)||Character.isDigit(CHAR)||CHAR=='_'){
                                TOKEN+=CHAR;
                            }
                            else{
                                printToken(TOKEN);
                                break;
                            }
                        }
                    }
                    TOKEN="";//初始化TOKEN
               }
               if (CHAR == '=') {
                   TOKEN+=CHAR;
                       i++;
                       if(i>=LINE.length()){
                           printToken(TOKEN);
                           break;
                       }
                       else{//没有读完
                           CHAR=LINE.charAt(i);
                           if(CHAR=='='){
                               TOKEN+=CHAR;
                               printToken(TOKEN);
                           }
                           else{
                               printToken(TOKEN);
                           }
                       }
                   TOKEN="";//初始化TOKEN
               }
               if(CHAR==';'||CHAR=='('||CHAR==')'||CHAR=='{'||CHAR=='}'
                       ||CHAR=='+'||CHAR=='*'||CHAR=='/'||CHAR=='<'||CHAR=='>'){
                   TOKEN+=CHAR;
                   i++;
                   if(i>=LINE.length()){
                       printToken(TOKEN);
                       TOKEN="";
                       break;
                   }
                   else{//没有读完
                       CHAR=LINE.charAt(i);
                       printToken(TOKEN);
                       TOKEN="";
                   }
               }
               if(CHAR==' '){
                   i++;
                   if(i>=LINE.length()){
                       TOKEN="";
                       break;
                   }
                   else{//没有读完
                       CHAR=LINE.charAt(i);
                   }
               }
               else if(chars.get(""+CHAR) == null && !Character.isDigit(CHAR) && !Character.isLetter(CHAR) && CHAR != '_'){
                   System.out.println("Err");
                   System.exit(0);
               }
           }
        }

    }
}
