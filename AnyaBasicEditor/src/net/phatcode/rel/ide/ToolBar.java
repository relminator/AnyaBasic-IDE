/********************************************************************
 *  ToolBar.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;

public class ToolBar extends JToolBar
{
	private static final long serialVersionUID = 1L;
	private Container parent;
	private JButton newFileButton;
	private JButton openButton;
	private JButton saveButton;
	private JButton saveAsButton;
    private JButton cutButton;
	private JButton copyButton;
	private JButton pasteButton;
	private JButton findButton;
	private JButton undoButton;
	private JButton redoButton;
    private JButton runButton;
	
	private IDEmain ideMain;
    
	private Lexer lexer;
	private JTextPanePlus textPane;
	private JTextArea textOutput;
	//private IDEmain ideMain;
	
	private JTree sourceTree;
    private JTree workspaceTree;
    
	private SourceProcessor sourceProcessor;;
	
	private List<String> types;
	private List<String> variables;
	private List<String> functions;
	
	private DefaultMutableTreeNode nodeRoot;
    private DefaultMutableTreeNode nodeTypes;
    private DefaultMutableTreeNode nodeVars;
    private DefaultMutableTreeNode nodeFuncs;
    private DefaultMutableTreeNode wpNodeRoot;
    
	

	public ToolBar()
	{
		
		parent = this.getParent();
		
		
		setNewFileDefault();
		setOpenDefault();
		setSaveDefault();  
		setSaveAsDefault();
        setCutDefault();
		setCopyDefault();
		setPasteDefault();
		setFindDefault();
		setUndoDefault();
		setRedoDefault();
        setRunDefault();
		
		  
		  
        URL imageUrl = this.getClass().getClassLoader().getResource("gfx/dot.png");
        ImageIcon dotIcon = new ImageIcon(imageUrl);
          
		//setLayout(new FlowLayout(FlowLayout.LEADING));
		addSeparator();
		add(newFileButton);
		add(openButton);
		add(Box.createHorizontalStrut(12));
		add(new JLabel(dotIcon));
		add(Box.createHorizontalStrut(12));
        add(saveButton);
		add(saveAsButton);
		add(Box.createHorizontalStrut(12));
        add(new JLabel(dotIcon));
        add(Box.createHorizontalStrut(12));
        add(cutButton);
		add(copyButton);
		add(pasteButton);
		add(Box.createHorizontalStrut(12));
        add(new JLabel(dotIcon));
        add(Box.createHorizontalStrut(12));
        add(findButton);
        add(Box.createHorizontalStrut(12));
        add(new JLabel(dotIcon));
        add(Box.createHorizontalStrut(12));
        add(undoButton);
        add(redoButton);
        add(Box.createHorizontalStrut(12));
        add(new JLabel(dotIcon));
        add(Box.createHorizontalStrut(12));
        add(runButton);
		
		
	}
	
	private void setNewFileDefault()
	{
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/new_file.png");
		ImageIcon newFile = new ImageIcon(imageUrl);
		newFileButton = new JButton(newFile);
		newFileButton.setToolTipText("New program");
		newFileButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{ 
			    ideMain.reset(); 
			    ideMain.setCurrentFileName("temp001.abs");
			    ideMain.setCurrentFileNameFolder("myprograms");
			}
		});
	}
	
	private void setOpenDefault()
	{
		parent = this.getParent();
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/open.png");
		ImageIcon open = new ImageIcon(imageUrl);

		openButton = new JButton(open);
		openButton.setToolTipText("Load Program");
		openButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				
				//try {
				//	String jarPath = getClass()
				//	        .getProtectionDomain()
				//	        .getCodeSource()
				//	        .getLocation()
				//	        .toURI()
				//	        .getPath();
				//	System.out.println("Path: " + jarPath);
				//	        
				//} catch (URISyntaxException e) {
				//	// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				
				
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
                    
                    System.out.println(currentFileName);
                    System.out.println(currentFileNameFolder);
                    
                    ideMain.sourceTreeReprocess(currentFileName, currentFileNameFolder);
                            	
            	}
			}
		});
		
	}
	
	
	private void setSaveDefault()
	{
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/save.png");
		ImageIcon save = new ImageIcon(imageUrl);
		saveButton = new JButton(save);
		saveButton.setEnabled(true);
		saveButton.setToolTipText("Save Program");
		saveButton.addActionListener(new ActionListener() 
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

	}
	
	private void setSaveAsDefault()
    {
        URL imageUrl = this.getClass().getClassLoader().getResource("gfx/save_as.png");
        ImageIcon folder = new ImageIcon(imageUrl);
        saveAsButton = new JButton(folder);
        saveAsButton.setToolTipText("Save as...");
        saveAsButton.addActionListener(new ActionListener() 
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
    
    }
    
	private void setCutDefault()
	{
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/cut.png");
		ImageIcon cut = new ImageIcon(imageUrl);
		cutButton = new JButton(cut);
		cutButton.setToolTipText("Cut");
		cutButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    textPane.cut();
			}
		});

	}

	private void setCopyDefault()
	{
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/copy.png");
		ImageIcon copy = new ImageIcon(imageUrl);
		copyButton = new JButton(copy);
		copyButton.setToolTipText("Copy");
		copyButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    textPane.copy();
			}
		});

	}

	private void setPasteDefault()
	{
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/paste.png");
		ImageIcon paste = new ImageIcon(imageUrl);
		pasteButton = new JButton(paste);
		pasteButton.setToolTipText("Paste");
		pasteButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    textPane.paste();
			}
		});

	}
	
	private void setFindDefault()
	{
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/search.png");
		ImageIcon find = new ImageIcon(imageUrl);
		findButton = new JButton(find);
		findButton.setToolTipText("Find");
		
		findButton.addActionListener(new ActionListener() 
		{
		   @Override
			public void actionPerformed(ActionEvent event) 
			{
		       String result = (String)JOptionPane.showInputDialog(
                       ideMain,
                       "Enter word to find...",
                       "Find",
                       JOptionPane.PLAIN_MESSAGE,
                       find, null, "" );
		       if( result != null && result.length() > 0 )
		       {
    		       try {
    			        Document doc = textPane.getDocument();
    			        int pos = textPane.getCaretPosition();
    			        boolean found = false;
    			        int findLength = result.length();
    			        // Rest the search position if we're at the end of the document
    			        if (pos + findLength > doc.getLength()) {
    			            pos = 0;
    			        }
    			        while (pos + findLength <= doc.getLength()) {
    			            // Extract the text from doc
    			            String match = doc.getText(pos, findLength).toLowerCase();
    			            // Check to see if it matches or request
    			            if (match.equals(result)) {
    			                found = true;
    			                break;
    			            }
    			            pos++;
    			        }
    
    			        if (found) {
    			            textPane.setSelectionStart(pos);
    			            textPane.setSelectionEnd(pos + result.length());
    			            textPane.getCaret().setSelectionVisible(true);
    			        }
    			    } catch (Exception e) {
    			        e.printStackTrace();
    			    }
		       }
			}
		});

	}
	
	private void setUndoDefault()
	{
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/undo.png");
		ImageIcon settings = new ImageIcon(imageUrl);
		undoButton = new JButton(settings);
		undoButton.setToolTipText("Undo");
		undoButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			}
		});
  
	}

	   private void setRedoDefault()
	    {
	        URL imageUrl = this.getClass().getClassLoader().getResource("gfx/redo.png");
	        ImageIcon settings = new ImageIcon(imageUrl);
	        redoButton = new JButton(settings);
	        redoButton.setToolTipText("Redo");
	        redoButton.addActionListener(new ActionListener() 
	        {
	            @Override
	            public void actionPerformed(ActionEvent event) 
	            {
	            }
	        });
	  
	    }

	private void setRunDefault()
	{
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/run.png");
		ImageIcon go = new ImageIcon(imageUrl);
		runButton = new JButton(go);
		runButton.setEnabled(true);
		runButton.setToolTipText("Run Program");
		runButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			    
			    // Automatically save file
			    String currentFileName = ideMain.getCurrentFileName();
                String currentFileNameFolder = ideMain.getCurrentFileNameFolder();
                if(currentFileName.equals("") || currentFileNameFolder.equals(""))
                {
                    JOptionPane.showMessageDialog(parent,
                            "File not saved! \n"+
                            "\n"+
                            "You need to save the file\n"+
                            "before you can run it.",
                            "",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Utils.saveProgram(textPane, currentFileName, currentFileNameFolder);
               
				//String[] command = {"C:/Dev/BASIC/AnyaBasic/AnyaBasic2022/runhelloworld.bat", ""};
				//ProcessBuilder builder = new ProcessBuilder(command);
				//builder = builder.directory(new File("C:/Dev/BASIC/AnyaBasic/AnyaBasic2022"));
				//try {
				//	Process p = builder.start();
				//} catch (IOException e) {
				//	// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				//Runtime rt = Runtime.getRuntime();
				//try {
				//	rt.exec(new String[]{"runhelloworld.bat",null,"C:/Dev/BASIC/AnyaBasic/AnyaBasic2022/"});
				//} catch (IOException e) {
				//	// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				try {
					//Process process = Runtime.getRuntime().exec("cmd /c start C:/Dev/BASIC/AnyaBasic/AnyaBasic2022/runhelloworld.bat");
					//Process process = Runtime.getRuntime().exec("cmd /c start runhelloworld.bat");
					//Process process = Runtime.getRuntime().exec("cmd /c start samples/runbubbles.bat");
					//Process proc = Runtime.getRuntime().exec("java -jar AnyaBasic2022.jar samples/bubbles.abs");
					//Process proc = Runtime.getRuntime().exec("cmd /c start cmd.exe"); 
					///Process proc = Runtime.getRuntime().exec("java -jar AnyaBasic2022.jar samples/helloworld.abs");
					//System.out.println("creating directory: ");
					//Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar AnyaBasic2022.jar samples/helloworld.abs" + "\""});
					//Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar AnyaBasic.jar samples/bubbles.abs" + "\""});
					//Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar AnyaBasic.jar samples/particles2.abs" + "\""});
					
					
				    currentFileName = ideMain.getCurrentFileName();
		            currentFileNameFolder = ideMain.getCurrentFileNameFolder();
		            
					//String command = "java -jar AnyaBasic.jar ";
		            //String command = "";
		            //command += currentFileNameFolder + "/" + currentFileName;
					//command += " "+ currentFileNameFolder;
					
					//Runtime.getRuntime().exec( command );
                    
					//Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k", command});
					
					//System.out.println( command  );
                    /*
                     * String path = currentFileNameFolder + "/" + currentFileName; path += " "+
                     * currentFileNameFolder;
                     * 
                     * ProcessBuilder pb = new ProcessBuilder("java", "-jar",
                     * "C:/Dev/BASIC/AnyaBasic/AnyaBasic2022/AnyaBasic.jar", path ); Process p =
                     * pb.start(); BufferedReader in = new BufferedReader(new
                     * InputStreamReader(p.getInputStream())); String s = ""; while((s =
                     * in.readLine()) != null){ System.out.println(s); } int status; try { status =
                     * p.waitFor(); System.out.println("Exited with status: " + status); } catch
                     * (InterruptedException e) { // TODO Auto-generated catch block
                     * e.printStackTrace(); }
                     */
					List<String> commands = new ArrayList<String>();
					//commands.add("cmd");
					//commands.add("/c");
					//commands.add("start");
					//commands.add("cmd");
					//commands.add("/k");
                    commands.add("java");
                    commands.add("-Djava.library.path=.");
                    commands.add("-jar");
				    commands.add("AnyaBasic.jar");
				    commands.add(currentFileNameFolder + "/" + currentFileName);
				    commands.add("./"+currentFileNameFolder);
                    ProcessBuilder builder = new ProcessBuilder(commands);		    
				    Process process = builder.start();
				    try
                    {
                        process.waitFor();
                    } catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	
	public void updateSaveRunButtons()
    {
        if(textPane.getText().equals(""))
        {
            saveButton.setEnabled(false);
            saveAsButton.setEnabled(false);
            runButton.setEnabled(false);
        }
        else
        {
            saveButton.setEnabled(true);
            saveAsButton.setEnabled(true);
            runButton.setEnabled(true);
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
	
	public void setLexer( Lexer lexer)
	{
		this.lexer = lexer;
	}
	

	public void setTextPane(JTextPanePlus textPane)
	{
		this.textPane = textPane;
	}

	public void setTextOutput(JTextArea textOutput)
	{
		this.textOutput = textOutput;
	}


	public void setTree(JTree sourceTree)
	{
		this.sourceTree = sourceTree;
	}

    public void setSourceTree(JTree sourceTree)
    {
        this.sourceTree = sourceTree;
    }

    public void setSourceProcessor(SourceProcessor sourceProcessor)
    {
        this.sourceProcessor = sourceProcessor;
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

    public void setWorkspaceTree(JTree workspaceTree)
    {
        this.workspaceTree = workspaceTree;
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

    public void setIdeMain(IDEmain ideMain)
    {
        this.ideMain = ideMain;
    }

    public JButton getUndoButton()
    {
        return undoButton;
    }

    public JButton getRedoButton()
    {
        return redoButton;
    }

    
	
}
