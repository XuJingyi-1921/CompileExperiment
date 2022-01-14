import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;

public class Lexer {
    static Hashtable<String, String> chars= new Hashtable<>();
     static void printToken(String s, Vector<String>vector){
        //System.out.println(s);
        if(chars.get(s)!=null){
            String res= chars.get(s);
            vector.add(res);
        //    System.out.println(res);
        }
        else{
            if(Character.isDigit(s.charAt(0))){
                vector.add("Number("+s+")");
            //    System.out.println("Number("+s+")");
            }
            else{
                vector.add("Ident("+s+")");
            //    System.out.println("Ident("+s+")");
                }
        }
    }
    public static void lexer(Vector<String>vector) throws FileNotFoundException {
        String TOKEN="";
        char CHAR;
        int flag=0;
        chars.put("if","If");
        chars.put("else","Else");
        chars.put("while","While");
        chars.put("break","Break");
        chars.put("continue","Continue");
        chars.put("return","Return");
        chars.put("=","Assign");
        chars.put(";","Semicolon");
        chars.put(",","Quote");
        chars.put("(","LPar");
        chars.put(")","RPar");
        chars.put("{","LBrace");
        chars.put("}","RBrace");
        chars.put("+","Plus");
        chars.put("-","Minus");
        chars.put("*","Mult");
        chars.put("/","Div");
        chars.put("%","Mod");
        chars.put("<","Lt");
        chars.put(">","Gt");
        chars.put("==","Eq");
        Scanner scanner=new Scanner(new FileReader("in.txt"));
        while(scanner.hasNextLine()){
           StringBuilder LINE= new StringBuilder(scanner.nextLine());
           StringBuilder LINE1=new StringBuilder();
           LINE.append("   ");
           for(int i=0;i<LINE.length();i++){
               if(flag==0){
                   if(LINE.charAt(i)=='/'&&LINE.charAt(i+1)=='*'){//多行注释状态开始
                       flag+=1;
                       i+=2;
                   } else if (LINE.charAt(i) == '/' && LINE.charAt(i + 1) == '/') {//先遇见单行注释
                       break;
                   }
               }
               if(flag>0){
                   if(LINE.charAt(i)=='*'&&LINE.charAt(i+1)=='/'){
                   flag--;
                   i+=2;
                   }
               }
               if(flag==0) LINE1.append(LINE.charAt(i));
           }
            LINE=LINE1;
           if(LINE.length()>0){
               CHAR=LINE.charAt(0);
               for (int i=0;i<LINE.length();){
                   if(Character.isDigit(CHAR)){//数字开头 可能是无符号整数
                       TOKEN+=CHAR;
                       while(true){
                           i++;//指针向后移动一位
                           if(i>=LINE.length()){
                               printToken(TOKEN,vector);
                               // 已经读完这一行了
                               break;
                           }
                           else{//没有读完
                               CHAR=LINE.charAt(i);
                               if(Character.isDigit(CHAR)){
                                   TOKEN+=CHAR;
                               }
                               else{
                                   printToken(TOKEN,vector);
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
                               Lexer.printToken(TOKEN,vector);
                               // 已经读完这一行了
                               break;
                           }
                           else{//没有读完
                               CHAR=LINE.charAt(i);
                               if(Character.isLetter(CHAR)||Character.isDigit(CHAR)||CHAR=='_'){
                                   TOKEN+=CHAR;
                               }
                               else{
                                   printToken(TOKEN,vector);
                                   break;
                               }
                           }
                       }
                       TOKEN="";//初始化TOKEN
                   }
                   if (CHAR == '=') {
                       TOKEN+=CHAR;
                       while(true){
                           i++;
                           if(i>=LINE.length()){
                               Lexer.printToken(TOKEN,vector);
                               // 已经读完这一行了
                               break;
                           }
                           else{//没有读完
                               CHAR=LINE.charAt(i);
                               if(CHAR=='='&&TOKEN.length()<=1){
                                   TOKEN+=CHAR;
                               }
                               else{
                                   printToken(TOKEN,vector);
                                   break;
                               }
                           }
                       }
                       TOKEN="";//初始化TOKEN
                   }
                   if(CHAR==';'||CHAR=='('||CHAR==','||CHAR==')'||CHAR=='{'||CHAR=='}'
                           ||CHAR=='+'||CHAR=='-'||CHAR=='*'||CHAR=='/'||CHAR=='%'||CHAR=='<'||CHAR=='>'){
                       TOKEN+=CHAR;
                       i++;
                       if(i>=LINE.length()){
                           printToken(TOKEN,vector);
                           TOKEN="";
                           break;
                       }
                       else{//没有读完
                           CHAR=LINE.charAt(i);
                           printToken(TOKEN,vector);
                           TOKEN="";
                       }
                   }
                   if(CHAR==' '||CHAR==9||CHAR==10||CHAR==11){
                       i++;
                       if(i>=LINE.length()){
                           TOKEN="";
                           break;
                       }
                       else{//没有读完
                           CHAR=LINE.charAt(i);
                       }
                   }
                   else if(chars.get(""+CHAR) == null && !Character.isDigit(CHAR) && !Character.isLetter(CHAR) && CHAR != '_'&&CHAR>=32){
                   //    System.out.println("Err");
                       System.exit(-1);
                   }
               }
           }
        }
        if(flag==1){
            System.exit(-1);
        }
    }
}
