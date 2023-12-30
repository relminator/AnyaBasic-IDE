/********************************************************************
 *  IDEMain.java
 *  Entry point/main class
 * 
 *  Richard Eric Lope BSN RN
 *  http://rel.phatcode.net
 *  
 * License MIT: 
 * Copyright (c) 2023 Richard Eric Lope 

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. (As clarification, there is no
 * requirement that the copyright notice and permission be included in binary
 * distributions of the Software.)

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 *******************************************************************/

package net.phatcode.rel.ide;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class IDEmain extends JFrame
{
    private static final long serialVersionUID = 1L;
    private ToolBar toolBar;
    private JTextPanePlus textPane;
    private JTextArea textOutput;
    private PrintStream printStream;
    private LineNumbersView lineNumbers;
    private Menu menu;
    private UndoRedo undoRedo;
    private JTree sourceTree;
    private JTree workspaceTree;
    private List<Token> tokens;
    private Lexer lexer = new Lexer();

    private List<String> types;
    private List<String> variables;
    private List<String> functions;

    private DefaultMutableTreeNode nodeRoot;
    private DefaultMutableTreeNode nodeTypes;
    private DefaultMutableTreeNode nodeVars;
    private DefaultMutableTreeNode nodeFuncs;
    private DefaultMutableTreeNode wpNodeRoot;

    private SourceProcessor sourceProcessor = new SourceProcessor();

    private String currentFileName = "temp001.abs";
    private String currentFileNameFolder = "myprograms";
    
    private Font editorFont;
    private float fontSize = 14f;

    public IDEmain()
    {

        try
        {

            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            SwingUtilities.updateComponentTreeUI(this);

        } catch (Exception ex)
        {
            System.err.println("Failed to initialize theme. Using fallback.");
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        System.setProperty("flatlaf.useWindowDecorations", "true");
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        setTitle("AnyaBasic IDE");

        // load font
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("gfx/consola.ttf");

        try
        {
            Font firaFont = Font.createFont(Font.TRUETYPE_FONT, is);
            editorFont = firaFont.deriveFont(Font.TRUETYPE_FONT, fontSize);

        } catch (FontFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        toolBar = new ToolBar();
        textPane = new JTextPanePlus();
        lineNumbers = new LineNumbersView(textPane);
        menu = new Menu();

        undoRedo = new UndoRedo(textPane, toolBar.getUndoButton(), toolBar.getRedoButton());

        setPreferredSize(new Dimension(1337, 700));
        setJMenuBar(menu);

        textOutput = new JTextArea(5, 10);
        textOutput.setEditable(false);
        if (editorFont == null)
        {
            textOutput.setFont(new Font("Monospaced", Font.PLAIN, (int) fontSize - 1));
        } else
        {
            Font sFont = editorFont.deriveFont(Font.TRUETYPE_FONT, fontSize - 1);
            textOutput.setFont(sFont);
        }
        printStream = new PrintStream(new CustomOutputStream(textOutput));
        System.setOut(printStream);
        System.setErr(printStream);

        JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JScrollPane scrollPaneBottom = new JScrollPane(textOutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setLineNumberDefaults();

        setTextPaneDefaults();
        setTreeDefaults();
        setTreeWorkspaceDefaults();

        JScrollPane treeScroll = new JScrollPane(sourceTree);
        JScrollPane treeWorkspaceScroll = new JScrollPane(workspaceTree);
        add(scrollPane, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        add(treeScroll, BorderLayout.WEST);
        add(treeWorkspaceScroll, BorderLayout.EAST);
        // add(bottomPanel, BorderLayout.SOUTH);
        add(scrollPaneBottom, BorderLayout.SOUTH);

        scrollPane.setRowHeaderView(lineNumbers);

        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scrollPaneBottom.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        treeScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        treeWorkspaceScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        menu.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        toolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        checkOS();

        initEverything();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        // setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

    }

    private void initEverything()
    {

        menu.setTextPane(textPane);
        menu.setIdeMain(this);
        menu.setSourceProcessor(sourceProcessor);
        menu.setSourceTree(sourceTree);
        menu.setWorkspaceTree(workspaceTree);
        menu.setNodeRoot(nodeRoot);
        menu.setNodeTypes(nodeTypes);
        menu.setNodeFuncs(nodeFuncs);
        menu.setNodeVars(nodeVars);
        menu.setWpNodeRoot(wpNodeRoot);
        menu.setEditorFont(editorFont);
        menu.setFontSize(fontSize);

        toolBar.setIdeMain(this);
        toolBar.setLexer(lexer);
        toolBar.setTextPane(textPane);
        toolBar.setTextOutput(textOutput);
        toolBar.setTree(sourceTree);
        toolBar.setSourceProcessor(sourceProcessor);
        toolBar.setSourceTree(sourceTree);
        toolBar.setWorkspaceTree(workspaceTree);
        toolBar.setNodeRoot(nodeRoot);
        toolBar.setNodeTypes(nodeTypes);
        toolBar.setNodeFuncs(nodeFuncs);
        toolBar.setNodeVars(nodeVars);
        toolBar.setWpNodeRoot(wpNodeRoot);

        toolBar.updateSaveRunButtons();
        menu.updateSaveItems();
        
        textPane.addMouseListener(new ContextMenuMouseListener());
    }

    private void setTextPaneDefaults()
    {

        if (editorFont == null)
        {
            textPane.setFont(new Font("Monospaced", Font.PLAIN, (int) fontSize));
        } else
        {
            textPane.setFont(editorFont);
        }


        textPane.getDocument().addDocumentListener(new DocumentListener()
        {

            
            @Override
            public void changedUpdate(DocumentEvent arg0)
            {
            }

            @Override
            public void insertUpdate(DocumentEvent arg0)
            {

                toolBar.updateSaveRunButtons();
                menu.updateSaveItems();
            }

            @Override
            public void removeUpdate(DocumentEvent arg0)
            {

                toolBar.updateSaveRunButtons();
                menu.updateSaveItems();
            }


        });
    }

    private void setLineNumberDefaults()
    {
        // lineNumbers.setBackground(Color.DARK_GRAY);
        lineNumbers.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        // lineNumbers.setForeground(Color.LIGHT_GRAY);

    }

    private void setTreeDefaults()
    {
        nodeRoot = new DefaultMutableTreeNode("SOURCE                  ");

        sourceTree = new JTree(nodeRoot);

        if (editorFont == null)
        {
            sourceTree.setFont(new Font("Monospaced", Font.PLAIN, (int) fontSize - 2));
        } else
        {
            Font sFont = editorFont.deriveFont(Font.TRUETYPE_FONT, fontSize - 2);
            sourceTree.setFont(sFont);
        }

        sourceTree.setShowsRootHandles(true);

        nodeTypes = new DefaultMutableTreeNode("TYPES");
        nodeVars = new DefaultMutableTreeNode("VARIABLES");
        nodeFuncs = new DefaultMutableTreeNode("FUNCTIONS");

        nodeRoot.add(nodeTypes);
        nodeRoot.add(nodeFuncs);
        nodeRoot.add(nodeVars);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) sourceTree.getCellRenderer();

        ImageIcon leafIcon = new ImageIcon(this.getClass().getClassLoader().getResource("gfx/srctree_button.png"));
        ImageIcon openIcon = new ImageIcon(this.getClass().getClassLoader().getResource("gfx/srctree_button_open.png"));
        ImageIcon closeIcon = new ImageIcon(
                this.getClass().getClassLoader().getResource("gfx/srctree_button_close.png"));

        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(closeIcon);

        sourceTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) sourceTree.getLastSelectedPathComponent();

                if (node == null)
                {
                    return;
                }

                String nodeName = node.getUserObject().toString();
                // System.out.println(nodeName);

                Map<String, Token> symbols = sourceProcessor.getSymbols();

                if (symbols.containsKey(nodeName))
                {
                    try
                    {
                        Document doc = textPane.getDocument();
                        String symbolText = symbols.get(nodeName).getText();
                        // set caret position
                        int pos = textPane.getDocument().getDefaultRootElement()
                                .getElement(symbols.get(nodeName).getRow() - 1).getStartOffset();
                        textPane.setCaretPosition(pos);
                        boolean found = false;
                        int findLength = symbols.get(nodeName).getText().length();
                        // Rest the search position if we're at the end of the document
                        if (pos + findLength > doc.getLength())
                        {
                            pos = 0;
                        }
                        while (pos + findLength <= doc.getLength())
                        {
                            // Extract the text from doc
                            String match = doc.getText(pos, findLength).toLowerCase();
                            // Check to see if it matches or request
                            if (match.equals(symbolText))
                            {
                                found = true;
                                break;
                            }
                            pos++;
                        }

                        if (found)
                        {
                            textPane.setSelectionStart(pos);
                            textPane.setSelectionEnd(pos + symbolText.length());
                            textPane.getCaret().setSelectionVisible(true);

                        }

                    } 
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }

                }
            }

        });

    }

    public void sourceTreeReset()
    {

        Font sFont = editorFont.deriveFont(Font.TRUETYPE_FONT, textPane.getFont().getSize() - 2);
        sourceTree.setFont(sFont);

        sourceTree.setShowsRootHandles(true);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) sourceTree.getCellRenderer();

        ImageIcon leafIcon = new ImageIcon(this.getClass().getClassLoader().getResource("gfx/srctree_button.png"));
        ImageIcon openIcon = new ImageIcon(this.getClass().getClassLoader().getResource("gfx/srctree_button_open.png"));
        ImageIcon closeIcon = new ImageIcon(
                this.getClass().getClassLoader().getResource("gfx/srctree_button_close.png"));

        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(closeIcon);

        DefaultTreeModel model = (DefaultTreeModel) sourceTree.getModel();
        model.reload(nodeRoot);
     
        // expand tree
        JTree tree = sourceTree;
        int rows = tree.getRowCount();
        for (int r = rows - 1; r > -1; r--)
        {
            tree.expandRow(r);
        }

    }

    public void sourceTreeReprocess( String currentFileName, 
                                     String currentFileNameFolder )
    {
        
        boolean success = false;
        BufferedReader input;
        try
        {
            input = new BufferedReader(new InputStreamReader(
                    new FileInputStream(currentFileNameFolder + "/" + currentFileName)));
            textPane.read(input, "READING FILE");
            success = true;
        } catch (FileNotFoundException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (success)
        {

            nodeTypes.removeAllChildren();
            nodeVars.removeAllChildren();
            nodeFuncs.removeAllChildren();

            sourceProcessor.tokenize(textPane.getText());
            sourceProcessor.processTokens();
            types = sourceProcessor.getTypes();
            variables = sourceProcessor.getVariables();
            functions = sourceProcessor.getFunctions();

            for (String s : types)
            {
                DefaultMutableTreeNode types = new DefaultMutableTreeNode(s);
                nodeTypes.add(types);

            }

            for (String s : variables)
            {
                DefaultMutableTreeNode vars = new DefaultMutableTreeNode(s);
                nodeVars.add(vars);

            }

            for (String s : functions)
            {
                DefaultMutableTreeNode funcs = new DefaultMutableTreeNode(s);
                nodeFuncs.add(funcs);

            }

            // reset tree
            DefaultTreeModel model = (DefaultTreeModel) sourceTree.getModel();
            model.reload(nodeRoot);
            // expand tree
            JTree tree = sourceTree;
            int rows = tree.getRowCount();
            for (int r = rows - 1; r > -1; r--)
            {
                tree.expandRow(r);
            }
            // set cursor to 5th row
            //JTextPane pane = textPane;
            //pane.setCaretPosition(
            //        pane.getDocument().getDefaultRootElement().getElement(4).getStartOffset());
            
            // get number of lines
            Element root = textPane.getDocument().getDefaultRootElement();
            System.out.println( "Elements: " + root.getElementCount());
            // get 
            
            toolBar.updateSaveRunButtons();
            menu.updateSaveItems();
            
        }
    
    }
   
    private void setTreeWorkspaceDefaults()
    {
        wpNodeRoot = new DefaultMutableTreeNode("WORKSPACE EXPLORER        ");

        workspaceTree = new JTree(wpNodeRoot);

        if (editorFont == null)
        {
            workspaceTree.setFont(new Font("Monospaced", Font.PLAIN, (int) fontSize - 2));
        } else
        {
            Font sFont = editorFont.deriveFont(Font.TRUETYPE_FONT, fontSize - 2);
            workspaceTree.setFont(sFont);
        }

        workspaceTree.setShowsRootHandles(true);
        
        currentFileNameFolder = "myprograms";
        treeWorkspaceProcessDirectories();
        currentFileNameFolder = "samples";
        treeWorkspaceProcessDirectories();
        
        workspaceTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) workspaceTree.getLastSelectedPathComponent();

                if (node == null)
                {
                    return;
                }

                if (node.isLeaf())
                {
                    String nodeName = node.getUserObject().toString();
                    currentFileName = nodeName;
                    currentFileNameFolder = node.getParent().toString();
                   
                    sourceTreeReprocess(currentFileName, currentFileNameFolder);
                    
                    System.out.println(nodeName);
                    System.out.println("relativepath = " + currentFileNameFolder);
                    System.out.println("IsLeaf = " + node.isLeaf());

                } // isLeaf
            }

        });

    }
    
    public void workSpaceTreeReset()
    {
        Font sFont = editorFont.deriveFont(Font.TRUETYPE_FONT, textPane.getFont().getSize() - 2);
        workspaceTree.setFont(sFont);

        workspaceTree.setShowsRootHandles(true);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) workspaceTree.getCellRenderer();

        ImageIcon leafIcon = new ImageIcon(this.getClass().getClassLoader().getResource("gfx/wptree_button.png"));
        ImageIcon openIcon = new ImageIcon(
                this.getClass().getClassLoader().getResource("gfx/wptree_button_parent_open.png"));
        ImageIcon closeIcon = new ImageIcon(
                this.getClass().getClassLoader().getResource("gfx/wptree_button_parent_close.png"));

        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(closeIcon);

        DefaultTreeModel model = (DefaultTreeModel) workspaceTree.getModel();
        model.reload(wpNodeRoot);
        // expand tree
        JTree tree = workspaceTree;
        int rows = tree.getRowCount();
        for (int r = rows - 1; r > -1; r--)
        {
            tree.expandRow(r);
        }

    }

    public void treeWorkspaceProcessDirectories()
    {
        File fileRoot = new File(currentFileNameFolder + "/");
        // System.out.println("folder-root = " + fileRoot.getName());

        DefaultMutableTreeNode nodeFolder = new DefaultMutableTreeNode(fileRoot.getName());
        wpNodeRoot.add(nodeFolder);

        FilenameFilter absFileFilter = new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                if (name.endsWith(".abs"))
                {
                    return true;
                } else
                {
                    return false;
                }
            }
        };

        File[] files = fileRoot.listFiles(absFileFilter);

        for (File file : files)
        {
            DefaultMutableTreeNode nodeFile = new DefaultMutableTreeNode(file.getName());
            nodeFolder.add(nodeFile);
            // System.out.println(file.getName());
        }

        ImageIcon leafIcon = new ImageIcon(this.getClass().getClassLoader().getResource("gfx/wptree_button.png"));
        ImageIcon openIcon = new ImageIcon(
                this.getClass().getClassLoader().getResource("gfx/wptree_button_parent_open.png"));
        ImageIcon closeIcon = new ImageIcon(
                this.getClass().getClassLoader().getResource("gfx/wptree_button_parent_close.png"));

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) workspaceTree.getCellRenderer();

        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(closeIcon);

        // reset tree
        DefaultTreeModel model = (DefaultTreeModel) workspaceTree.getModel();
        model.reload(wpNodeRoot);

        // expand tree
        JTree tree = workspaceTree;
        int rows = tree.getRowCount();
        for (int r = rows - 1; r > -1; r--)
        {
            tree.expandRow(r);
        }

    }

    public void reset()
    {
        textPane.setText("");
        sourceProcessor.reset();
        nodeTypes.removeAllChildren();
        nodeFuncs.removeAllChildren();
        nodeVars.removeAllChildren();

        SwingUtilities.updateComponentTreeUI(this);
        pack();
        sourceTreeReset();
        workSpaceTreeReset();
        
        toolBar.updateSaveRunButtons();
        menu.updateSaveItems();
        
        currentFileName = "";
        currentFileNameFolder = "";
    }

    public JTextPanePlus getTextPane()
    {
        return textPane;
    }

    public JTextArea getTextOutput()
    {
        return textOutput;
    }

    public JTree getTree()
    {
        return sourceTree;
    }

    public DefaultMutableTreeNode getRootNode()
    {
        return nodeRoot;
    }

    public DefaultMutableTreeNode getTypeNode()
    {
        return nodeTypes;
    }

    public DefaultMutableTreeNode getVarNode()
    {
        return nodeVars;
    }

    public DefaultMutableTreeNode getFuncNode()
    {
        return nodeFuncs;
    }

    public static void main(String[] args)
    {
        new IDEmain();

    }

    public JTree getSourceTree()
    {
        return sourceTree;
    }

    public JTree getWorkspaceTree()
    {
        return workspaceTree;
    }

    public List<Token> getTokens()
    {
        return tokens;
    }

    public Lexer getLexer()
    {
        return lexer;
    }

    public DefaultMutableTreeNode getNodeRoot()
    {
        return nodeRoot;
    }

    public DefaultMutableTreeNode getNodeTypes()
    {
        return nodeTypes;
    }

    public DefaultMutableTreeNode getNodeVars()
    {
        return nodeVars;
    }

    public DefaultMutableTreeNode getNodeFuncs()
    {
        return nodeFuncs;
    }

    public DefaultMutableTreeNode getWpNodeRoot()
    {
        return wpNodeRoot;
    }

    public SourceProcessor getSourceProcessor()
    {
        return sourceProcessor;
    }

    public String getCurrentFileName()
    {
        return currentFileName;
    }

    public String getCurrentFileNameFolder()
    {
        return currentFileNameFolder;
    }

    
    public void setCurrentFileName(String currentFileName)
    {
        this.currentFileName = currentFileName;
    }

    public void setCurrentFileNameFolder(String currentFileNameFolder)
    {
        this.currentFileNameFolder = currentFileNameFolder;
    }

    private void checkOS()
    {
        String nameOS = "os.name";
        String versionOS = "os.version";
        String architectureOS = "os.arch";
        System.out.println("<OS Info>");
        System.out.println("Name: " + System.getProperty(nameOS));
        System.out.println("Version: " + System.getProperty(versionOS));
        System.out.println("Architecture: " + System.getProperty(architectureOS));
        
        BufferedReader lineRead = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try
        {
            input = lineRead.readLine();
        } catch (IOException e)
        {
            System.out.println("Error in Input method" + e);
        }

    }

}
