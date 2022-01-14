import java.util.Vector;

public class Function {
    public String name="";//函数名
    public int paramsNum=0;//参数个数
    public boolean isVoid;
    public Vector<Integer>params=new Vector<>();//记录每一个参数是否为数组，是则为1，不是则为0
    public Vector<String>paramsName=new Vector<>();//记录参数名字
}
