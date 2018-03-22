package RailGraphActual;
     
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;
    
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
     
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
     
/**
 * The main class for the project. Initializes the graph and user interface, runs the algorithms on the graph, and fill the table values. Graph, Edge, SpriteManager, Sprite, Dijkstra and Node objects are all from the GraphStream library.
 * @author Team 5
 *
 */
public class UIv3 
{
    static List<String> conflict = new ArrayList<String>();
    private JFrame frame = null;
     
    private static JTable table = null;
     
    private TableModel model = null;
     
    private JScrollPane s_pan = null;
     
    private JButton button_1 = null, button_2 = null;
     
    private JPanel pane = null;
     
    static Graph graph = new SingleGraph("graph");
     
    static SpriteManager sman = new SpriteManager(graph);
     
    static Network network = new Network(graph, sman);
    static int idcounter ;
    static List<String> path = new ArrayList<String>();
     static int counterui;
   
    String id;
    String points;
    String signals;
    String pathh;
    static String conflicts;
    boolean wait;
    String source;
    String dest;
     
    static int nodeId = 0;
     
    /**
     * Checks file string input for validation of network. Umar.
     * @param s
     * @return
     */
    public static boolean checkInput(String s) {
        Scanner s1 = new Scanner(s);
        String c = s1.next();
        String[] j = c.split("[np;]+");
        if (c.charAt(0) != 's') {return false;}
        for (int i=0;i<j.length;i++)
        {
            if (j[i].matches(".*[s]+.*"))
            {
     
            }
            else
            {
                return false;
            }
        }
        int numP=0;
        int numB=0;
        int numN=0;
        for (int i=0;i<c.length();i++)
        {
            if (c.charAt(i) != 'n' && c.charAt(i) != 'p' && c.charAt(i) != ';'
                    && c.charAt(i) != 's' && c.charAt(i) != 'u' && c.charAt(i) != 'd') {
                return false;
            }
            if (c.charAt(i) == 'n') {numN++;}
            else if (c.charAt(i) == 'p') {numP++;}
            else if (c.charAt(i) == ';') {numB++;}
        }
        if (numN < numP || numN< numB || numN < numB+numP) {return false;}
        return true;
    }
     
    /**
     * Initialize the UI. Jack.
     */
    public UIv3() {
        frame = new JFrame("JTableTest");
        pane = new JPanel();
        JTextField tf1 = new JTextField(10);
        JTextField tf2 = new JTextField(10);
     
     
        JLabel jl1 = new JLabel("Please Enter the Source:");
        JLabel jl2 = new JLabel("Please Enter the Destination:");
     
        button_1 = new JButton("End journey");
        button_2 = new JButton("Add route");
     
        pane.add(jl1);
        pane.add(tf1);
        pane.add(jl2);
        pane.add(tf2);
     
        /*
         * pane.add(tf3); pane.add(tf4); pane.add(tf5); pane.add(tf6);
         */
        pane.add(button_2);
        pane.add(button_1);
     
/**
 * A action when click button_1. It will use removeData() method to clear the table. Jack.
 */
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeData();
                network.resetVisuals(graph, sman);
                idcounter =0;
                path.clear();
                counterui = 0;
            }
        });
/**
 * A action when click button_2. Get the source and destination from textfield. Jack.
 */
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) throws NumberFormatException, IndexOutOfBoundsException 
            {
    
                    source = tf1.getText();
    
                    dest = tf2.getText();
                    
                wait = true;
     
            }
        });
        model = new TableModel(20);
        table = new JTable(model);
        table.setBackground(Color.white);
        String[] age = { "16", "17", "18", "19", "20", "21", "22" };
        JComboBox com = new JComboBox(age);
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(3).setCellEditor(new DefaultCellEditor(com));
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setPreferredWidth(100);
     
        s_pan = new JScrollPane(table);
     
        frame.getContentPane().add(s_pan, BorderLayout.CENTER);
        frame.getContentPane().add(pane, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 200);
        frame.setVisible(true);
     
    }
     
    public boolean clearfunction() {
        return true;
    }
     
    public void setPoints(String newPoints) {
        this.points = newPoints;
    }
     
    public void setSignals(String newSignals) {
        this.signals = newSignals;
    }
     
    public void setId(String newId) {
        this.id = newId;
    }
     
    public void setPath(String newPath) {
        this.pathh = newPath;
    }
     
    public static void setConflicts(String newConflicts) {
        conflicts = newConflicts;
    }
     
    public String getSource() {
        return source;
    }
     
    public String getDest() {
        return dest;
    }
     
    public void setSource(String newSource) {
        source = newSource;
    }
     
    public void setDest(String newDest) {
        dest = newDest;
    }
     
    public boolean getwait() {
        return wait;
    }
     
    public void resetckeck() {
        wait = false;
    }
/**
 * A method to add data in table. Jack.
 * @param id
 * @param source
 * @param dest
 * @param points
 * @param signals
 * @param path
 * @param conflicts
 */
    void addData(String id, String source, String dest, String points, String signals, String path, String conflicts, List<String> con) {
        model.addRow(id, source, dest, points, signals, path, conflicts, con);
        table.updateUI();
    }
/**
 * A method to remove data from table. Jack.
 */
    private void removeData() {
        model.removeRows(0, model.getRowCount());
        table.updateUI();
    }
    /**
     * This method will calculate conflicts in the table. It takes in the list of paths and for each path calculates the conflict. Umar.
     * 
     * @param path
     * @return
     */
    private static List<String> calculateConflict(List<String> path) {
        List<String>conflict = new ArrayList<String>();
        for (int i = 0; i < path.size(); i++) {
            conflict.add("");
        }
        for (int i = 0; i < path.size(); i++) {
            for (int j = 0; j < path.size(); j++) {
                if (i != j) {
                    String a = path.get(j);
                    String b = path.get(i);
                    String[] a1 = a.split(" ");
                    String[] b1 = b.split(" ");
                    ArrayList<String> a2 = new ArrayList<String>();
                    ArrayList<String> b2 = new ArrayList<String>();
                    for (int k = 1; k < a1.length; k++) {
                        a2.add(a1[k]);
                    }
                    for (int k = 1; k < b1.length; k++) {
                        b2.add(b1[k]);
                    }
                    for (int l=0;l<a2.size();l++) {
                        for (int m=0;m<b2.size();m++) {
                            if (a2.get(l).equals(b2.get(m))) {
                                conflict.set(j, conflict.get(j) + "r" + i + " ");
                                     
                            }
                        }
                    }
                }
            }
        }
        for (int n=0;n<conflict.size();n++) {
            String con1 = conflict.get(n);
            String[] con2 = con1.split(" ");
            Set<String> set1 = new HashSet<String>();
            for (int k = 0; k < con2.length; k++) {
                set1.add(con2[k]);
            }
            String s = new String("");
            ArrayList<String> temp = new ArrayList<String>();
            temp.addAll(set1);
            for (int x = 0; x < temp.size(); x++) {
                s = s + temp.get(x) + " ";
            }
            conflict.set(n,s);
        }
        System.out.println(conflict);
        return conflict;
        //table.updateUI();
    }
     
    public JFrame getFrame() {
        return frame;
    }
         
    /**
     * 
     * @param args
     * 
     *
     */
    public static void main(String args[])  {
          // File reading 
          // Harris
        try {
        Scanner scan = new Scanner(new File("C:\\Users\\xaris\\eclipse-workspace\\Test2\\RailGraphPkg\\network1.txt"));
        String s = scan.next();
            try {
                if (!checkInput(s)) {
                    throw new InvalidParameterException();
                }
        List<String> id = new ArrayList<String>();
        List<String> source = new ArrayList<String>();
        List<String> dest = new ArrayList<String>();
        List<String> points = new ArrayList<String>();
        List<String> signals = new ArrayList<String>();
        List<String> conflict = new ArrayList<String>();
     
        int direction = 0;
        String temppoints = "", tempsignals = "", temppath = "", conflicts = "", tempid = "";
     
        int trackCount = 0;
         
     
        LinkedList<Block> blocks = new LinkedList<Block>();
     
        blocks.add(new Block());
             
        createBlocksFromFile(blocks, trackCount, s);
     
        network.createNetwork(blocks, graph, sman);
     
        network.createVisualStyling(graph);
     
        UIv3 UI = new UIv3();
     
        //Settings for merging the graph and UI in one window. Evan.
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel view = viewer.addDefaultView(false);
        UI.getFrame().add(view);
     
        //Forces the UI to wait for an input value by using Thread class  Harris.
        for (int o = 0; o < 100; o++) 
        {
            boolean checkk = false;
            while (UI.getwait() == checkk) 
            {
                try
                {
                    Thread.sleep(200);
                } 
                catch (InterruptedException e) 
                {
                        
                }
            }
            UI.resetckeck();
     
            //The user inputs (start and end destinations) from UI Harris.
            String input1 = UI.getSource();
            String input2 = UI.getDest();
    
             
            //When a user adds a route, the route ID is obtained and incremented. r + Route ID is used in Route field. Harris
            String routeid = "r" + idcounter;
            id.add(routeid);
            idcounter++;
     
            //S is added to source and destination signals to be placed in Signals field Harris
            source.add("s" + input1);
            dest.add("s" + input2);
     
            //Check which signal is larger to indicate which direction the path is following. 0 is left to right (Up), 1 is right to left (Down). Harris
            try
            {
            if (Integer.parseInt(input1) > Integer.parseInt(input2)) {
                direction = 0;
            } else {
                direction = 1;
            }
            }
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null,"Inputs Incorrect - Must Be Number");
            }
     
            //Gets the correct nodes for using Dijkstra from signals input the user types. Harris
            try
            {
            String[] attributes = GetAttributes(input1, input2, graph, direction, sman);
               
     
            //List used to store the path a user takes and organizes it.  Harris
            List<String> pathray = new ArrayList<String>();
            pathray = Dijkstra(graph, attributes);
            Collections.reverse(pathray);
     
            //Find every point in the path generated by Dijkstra. Adds a P to each to be used in Points field Harris
            for (Edge edge : graph.getEachEdge()) {
     
                for (int i = 0; i < pathray.size(); i++) {
                    if (edge.getId().equals(pathray.get(i))) {
                        if (edge.getId().contains("+") || edge.getId().contains("-")) {
                            temppoints = temppoints + " " + "p" + edge.getId();
     
                        }
                    }
                }
            }
                
     
            points.add(temppoints);
            UI.setPoints(points.get(counterui));
                 
            //Get the path and blocks to be used in Path field Harris
            for (Edge edge : graph.getEachEdge()) {
     
                for (int i = 0; i < pathray.size(); i++) {
                    if (edge.getId().equals(pathray.get(i))) {
                        if (edge.getId().contains("+") || edge.getId().contains("-")) {
                            temppath = temppath + " " + "p" + edge.getId();
     
                        } else {
                            temppath = temppath + " " + "b" + edge.getId();
     
                        }
                    }
                }
            }
                
     
            path.add(temppath);
            UI.setPath(path.get(counterui));
     
            //Find the signals in the graph to be used in the Signals field and Conflicts Harris, Umar
            tempsignals = DijkstraSignals(graph, attributes, sman, direction);
            signals.add(tempsignals);
            List<String> c = getInterlocking(graph, pathray, blocks, direction);
            if (c.size() > 0) {
                for (int i = 0; i < c.size(); i++) {
                    // System.out.println(c.get(i));
                }
     
                List<String> d = getInterlockSigs(c, direction, blocks);
     
                if (d.size() > 0) {
                    for (int i = 0; i < d.size(); i++) {
                        List<Signal> temp = blocks.get(Integer.parseInt(d.get(i))).getSignals();
                        for (int j = 0; j < temp.size(); j++) {
                            // signals.add(Integer.toString(temp.get(j).getId()));
                            tempsignals = tempsignals + " s" + (Integer.toString(temp.get(j).getId()));
                        }
                    }
                    for (int i = 1; i < c.size(); i = i + 2) {
                        temppoints = temppoints + " p" + c.get(i);
                    }
                }
            }
     
            UI.setSignals(signals.get(counterui));
            UI.setId(tempid = id.get(counterui));
            //UI.setConflicts(conflict.get(counterui));
            counterui++;
            //table.column
            List<String> con =  calculateConflict(path);
     
            UI.addData(tempid, input1, input2, temppoints, tempsignals, temppath, conflicts,con);
     
            temppath = "";
            tempsignals = "";
            temppoints = "";
     
            for (int i = 0; i < path.size(); i++) {
                conflict.add(" ");
            }
        }
            
        catch (NullPointerException e)
        {
            JOptionPane.showMessageDialog(null,"Input Incorrect = Must Select An Existing Node In Graph");
        }}}
        catch (InvalidParameterException e) {
            JOptionPane.showMessageDialog(null,"Invalid File format!");
        }}
   catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(null,"File Not Found!");
    }
      
          
    }
     
    /**
     * Creates block and signal objects from the text file and places them in a LinkedList for use in creating network. Umar
     * @param blocks
     * @param trackCount
     * @param s
     */
    private static void createBlocksFromFile(LinkedList<Block> blocks, int trackCount, String s) 
    {
        int prevBlock = -1;
        boolean broken = false;
        boolean negative = false;
        boolean plus = false;
        int trackIndex = 0;
        ArrayList<Integer> counts = new ArrayList<Integer>();
        ArrayList<Integer> lastID = new ArrayList<Integer>();
        boolean startCount = false;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            prevBlock++;
            // counter should not start until the first negative
            if (startCount) {trackCount++;}
            //character us a section
            if (c == 's') {
                //normal case
                if (!broken && !negative && !plus) {
                    blocks.add(new Block(prevBlock, 1));
                    prevBlock = blocks.get(blocks.size() - 1).getId() - 1;
                    blocks.get(prevBlock).setBlockPlus(prevBlock + 1);
                    // after a negative block has been placed
                } else if (negative && !broken && !plus) {
                    negative = false;
                    blocks.add(new Block(prevBlock, 1));
                    prevBlock = blocks.get(blocks.size() - 1).getId() - 1;
                    blocks.get(prevBlock).setBlockNeg(prevBlock + 1);
                    //After a broken block has been placed
                } else if (broken && !negative && !plus) {
                    broken = false;
                    blocks.add(new Block(prevBlock, 1));
                    prevBlock = blocks.get(blocks.size() - 1).getId() - 1;
                    // after plus point has been placed
                } else if (plus && !negative && !broken) {
                    plus = false;
                    blocks.add(new Block(prevBlock, 1));
                    blocks.get(prevBlock).setBlockPlus(blocks.get(blocks.size() - 1).getId());
                    blocks.get(prevBlock).setBlockNeg(blocks.get(blocks.size() - 1).getId() - 1);
                    prevBlock = blocks.get(blocks.size() - 1).getId() - 1;
                }
            } else if (c == 'n') {
                // add negative point
                blocks.add(new Block(prevBlock, 3));
                lastID.add(new Integer(Integer.toString(prevBlock + 1)));
                trackIndex++;
                trackCount = 0;
                startCount = true;
                negative = true;
            } else if (c == 'p') {
                // add positive point
                trackIndex = trackIndex - 1;
                blocks.add(new Block(prevBlock, 2));
                int currID = blocks.get(blocks.size() - 1).getId();
                int prevID = lastID.get(trackIndex).intValue();
                prevBlock = prevID;
                blocks.get(prevID).setBlockPlus(currID + 1);
                blocks.add(new Block(prevID, 1));
                prevID++;
                for (int j = 1; j < trackCount - 1; j++) {
                    // create positive rail automatically for the user
                    prevBlock = blocks.get(blocks.size() - 1).getId();
                    prevID++;
                    if (blocks.get(prevID).hasSignal()) {
                        blocks.add(new Block(prevBlock, 1));
                        ArrayList<Signal> sig = blocks.get(prevID).getSignals();
                        ArrayList<Signal> siglist = new ArrayList<Signal>();
                        for (int k = 0; k < sig.size(); k++) {
                            Signal tempsig = new Signal(sig.get(k).isDownDirection(), prevID);
                            siglist.add(tempsig);
                        }
                        blocks.get(prevBlock + 1).setSignals(siglist);
                    } else {
                        blocks.add(new Block(prevBlock, 1));
                    }
                }
                if (lastID.size() > 0) {
                    lastID.remove(trackIndex);
                }
                prevBlock = blocks.get(blocks.size() - 1).getId();
                blocks.get(prevBlock).setBlockNeg(currID);
                trackCount = 0;
                blocks.get(currID).setBlockNeg(blocks.get(blocks.size() - 1).getId() - 1);
                prevBlock = currID - 1;
                plus = true;
                // add trackCount amount of blocks to close up!
            } else if (c == ';') {
                //place a break
                trackIndex = trackIndex - 1;
                prevBlock = lastID.get(trackIndex).intValue();
                blocks.get(prevBlock).setBlockNeg(prevBlock + 1);
                blocks.get(prevBlock).setBlockPlus(prevBlock + trackCount);
                if (lastID.size() > 0) {
                    lastID.remove(trackIndex);
                }
     
                prevBlock = blocks.get(blocks.size() - 1).getId() - trackCount;
                trackCount = 0;
                broken = true;
            } else if (c == 'u') {
                // place an UP signal on the previous section
                Signal sig = new Signal(false, prevBlock);
                blocks.get(prevBlock).getSignals().add(sig);
                prevBlock--;
                trackCount--;
            } else {
                // place an DOWN signal on the previous section
                Signal sig = new Signal(true, prevBlock);
                blocks.get(prevBlock).getSignals().add(sig);
                prevBlock--;
                trackCount--;
            }
        }
    }
     
    /**
     * This will return the id's of blocks that conatain signals along the path which is to be interlocked Umar
     * @param sigs
     * @param direction
     * @param blocks
     * @return
     */
    private static List<String> getInterlockSigs(List<String> sigs, int direction, LinkedList<Block> blocks) {
        List<String> foundSigs = new ArrayList<String>();
        if (direction == 1) {
            // UP direction
            if (sigs.get(0).charAt(sigs.get(0).length()-1) == '-') {
                // negative point
                for (int i = 0; i < sigs.size(); i = i + 2) {
                    // only interested in the opening point
                    int prevblock = blocks.get(Integer.parseInt(sigs.get(i + 1).substring(0, 1))).getBlockNeg();
                    int stopper = blocks.get(Integer.parseInt(sigs.get(i).substring(0, 1))).getId();
                    while (prevblock != stopper) {
                        // add block ids
                        foundSigs.add(Integer.toString(prevblock));
                        prevblock = blocks.get(prevblock).getPrevBlock();
                    }
                }
            } else {
                for (int i = 0; i < sigs.size(); i = i + 2) {
                    // only interested in the opening point
                    int prevblock = blocks.get(Integer.parseInt(sigs.get(i + 1).substring(0, 1))).getPrevBlock();
                    int stopper = blocks.get(Integer.parseInt(sigs.get(i).substring(0, 1))).getId();
                    while (prevblock != stopper) {
                        // add block ids
                        foundSigs.add(Integer.toString(prevblock));
                        prevblock = blocks.get(prevblock).getPrevBlock();
                    }
                }
            }
        } else {
            //Down direction
            if (sigs.get(0).charAt(1) == '-') {
                //negative point
                for (int i = 0; i < sigs.size(); i = i + 2) {
                    int prevblock = blocks.get(Integer.parseInt(sigs.get(i).substring(0, 1))).getBlockNeg();
                    int stopper = blocks.get(Integer.parseInt(sigs.get(i + 1).substring(0, 1))).getId();
                    while (prevblock != stopper) {
                        // add block ids
                        foundSigs.add(Integer.toString(prevblock));
                        prevblock = blocks.get(prevblock).getPrevBlock();
                    }
                }
            } else {
                for (int i = 0; i < sigs.size(); i = i + 2) {
                    int prevblock = blocks.get(Integer.parseInt(sigs.get(i).substring(0, 1))).getPrevBlock();
                    int stopper = blocks.get(Integer.parseInt(sigs.get(i + 1).substring(0, 1))).getId();
                    while (prevblock != stopper) {
                        // add block ids
                        foundSigs.add(Integer.toString(prevblock));
                        prevblock = blocks.get(prevblock).getPrevBlock();
                    }
                }
            }
        }
        return foundSigs;
    }
     
    /**
     * Generates pairs of interlocking points as an arraylist Umar
     * @param graph2
     * @param pathray
     * @param blocks
     * @param direction
     * @return
     */
    private static List<String> getInterlocking(Graph graph2, List<String> pathray, LinkedList<Block> blocks,
            int direction) {
        int size = pathray.size();
        List<String> pairs = new ArrayList<String>();
        if (direction == 1) {
            //UP Driection
            for (int i = 0; i < size; i++) {
                if (pathray.get(i).length() > 1
                        && blocks.get(Integer.parseInt(pathray.get(i).substring(0, 1))).getBlockType() == 3) {
                    // if negative point
                    int id = (Integer.parseInt(pathray.get(i).substring(0, 1)));
                    char dir = pathray.get(i).charAt(1);
                    int level = 0;
                    for (int j = id + 1; j < blocks.size(); j++) {
                        if (blocks.get(j).getBlockType() == 2 && level == 0) {
                            // found corresponding point
                            if (dir == '+') {
                                if (pathray.contains(pathray.contains(Integer.toString(j) + dir))) {
     
                                } else {
                                    // found (negative)
                                    pairs.add(Integer.toString(id) + dir);
                                    pairs.add((j + "-"));
                                }
                            } else {
                                if (pathray.contains(pathray.contains(Integer.toString(j) + dir))) {
     
                                } else {
                                    // found (positive)
                                    pairs.add(Integer.toString(id) + dir);
                                    pairs.add((j + "+"));
                                }
                            }
                        }
                        if (blocks.get(j).getBlockType() == 3) {
                            level++;
                            // up a level
                        }
                        if (blocks.get(j).getBlockType() == 2 && level != 0) {
                            level--;
                            // down a level
                        }
                    }
                }
     
            }
        } else {
            // Down Direction
            for (int x = 0; x < size; x++) {
                if (pathray.get(x).length() > 1
                        && blocks.get(Integer.parseInt(pathray.get(x).substring(0, 1))).getBlockType() == 2) {
                    // going backwards so look for positive points
                    int id = (Integer.parseInt(pathray.get(x).substring(0, 1)));
                    char dir = pathray.get(x).charAt(1);
                    int level = 0;
                    for (int j = id - 1; j > 0; j--) {
                        //go backwards through block array
                        if (blocks.get(j).getBlockType() == 3 && level == 0) {
                            if (dir == '+') {
                                //found
                                    pairs.add(Integer.toString(id) + dir);
                                    pairs.add((j + "-"));
                            } else {
                                 pairs.add(Integer.toString(id) + dir);
                                 pairs.add((j + "+"));
                            }
                        }
                        if (blocks.get(j).getBlockType() == 2) {
                            level++;
                            //go up a level
                        }
                        if (blocks.get(j).getBlockType() == 3 && level != 0) {
                            level--;
                            //go down a level
                        }
                    }
                }
            }
        }
        return pairs;
    }
     
    /**
     * A method for finding all the signals and edges a path has and change their color. Harris, Umar, Evan
     * @param graph
     * @param attributes
     * @param sman
     * @param direction
     * @return
     */
    private static String DijkstraSignals(Graph graph, String[] attributes,
            SpriteManager sman, int direction) {
        String tempsignals = "";
        List<String> pathray = new ArrayList<String>();
     
        /**
         * Initialize the algorithm and set starting node.
         */
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.NODE, null, null);
        dijkstra.init(graph);
        dijkstra.setSource(graph.getNode(attributes[0]));
        dijkstra.compute();
     
        for (Edge edge : dijkstra.getPathEdges(graph.getNode(attributes[1]))) {
            pathray.add(edge.getId());
        }
        Collections.reverse(pathray);
     
        for (Edge edge : dijkstra.getPathEdges(graph.getNode(attributes[1]))) {
            edge.setAttribute("ui.style", "fill-color: blue; ");
            for (Sprite sprite : sman.sprites()) {
     
                if (sprite.getLabel("edgeId").equals(edge.getId())) {
                    tempsignals = tempsignals + " " + "s" + sprite.getId();
                    if (sprite.getLabel("direction").equals(Integer.toString(direction))) {
                        // Add to string of sprites to return to UI
                        sprite.setAttribute("ui.style", "fill-color: green;");
                        sprite.setAttribute("isOn", "Yes");
                    }
                }
            }
     
        }
     
        dijkstra.clear();
        return tempsignals;
    }
     /**
      * Using Dijkstra a list of strings containing thw correct path is created. Harris
      * @param graph
      * @param attributes
      * @return
      */
    private static List<String> Dijkstra( Graph graph, String[] attributes) {
     
        List<String> pathray = new ArrayList<String>();
     
        /**
         * Initialize the algorithm and set starting node.
         */
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.NODE, null, null);
        dijkstra.init(graph);
        dijkstra.setSource(graph.getNode(attributes[0]));
        dijkstra.compute();
     
        for (Edge edge : dijkstra.getPathEdges(graph.getNode(attributes[1]))) {
            pathray.add(edge.getId());
     
        }
     
        dijkstra.clear();
        return pathray;
    }
    /**
     * A method for finding the starting and ending node using the users input. It converts the signals to nodes. Harris, Umar, Evan
     * @param input1
     * @param input2
     * @param graph
     * @param direction
     * @param sman
     * @return
     */
    private static String[] GetAttributes(String input1, String input2, Graph graph, int direction,
            SpriteManager sman) {
     
        String start = input1;
        String end = input2;
        String startid = sman.getSprite(start).getAttribute("edgeId");
        String endid = sman.getSprite(end).getAttribute("edgeId");
     
        String[] attributes = new String[2];
        if (direction == 0) {
            attributes[0] = graph.getEdge(startid).getNode1().getId();
            attributes[1] = graph.getEdge(endid).getNode0().getId();
        } else {
            attributes[0] = graph.getEdge(startid).getNode0().getId();
            attributes[1] = graph.getEdge(endid).getNode1().getId();
        }
        return attributes;
     
    }
     
}
     
/**
 * Create a table model to use as a template for the rows and columns
 * @author Jack - Team 5
 *
 */
class TableModel extends AbstractTableModel {
     
    private static final long serialVersionUID = -7495940408592595397L;
     
    private Vector content = null;
/**
 * Create table according to title name. Here we have seven titles so we will get a seven rows table.
 */
    private String[] title_name = { "ID", "Source", "Dest", "Points", "Signals", "Path", "Conflicts" };
     
    public TableModel() {
        content = new Vector();
    }
     
    public TableModel(int count) {
        content = new Vector(count);
    }
     
/**
 * A method to add params to the table
 * @param id
 * @param source
 * @param dest
 * @param points
 * @param path
 * @param signals
 * @param conflicts
 */
    public void addRow(String id, String source, String dest, String points, String path, String signals,
            String conflicts, List<String> con) {
        Vector v = new Vector(7);
        v.add(0, id);
        v.add(1, source);
        v.add(2, dest);
        v.add(3, points);
        v.add(4, path);
        v.add(5, signals);
        v.add(6, conflicts);
        content.add(v);
           
           
        if(content.size()>1) {
            int index = content.size();
            for (int j=0;j<con.size();j++) {
                Vector v1 =new Vector();
                v1 = (Vector) content.get(j);
                v1.setElementAt(con.get(j),6);
            }
            System.out.println(content.size());
            }
        }
       
/**
 * A method to remove one row
 * @param row
 */
    public void removeRow(int row) {
        content.remove(row);
    }
/**
 * A method to remove all rows in the table by using for loop
 * @param row
 * @param count
 */
    public void removeRows(int row, int count) {
        for (int i = 0; i < count; i++) {
            if (content.size() > row) {
                content.remove(row);
            }
        }
    }
/**
 * A method to check each row and column is editable or not.
 */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        }
        return true;
    }
     
    public void setValueAt(Object value, int row, int col) {
        ((Vector) content.get(row)).remove(col);
        ((Vector) content.get(row)).add(col, value);
        this.fireTableCellUpdated(row, col);
    }
     
    public String getColumnName(int col) {
        return title_name[col];
    }
     
    public int getColumnCount() {
        return title_name.length;
    }
     
    public int getRowCount() {
        return content.size();
    }
     
    public Object getValueAt(int row, int col) {
        return ((Vector) content.get(row)).get(col);
    }
     
}