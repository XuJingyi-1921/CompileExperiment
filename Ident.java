import java.util.Vector;

public class Ident {
    String name = "";
    Vector<Info> infos=new Vector<>();

    public void setName(String name) {
        this.name = name;
    }
}
class Info{
    Boolean isConst=false;
    int no;// alloca number
    int level;
    public Info(Boolean isConst,int no,int level){
        this.isConst=isConst;
        this.no=no;
        this.level=level;
    }
}
