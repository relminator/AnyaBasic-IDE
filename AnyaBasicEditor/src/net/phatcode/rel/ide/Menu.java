/********************************************************************
 *  Menu.java
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

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import javax.swing.tree.DefaultMutableTreeNode;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class Menu extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	private JMenu file = new JMenu("File");
	private JMenu settings = new JMenu("Settings");
	private JMenu help = new JMenu("Help");
	private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    
	private Container parent;
	private JTextPanePlus textPane;
	private IDEmain ideMain;
	private SourceProcessor sourceProcessor;;
    private JTree sourceTree;
    private JTree workspaceTree;
    
    private List<String> types;
    private List<String> variables;
    private List<String> functions;
    
    private DefaultMutableTreeNode nodeRoot;
    private DefaultMutableTreeNode nodeTypes;
    private DefaultMutableTreeNode nodeVars;
    private DefaultMutableTreeNode nodeFuncs;
    private DefaultMutableTreeNode wpNodeRoot;
    
    private Font editorFont;
    private float fontSize;
    
	public Menu()
	{
		
		parent = this.getParent();
	
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/cancel.png");
		ImageIcon exit = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/new_file.png");
        ImageIcon newFile = new ImageIcon(imageUrl);
        
        imageUrl = this.getClass().getClassLoader().getResource("gfx/open.png");
		ImageIcon open = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/info.png");
		ImageIcon save = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/info.png");
		ImageIcon reset = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/ab.png");
        ImageIcon abIcon = new ImageIcon(imageUrl);
        
        JLabel iconLabel = new JLabel(abIcon);
        
        // --------file----------------
		file.setMnemonic(KeyEvent.VK_F);

	        JMenuItem newMenuItem = new JMenuItem("New...", newFile);
	        newMenuItem.setMnemonic(KeyEvent.VK_N);
	        newMenuItem.setToolTipText("Start a program from scratch.");
	        newMenuItem.addActionListener(new ActionListener() 
	        {
	            @Override
	            public void actionPerformed(ActionEvent event) 
	            {
	                  ideMain.reset();              
	            }
	            
	        });
	  

		
		JMenuItem openMenuItem = new JMenuItem("Load...", open);
		openMenuItem.setMnemonic(KeyEvent.VK_L);
		openMenuItem.setToolTipText("Load #AnyaBasic Program ");
		openMenuItem.addActionListener(new ActionListener() 
		{
			@Override
	         public void actionPerformed(ActionEvent event) 
            {
                
                JFileChooser fileChooser = new JFileChooser();
                
                String currentFileName = ideMain.getCurrentFileName();
                String currentFileNameFolder = ideMain.getCurrentFileNameFolder();
                
                fileChooser.setCurrentDirectory(
                        new File(this.getClass().getClassLoader().getResource(".").getPath()
                        + "/" + currentFileNameFolder));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                
                FileFilter filter = new FileNameExtensionFilter( "#AnyaBasic sourcefiles", 
                                                                 "abs");
                fileChooser.setFileFilter(filter);
                fileChooser.setMultiSelectionEnabled(false);
                int returnValue = fileChooser.showOpenDialog(parent);
                
                if (returnValue == JFileChooser.APPROVE_OPTION) 
                {                   
                    File selectedFile = fileChooser.getSelectedFile();
                    
                    currentFileName = selectedFile.getName();
                    
                    ideMain.setCurrentFileName(currentFileName);
                    
                    File rel = new File(selectedFile.getAbsolutePath());
                    rel = rel.getParentFile();
                    currentFileNameFolder = rel.getName();
                    ideMain.setCurrentFileNameFolder(currentFileNameFolder);
                    
                    System.out.println("filename: " + currentFileName);
                    System.out.println("folder: " + currentFileNameFolder);
                    
                    ideMain.sourceTreeReprocess(currentFileName, currentFileNameFolder);
                                                
                }
                
            }

		});
  
	
	
		saveMenuItem = new JMenuItem("Save", save);
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.setEnabled(false);
		saveMenuItem.setToolTipText("Save Program");
		saveMenuItem.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent event) 
            {
        
                int n = JOptionPane.showConfirmDialog( parent, 
                           "Would you like to save the code?",
                           "Save code",
                           JOptionPane.YES_NO_OPTION );
                if (n == JOptionPane.YES_OPTION) 
                {
                    JOptionPane.showMessageDialog( parent, 
                       "Code Saved",
                       "",
                       JOptionPane.INFORMATION_MESSAGE );
                    String currentFileName = ideMain.getCurrentFileName();
                    String currentFileNameFolder = ideMain.getCurrentFileNameFolder();
                    Utils.saveProgram(textPane, currentFileName, currentFileNameFolder);
                            
                }

            }

			
		});
  
	
		saveAsMenuItem = new JMenuItem("Save as...", save);
		saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
		saveAsMenuItem.setToolTipText("Save Atlas in a different folder");
		saveAsMenuItem.setEnabled(false);
		saveAsMenuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
            {
                JFileChooser fileChooser = new JFileChooser();
                
                String currentFileName = ideMain.getCurrentFileName();
                String currentFileNameFolder = ideMain.getCurrentFileNameFolder();
                
                fileChooser.setCurrentDirectory(new File(
                        this.getClass().getClassLoader().getResource(".").getPath()
                        + "/" + currentFileNameFolder));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileFilter filter = new FileNameExtensionFilter( "#AnyaBasic sourcefiles", 
                        "abs");
                fileChooser.setFileFilter(filter);
                fileChooser.setMultiSelectionEnabled(false);
                int returnValue = fileChooser.showSaveDialog(parent);
                if (returnValue == JFileChooser.APPROVE_OPTION) 
                {
                    
                    File file = fileChooser.getSelectedFile();
                    //System.out.println("selected file: " + file.getName());
                    //File f = new File(file.getName());
                    currentFileName  = file.getName();
                    ideMain.setCurrentFileName(currentFileName);
                    
                    File f = new File(
                            this.getClass().getClassLoader().getResource(".").getPath()
                            + "/" + currentFileNameFolder
                            + "/" + currentFileName);
                    if(f.exists() && f.isFile()) 
                    { 
                        int res = JOptionPane.showConfirmDialog(parent,
                                currentFileName 
                                + " exists!!! \n"+
                                "\n" +
                                " "+
                                "Overwrite file?\n",
                                "Warning!",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                        System.out.println( currentFileName + " exists ");
                        if( res ==  JOptionPane.YES_OPTION )
                        {
                            currentFileName = ideMain.getCurrentFileName();
                            currentFileNameFolder = ideMain.getCurrentFileNameFolder();
                            Utils.saveProgram(textPane, currentFileName, currentFileNameFolder);
                            currentFileNameFolder = "myprograms";
                            ideMain.treeWorkspaceProcessDirectories();
                            currentFileNameFolder = "samples";
                            ideMain.treeWorkspaceProcessDirectories();
                            
                        }
                    }
                    else
                    {
                        currentFileName = ideMain.getCurrentFileName();
                        currentFileNameFolder = ideMain.getCurrentFileNameFolder();
                        Utils.saveProgram(textPane, currentFileName, currentFileNameFolder);
                        currentFileNameFolder = "myprograms";
                        ideMain.treeWorkspaceProcessDirectories();
                        currentFileNameFolder = "samples";
                        ideMain.treeWorkspaceProcessDirectories();
                        
                    }
                        
                }
            }
		});
  
	
		JMenuItem exitMenuItem = new JMenuItem("Exit", exit);
		exitMenuItem.setMnemonic(KeyEvent.VK_X);
		exitMenuItem.setToolTipText("Exit application");
		exitMenuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				int n = JOptionPane.showConfirmDialog( parent, 
													   "Would you like to exit the application?",
													   "Exit",
													   JOptionPane.YES_NO_OPTION );
				if (n == JOptionPane.YES_OPTION) System.exit(0);
			}
		});
  
	
		file.add(newMenuItem);
        file.add(openMenuItem);
		file.addSeparator();
		file.add(saveMenuItem);
		file.add(saveAsMenuItem);
		file.addSeparator();
		file.add(exitMenuItem);
		
				// --------Settings----------------
		
		settings.setMnemonic(KeyEvent.VK_S);
		  
		imageUrl = this.getClass().getClassLoader().getResource("gfx/laf.png");
		ImageIcon laf = new ImageIcon(imageUrl);
		
		JMenu lafMenuItem = new JMenu("Change Theme");
		
		lafMenuItem.setMnemonic(KeyEvent.VK_C);
		lafMenuItem.setToolTipText("Changle the overall color scheme of the editor");
		
		String[] cbmiItems = { "Dark", "IntelliJ", "Darkula", "Mac Dark", "Mac Light" };
		JMenuItem[] subItems = new JMenuItem[cbmiItems.length];
		
		for( int i = 0; i < cbmiItems.length; i++ )
		{
			subItems[i] = new JMenuItem(cbmiItems[i], laf);
			lafMenuItem.add(subItems[i]);
		}
		
		subItems[0].addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    try 
		        {
		            
		            UIManager.setLookAndFeel( new FlatDarkLaf() );
		            SwingUtilities.updateComponentTreeUI(ideMain);
                    ideMain.pack();
                    ideMain.sourceTreeReset();
                    ideMain.workSpaceTreeReset();
                } 
		        catch( Exception ex ) 
		        {
		            System.err.println( "Failed to initialize theme. Using fallback." );
		        }
		        
			}
		});
		
		subItems[1].addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    try 
                {
                    
                    UIManager.setLookAndFeel( new FlatIntelliJLaf() );        
                    SwingUtilities.updateComponentTreeUI(ideMain);
                    ideMain.pack();
                    ideMain.sourceTreeReset();
                    ideMain.workSpaceTreeReset();
                } 
                catch( Exception ex ) 
                {
                    System.err.println( "Failed to initialize theme. Using fallback." );
                }
            	
			}
		});
		
		subItems[2].addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    try 
                {
                    
                    UIManager.setLookAndFeel( new FlatDarculaLaf() );        
                    SwingUtilities.updateComponentTreeUI(ideMain);
                    ideMain.pack();
                    ideMain.sourceTreeReset();
                    ideMain.workSpaceTreeReset();
                } 
                catch( Exception ex ) 
                {
                    System.err.println( "Failed to initialize theme. Using fallback." );
                }
            	
			}
		});
		
		subItems[3].addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    try 
                {
                    
                    UIManager.setLookAndFeel( new FlatMacDarkLaf() );
                    SwingUtilities.updateComponentTreeUI(ideMain);
                    ideMain.pack();        
                    ideMain.sourceTreeReset();
                    ideMain.workSpaceTreeReset();
                } 
                catch( Exception ex ) 
                {
                    System.err.println( "Failed to initialize theme. Using fallback." );
                }
            
			}
		});
		
		subItems[4].addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                try 
                {
                    
                    UIManager.setLookAndFeel( new FlatMacLightLaf() );
                    SwingUtilities.updateComponentTreeUI(ideMain);
                    ideMain.pack();        
                    ideMain.sourceTreeReset();
                    ideMain.workSpaceTreeReset();
                } 
                catch( Exception ex ) 
                {
                    System.err.println( "Failed to initialize theme. Using fallback." );
                }
            
            }
        });
        
		
		settings.add(lafMenuItem);
		
		
		// Increase ddecrease font under settings
		imageUrl = this.getClass().getClassLoader().getResource("gfx/font_increase.png");
        ImageIcon fontPlus = new ImageIcon(imageUrl);
        imageUrl = this.getClass().getClassLoader().getResource("gfx/font_decrease.png");
        ImageIcon fontMinus = new ImageIcon(imageUrl);
        
        JMenu fontMenuItem = new JMenu("Chang Font Size");
        
        fontMenuItem.setMnemonic(KeyEvent.VK_A);
        fontMenuItem.setToolTipText("Changle size of the font.");
        
        String[] fontItemNames = { "Increase font size.", "Decrease font size." };
        JMenuItem[] fontSubItems = new JMenuItem[fontItemNames.length];
        
        fontSubItems[0] = new JMenuItem(fontItemNames[0], fontPlus);
        fontSubItems[0].setMnemonic(KeyEvent.VK_PLUS); 
        fontMenuItem.add(fontSubItems[0]);
        
        fontSubItems[1] = new JMenuItem(fontItemNames[1], fontMinus);
        fontSubItems[0].setMnemonic(KeyEvent.VK_MINUS); 
        fontMenuItem.add(fontSubItems[1]);
    
        fontSubItems[0].addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if( fontSize < 24 ) fontSize += 1.0f;
                if(editorFont == null )
                {   
                    textPane.setFont(new Font("Monospaced", Font.PLAIN, (int)fontSize));        
                }
                else
                {
                    editorFont = editorFont.deriveFont(Font.TRUETYPE_FONT, fontSize);
                    textPane.setFont(editorFont);
                    
                }
                SwingUtilities.updateComponentTreeUI(ideMain);
                ideMain.pack();        
                ideMain.sourceTreeReset();
                ideMain.workSpaceTreeReset();
                //System.out.println("size = " + fontSize);
                //System.out.println("size = " + editorFont.getSize());
                
            }
        });
        
        fontSubItems[1].addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if( fontSize > 8 )fontSize -= 1.0f;
                if(editorFont == null )
                {   
                    textPane.setFont(new Font("Monospaced", Font.PLAIN, (int)fontSize));        
                }
                else
                {
                    editorFont = editorFont.deriveFont(Font.TRUETYPE_FONT, fontSize);
                    textPane.setFont(editorFont);
                }
                SwingUtilities.updateComponentTreeUI(ideMain);
                ideMain.pack();
                ideMain.sourceTreeReset();
                ideMain.workSpaceTreeReset();

            }
        });
        
        settings.add(fontMenuItem);
        
		
		// --------help----------------
		
		help.setMnemonic(KeyEvent.VK_H);
		  
		imageUrl = this.getClass().getClassLoader().getResource("gfx/info.png");
		ImageIcon about = new ImageIcon(imageUrl);
		
		JMenuItem aboutMenuItem = new JMenuItem("About...", about);
		aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		aboutMenuItem.setToolTipText("About");
		aboutMenuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    // Get current row of caret
			    int caretPos = textPane.getCaretPosition();
	            int rowNum = (caretPos == 0) ? 1 : 0;
                for (int offset = caretPos; offset > 0;) 
	            {
	                try
                    {
                        offset = Utilities.getRowStart(textPane, offset) - 1;
                    } 
	                catch (BadLocationException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
	                rowNum++;
	            }
	            System.out.println("Row: " + rowNum);
	            try
                {
	                //pane.setCaretPosition(pane.getDocument().
                    //      getDefaultRootElement().
                    //      getElement(4).getStartOffset());
	                int s = textPane.getDocument().
	                          getDefaultRootElement().
	                          getElement(rowNum-1).getStartOffset();
	                int ed = textPane.getDocument().
                            getDefaultRootElement().
                            getElement(rowNum-1).getEndOffset()-s;
	                String text = textPane.getText(s, ed);
	                System.out.println("Text:  " + text);
//	                ed = 0;
//	                int i = 0;
//	                while( i < text.length()  )
//	                {
//	                    if((text.charAt(i) == '\n') ) break;
//	                    ed++;
//	                    i++;
//	                }
	                    
//                    System.out.println("Text:  " + text);
//                    System.out.println("Caret: " + caretPos);
//                    System.out.println("Start: " + s);
//                    System.out.println("End:   " + ed);
//                    System.out.println("Txted:   " + textPane.getText(s, ed));
                    
                    //Element root =textPane.getDocument().getDefaultRootElement();
                    //Element first = root.getElement(rowNum-1);
                    //String txt = textPane.getDocument().getText(first.getStartOffset(), first.getEndOffset());
                    //System.out.println("Txt:   " + txt);
                    
                } catch (BadLocationException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
	            
				JOptionPane.showMessageDialog(parent,
					    "AnyaBasic IDE \n"+
					    "\n"+
					    "Richard Eric M. Lope\n"+
					    "http://rel.phatcode.net\n"+
					    "Icons by https://icons8.com/",
					    "About",
					    JOptionPane.INFORMATION_MESSAGE);
			}
		
		});
		
		help.add(aboutMenuItem);
		
		
		// --------main----------------
		add(iconLabel);
		add(Box.createHorizontalStrut(30));
        add(file);
		add(Box.createHorizontalStrut(30));
		add(settings);
		add(Box.createHorizontalGlue());
		add(help);
		    	
	}

	public void updateSaveItems()
	{
	    if(textPane.getText().equals(""))
	    {
	        saveMenuItem.setEnabled(false);
	        saveAsMenuItem.setEnabled(false);
	    }
	    else
	    {
	        saveMenuItem.setEnabled(true);
            saveAsMenuItem.setEnabled(true);
	    }
	}
	
	private void createDirectory( String directoryName )
	{
	  File theDir = new File(directoryName);

	  // if the directory does not exist, create it
	  if (!theDir.exists())
	  {
		  
	    System.out.println("creating directory: " + directoryName);
	    theDir.mkdir();
	  }
	}

	


	public void setTextPane(JTextPanePlus textPane)
	{
		this.textPane = textPane;
	}



	public SourceProcessor getSourceProcessor()
	{
		return sourceProcessor;
	}


    public void setEditorFont(Font editorFont)
    {
        this.editorFont = editorFont;
    }


    public void setFontSize(float fontSize)
    {
        this.fontSize = fontSize;
    }


    public void setIdeMain(IDEmain ideMain)
    {
        this.ideMain = ideMain;
    }


    public void setSourceProcessor(SourceProcessor sourceProcessor)
    {
        this.sourceProcessor = sourceProcessor;
    }


    public void setSourceTree(JTree sourceTree)
    {
        this.sourceTree = sourceTree;
    }


    public void setWorkspaceTree(JTree workspaceTree)
    {
        this.workspaceTree = workspaceTree;
    }


    public void setTypes(List<String> types)
    {
        this.types = types;
    }


    public void setVariables(List<String> variables)
    {
        this.variables = variables;
    }


    public void setFunctions(List<String> functions)
    {
        this.functions = functions;
    }


    public void setNodeRoot(DefaultMutableTreeNode nodeRoot)
    {
        this.nodeRoot = nodeRoot;
    }


    public void setNodeTypes(DefaultMutableTreeNode nodeTypes)
    {
        this.nodeTypes = nodeTypes;
    }


    public void setNodeVars(DefaultMutableTreeNode nodeVars)
    {
        this.nodeVars = nodeVars;
    }


    public void setNodeFuncs(DefaultMutableTreeNode nodeFuncs)
    {
        this.nodeFuncs = nodeFuncs;
    }


    public void setWpNodeRoot(DefaultMutableTreeNode wpNodeRoot)
    {
        this.wpNodeRoot = wpNodeRoot;
    }

    
	
}
