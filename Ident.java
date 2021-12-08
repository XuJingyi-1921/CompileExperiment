public class Ident {
    String name = "";
    int value;
    Boolean isConst = false;
    int no;//alloca number
    int temp = 0;

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

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
