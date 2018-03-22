package RailGraphActual;
import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

import java.awt.BorderLayout;  
import java.awt.Color;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;  
           
import javax.swing.DefaultCellEditor;  
import javax.swing.JButton;  
import javax.swing.JComboBox;  
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;  
import javax.swing.table.TableColumnModel;
     
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer; 

/**
 * The Network class handles everything related to graph visualisation  except dynamic path representation (highlighting path and switching signals) 
 * which is covered in main.
 * @author Evan
 *
 */
public class Network 
{
    Graph graph = new SingleGraph("graph");
    
    SpriteManager sman = new SpriteManager(graph);
    
    static int nodeId = 0;
    
    public Network(Graph graph, SpriteManager sman)
    {
    	this.graph = graph;
    	this.sman = sman;
    }
    
    /**
     * Create sprites to represent signals on the rail line by attaching to edges if the corresponding block has a signal. Evan.
     * @param block
     * @param newEdge
     * @param sman
     */
    public void createSprites(Block block, Edge newEdge, SpriteManager sman)
    {
        //If the block has a signal, create a sprite by getting the signal object from the block object it is attached to and give it the matching ID of the signal
        if(block.hasSignal())
        {
            String firstSignalId = Integer.toString(block.getSignals().get(0).getId());
                    
            //Add a sprite to the graph, attach it to an edge, label it with an identifying ID, associated edge, and whether or not the signal is "on" (used to visually show green or red and to indicate conflicts)
            Sprite sprite1 = sman.addSprite(firstSignalId);
            sprite1.attachToEdge(Integer.toString(block.getId()));
            sprite1.addAttribute("label", firstSignalId);
            sprite1.addAttribute("edgeId", newEdge.getId());
            sprite1.addAttribute("isOn", "No");
             
            //If the signal is below the rail (is indicated as "down"), place the sprite below the rail, and vice versa
            if (block.getSignals().get(0).isDownDirection())
            {
                sprite1.setPosition(Units.PX, 0.5, -18, 0);
                sprite1.addAttribute("direction", "1");
            }
                    
            else
            {
                sprite1.setPosition(Units.PX, 0.5, 18, 0);
                sprite1.addAttribute("direction", "0");
            }
                    
            //If the block has two signals, repeat for other signal
            if (block.getSignals().size() == 2)
            {
                String secondSignalId = Integer.toString(block.getSignals().get(1).getId());
                        
                Sprite sprite2 = sman.addSprite(secondSignalId);
                sprite2.attachToEdge(Integer.toString(block.getId()));
                        
                sprite2.addAttribute("label", secondSignalId);
                sprite2.addAttribute("edgeId", newEdge.getId());
                sprite2.addAttribute("isOn", "No");
                        
                if (block.getSignals().get(1).isDownDirection())
                {
                    sprite2.setPosition(Units.PX, 0.5, -18, 0);
                    sprite2.addAttribute("direction", "1");
                }
                        
                else
                {
                    sprite2.setPosition(Units.PX, 0.5, 18, 0);
                    sprite2.addAttribute("direction", "0");
                }
                        
            }
        }
    }
        
    /**
     * Creates the network by reading from list of blocks and creating corresponding edges one by one. Evan
     * @param blocks
     * @param graph
     */
    public void createNetwork(LinkedList<Block> blocks, Graph graph, SpriteManager sman2)
    {
        int nodeLabel;
        SpriteManager sman = sman2;
                
        for(Block block : blocks)
        {
                    
            if (block.getId() == 0)
            {
                        
                nodeLabel = nodeId;
                graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", 0, 0);
                nodeId++;
                        
                nodeLabel = nodeId;
                graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", 1, 0);
                nodeId++;
                        
                Edge startEdge = graph.addEdge("0", "0", "1");
                        
                createSprites(block, startEdge, sman);
        
            }
                    
            if(block.getBlockType() == 1)
            {
                for(Edge edge : graph.getEachEdge())
                {
                    if (edge.getId().equals(Integer.toString(block.getPrevBlock())))
                    {
                        createType1(graph, block, edge, blocks, sman);
                    }
                }
            }
                    
            if(block.getBlockType() == 3)
            {
                createType3(graph, block, blocks, sman);
            }
                    
            if(block.getBlockType() == 2)
            {
                createType2(graph, block, blocks, sman);
            }
                    
        }
                
    }
            
    /**
     * Create a block of type 1 (single straight line only). Evan
     * @param graph
     * @param block
     * @param edge
     * @param blocks
     * @param sman
     */
    public void createType1(Graph graph, Block block, Edge edge, LinkedList<Block> blocks, SpriteManager sman)
    {
        int nodeLabel = nodeId;
                
        //Get X and Y coordinates from second node of previous block/edge
        double xyzCoords[] = nodePosition(edge.getNode1());
        double xCoord = xyzCoords[0];
        double yCoord = xyzCoords[1];
                
        if(block.getBlockNeg() != 0)
        {
            Edge nextEdge = graph.getEdge(Integer.toString(block.getBlockNeg()) + "+");
            Edge prevEdge = graph.getEdge(Integer.toString(block.getPrevBlock()));
                    
            Edge newEdge = graph.addEdge(Integer.toString(block.getId()), prevEdge.getNode1().getId(), nextEdge.getNode1().getId());
                    
            createSprites(block, newEdge, sman);
                    
        }
                
        else
        {
            //Create node by giving it a label, incrementing X coordinate +1 and keeping the Y coordinate
            graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", xCoord + 1, yCoord);
            
            //Create the block/edge
            Edge newEdge = graph.addEdge(Integer.toString(block.getId()), Integer.parseInt(edge.getNode1().getId()), nodeLabel);
            nodeId++;
                    
            createSprites(block, newEdge, sman);
        }
        
    }
            
    /**
     * Create blocks of type 2 (two sloping blocks retracting in). Evan
     * @param graph
     * @param block
     * @param blocks
     * @param sman
     */
    public void createType2(Graph graph, Block block, LinkedList<Block> blocks, SpriteManager sman)
    {
        int nodeLabel = nodeId;
        int firstNodeLabel;
                
        for (Edge edge : graph.getEachEdge())
        {
            double xyzCoords[] = nodePosition(edge.getNode1());
            double xCoord = xyzCoords[0];
            double yCoord = xyzCoords[1];
                    
            //Adding plus block first, then negative
            if (edge.getId().equals(Integer.toString(block.getPrevBlock())))
            {
                //For adding the plus block
                graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", xCoord + 1, yCoord - 1);
                Edge newPlusEdge = graph.addEdge(Integer.toString(block.getId()) + "-", Integer.parseInt(edge.getNode1().getId()), nodeLabel);
                firstNodeLabel = nodeId;
                nodeId++;
                        
                createSprites(block, newPlusEdge, sman);
                        
                if (blocks.contains(blocks.get(block.getBlockNeg())))
                {
                    nodeLabel = nodeId;
                    graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", xCoord, yCoord - 1);
                    Edge newMinusEdge = graph.addEdge(Integer.toString(block.getId()) + "+", Integer.toString(firstNodeLabel), Integer.toString(nodeLabel));
                    nodeId++;
                            
                    createSprites(block, newMinusEdge, sman);
                    Block nextPlusBlock = blocks.get(block.getBlockPlus());
                            
                    if(nextPlusBlock.getBlockType() == 1)
                    {
                        createType1(graph, nextPlusBlock, graph.getEdge(Integer.toString(block.getId()) + "-"), blocks, sman);
                    }
                    break;
                }
            }
                    
            //Adding negative block first, then positive
            else if (edge.getId().equals(Integer.toString(block.getBlockNeg())))
            {
                //For adding the Minus block
                nodeLabel = nodeId;
                graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", xCoord + 1, yCoord + 1);
                Edge newMinusEdge = graph.addEdge(Integer.toString(block.getId()) + "+", Integer.parseInt(edge.getNode1().getId()), nodeLabel);
                firstNodeLabel = nodeId;
                nodeId++;
                        
                createSprites(block, newMinusEdge, sman);
                        
                if(blocks.contains(blocks.get(block.getPrevBlock())))
                {
                    nodeLabel = nodeId;
                    graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", xCoord - 1, yCoord + 1);
                    Edge newPlusEdge = graph.addEdge(Integer.toString(block.getId()) + "+", Integer.toString(firstNodeLabel), Integer.toString(nodeLabel));
                    nodeId++;
                            
                    createSprites(block, newPlusEdge, sman);
                            
                    Block nextPlusBlock = blocks.get(block.getBlockPlus());
                            
                    if(nextPlusBlock.getBlockType() == 1)
                    {
                        createType1(graph, nextPlusBlock, graph.getEdge(Integer.toString(block.getId()) + "-"), blocks, sman);
                    }
                    break;
                }
            }
        }
    }
            
    /**
     * Create blocks of type 3 (two sloping blocks extending out and subsequent following blocks). Evan
     * @param graph
     * @param block
     * @param blocks
     * @param sman
     */
    public void createType3(Graph graph, Block block, LinkedList<Block> blocks, SpriteManager sman)
    {
        int nodeLabel = nodeId;
                
        for (Edge edge: graph.getEachEdge())
        {
            if(edge.getId().equals(Integer.toString(block.getPrevBlock())))
            {
                double xyzCoords[] = nodePosition(edge.getNode1());
                double xCoord = xyzCoords[0];
                double yCoord = xyzCoords[1];
                        
                //For adding the Plus block
                graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", xCoord + 1, yCoord);
                Edge newPlusEdge = graph.addEdge(Integer.toString(block.getId()) + "+", Integer.parseInt(edge.getNode1().getId()), nodeLabel);
                nodeId++;
                        
                createSprites(block, newPlusEdge, sman);
                        
                //Create a following block extended from the original one depending on type of block.
                Block nextPlusBlock = blocks.get(block.getBlockPlus());
                Edge edgeFromPlus = graph.getEdge(Integer.toString(block.getId()) + "+");
                        
                if (nextPlusBlock.getBlockType() == 1)
                {
                    createType1(graph, nextPlusBlock, edgeFromPlus, blocks, sman);
                }
                        
                if (nextPlusBlock.getBlockType() == 3)
                {
                    createType3(graph, block, blocks, sman);
                }
                        
                if(nextPlusBlock.getBlockType() == 2)
                {
                            
                }
                        
                //For adding the Minus block
                nodeLabel = nodeId;
                graph.addNode(Integer.toString(nodeLabel)).addAttribute("xy", xCoord + 1, yCoord + 1);
                Edge newMinusEdge = graph.addEdge(Integer.toString(block.getId()) + "-", Integer.parseInt(edge.getNode1().getId()), nodeLabel);
                nodeId++;
                        
                createSprites(block, newMinusEdge, sman);
                        
                Block nextMinusBlock = blocks.get(block.getBlockNeg());
                Edge edgeFromMinus = graph.getEdge(Integer.toString(block.getId()) + "-");
                        
                if (nextMinusBlock.getBlockType() == 1)
                {
                    createType1(graph, nextMinusBlock, edgeFromMinus, blocks, sman);
                }
                        
                if (nextMinusBlock.getBlockType() == 3)
                {
                    createType3(graph, block, blocks, sman);
                }
                        
                if(nextMinusBlock.getBlockType() == 2)
                {
                            
                }
                        
                break;
            }
        }
    }
            
    /**
     * Sets default visual styling (signal/sprite color, text alignment, window rendering, node shape, etc. Evan
     */
    public void createVisualStyling(Graph graph)
    {
        //Create default settings for sprites, nodes. Sprite color Green = CLEAR, Red = STOP.
        graph.addAttribute("ui.stylesheet", "sprite { fill-color: red; text-alignment: at-right; } "
                        + "node { fill-color: black; shape: box; text-alignment: above; }"
                        + "edge {text-alignment: above; size: 5; }");
                
        //Display the ID of each node.
        for (Node node : graph) 
        {
            node.addAttribute("label", node.getId());
        }
                
        //Display ID of each edge.
        for (Edge edge : graph.getEachEdge()) 
        {
            edge.addAttribute("label", edge.getId());
                    
        }
                
                
        //Sets window rendering settings
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    }
            
    /**
     * Reset sprite and edge visuals to default colors. Evan
     * @param graph
     * @param sman
     */
     public void resetVisuals(Graph graph, SpriteManager sman)
     {
         for(Edge edge : graph.getEachEdge())
         {
            edge.setAttribute("ui.style", "fill-color: black;");
         }
                
         for(Sprite sprite : sman)
         {
            sprite.setAttribute("ui.style", "fill-color: red;");
         }
     }
}
