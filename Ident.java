public class Ident {
    String name="";
    int value;
    Boolean isConst=false;
    int no;

    public void setConst(Boolean aConst) {
        isConst = aConst;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
