package RailGraphActual;

import java.util.ArrayList;

public class Block {
    private static int count;
    private int id;
    private int prevBlock;
    private int blockPlus;
    private int blockNeg;
    private int blockType;
    private boolean hasSignal;
    
    public ArrayList<Signal> getSignals() 
    {
        return signals;
    }
    
    public void setSignals(ArrayList<Signal> signals) 
    {
        this.signals = signals;
    }
    
    private ArrayList<Signal>signals = new ArrayList<Signal>();
    
    public Block(int prev, int type) 
    {
        id = count;
        count++;
        prevBlock = prev;
        blockType = type;
        hasSignal = false;
    }
    
    public Block() 
    {
        id = count;
        count++;
        prevBlock = -1;
        blockType = 1;
        hasSignal = false;
    }
    
    public void setPrevBlock(int prevBlock)
    {
        this.prevBlock = prevBlock;
    }

    public void setBlockPlus(int blockPlus) 
    {
        this.blockPlus = blockPlus;
    }

    public void setBlockNeg(int blockNeg) 
    {
        this.blockNeg = blockNeg;
    }

    public void setBlockType(int blockType) 
    {
        this.blockType = blockType;
    }

    public int getBlockType() 
    {
        return blockType;
        //1 section
        //2 positive - contracting
        //3 minus - expanding
    }
    
    public int getPrevBlock() 
    {
        return prevBlock;
    }
    
    public int getBlockNeg() 
    {
        return blockNeg;
    }
    
    public int getId() 
    {
        return id;
    }
    
    public int getBlockPlus() 
    {
        return blockPlus;
    }
    
    public boolean hasSignal() 
    {
        if (signals.size() > 0)  
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    
    public String toString()
    {
    	return "ID: " + Integer.toString(this.getId()) + " Type: "  + blockType + " BlockPlus: " + blockPlus + "BlockNeg: " + blockNeg
    			+ " Previous Block: " + prevBlock;
    }
}

