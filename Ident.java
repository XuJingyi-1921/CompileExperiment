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
    int value=0;
    int level;
    Boolean isArray=false;//是否是数组
    int div=0;//维数
    int[] divs={0,0,0,0,0,0,0,0,0,0};//每一维的长度，目前先定义十维
    int length=0;
    Vector<Integer> divValues=new Vector<Integer>();//将多维数组转化为一维，值存储在divValues里

    public Info(Boolean isConst,int no,int level){
        this.isConst=isConst;
        this.no=no;
        this.level=level;
    }
    public Info(Boolean isConst,int no,int level,int div,int[]divs){
        this.isConst=isConst;
        this.no=no;
        this.level=level;
        this.div=div;
        this.divs=divs;
    }
}
