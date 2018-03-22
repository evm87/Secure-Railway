package RailGraphActual;

public class Signal {
    private int id;
    private boolean clear;
    private int block;
    private boolean direction;
    private static int count;
    public Signal(boolean dir,int blockNo) {
        id = count;
        count++;
        this.id = id;
        this.block = blockNo;
        this.direction = dir;
    }
    public int getId() {
        return id;
    }
    public boolean isDownDirection() {
        return direction;
    }
    public void setDownDirection(boolean direction) {
        this.direction = direction;
    }
    public void setClearState(boolean t) {
        clear = t;
    }
    public boolean isClear() {
        return clear;
    }
    public int getBlock() {
        return block;
    }
}
