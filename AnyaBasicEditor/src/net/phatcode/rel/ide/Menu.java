package net.phatcode.rel.ide;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

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
	private Container parent;
	private JTextPanePlus textPane;
	private IDEmain ideMain;
	private SourceProcessor sourceProcessor = new SourceProcessor();
	
	private Font editorFont;
    private float fontSize;
    
	public Menu()
	{
		
		parent = this.getParent();
	
		URL imageUrl = this.getClass().getClassLoader().getResource("gfx/cancel.png");
		ImageIcon exit = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/info.png");
		ImageIcon open = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/info.png");
		ImageIcon folder = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/info.png");
		ImageIcon save = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/info.png");
		ImageIcon reset = new ImageIcon(imageUrl);
		
		imageUrl = this.getClass().getClassLoader().getResource("gfx/ab.png");
        ImageIcon abIcon = new ImageIcon(imageUrl);
        
        JLabel iconLabel = new JLabel(abIcon);
        
        // --------file----------------
		file.setMnemonic(KeyEvent.VK_F);
  
		JMenuItem openMenuItem = new JMenuItem("Load Images", open);
		openMenuItem.setMnemonic(KeyEvent.VK_L);
		openMenuItem.setToolTipText("Load mutiple images");
		openMenuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				JFileChooser fileChooser = new JFileChooser();
				
				FileFilter filter = new FileNameExtensionFilter( "Image files(png, bmp, jpg, gif)", 
																 "png",
																 "bmp",
																 "gif",
																 "jpg", 
																 "jpeg");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setCurrentDirectory(new File(this.getClass().getClassLoader().getResource("").getPath()));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setMultiSelectionEnabled(true);
				int returnValue = fileChooser.showOpenDialog(parent);
            	if (returnValue == JFileChooser.APPROVE_OPTION) 
            	{
    				
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                }
            	else
            	{
            		
            	}
			}
		});
  
	
		JMenuItem folderMenuItem = new JMenuItem("Load Folder", folder);
		folderMenuItem.setMnemonic(KeyEvent.VK_L);
		folderMenuItem.setToolTipText("Load all images inside a folder");
		folderMenuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				JFileChooser fileChooser = new JFileChooser();
				
				fileChooser.setCurrentDirectory(new File(this.getClass().getClassLoader().getResource(".").getPath()));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(parent);
            	if (returnValue == JFileChooser.APPROVE_OPTION) 
            	{
    				
            		FilenameFilter filter = new FilenameFilter() 
            		{
            		    public boolean accept(File dir, String name) 
            		    {
            		        return ( name.endsWith(".png") || 
            		        		 name.endsWith(".bmp") ||
            		        		 name.endsWith(".gif") ||
            		        		 name.endsWith(".jpg") ||
            		        		 name.endsWith(".jpeg") );
            		    }
            		};
            		
                    File folder = fileChooser.getSelectedFile();
                    File[] selectedFiles = folder.listFiles(filter);
                }
			}
			
		});
  
	
		JMenuItem resetMenuItem = new JMenuItem("Reset", reset);
		resetMenuItem.setMnemonic(KeyEvent.VK_R);
		resetMenuItem.setToolTipText("Reset settings to default");
		resetMenuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			}
		});
  
		
		JMenuItem saveMenuItem = new JMenuItem("Save", save);
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.setEnabled(false);
		saveMenuItem.setToolTipText("Save Atlas");
		saveMenuItem.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				
				int n = JOptionPane.showConfirmDialog( parent, 
						   "Would you like to save the spritesheet?",
						   "Save Atlas",
						   JOptionPane.YES_NO_OPTION );
					if (n == JOptionPane.YES_OPTION) 
					{
						//PackerValues.getInstance().setFileName(
						//		PackerValues.getInstance().getTxFileName().getText() );
						//File folder = new File( "output/" + PackerValues.getInstance().getFileName() +".png" );
						//try
						//{
						//	ImageIO.write(PackerValues.getInstance().getPackerTexture(), "png", folder);
						//} catch (IOException e)
						//{
							// TODO Auto-generated catch block
						//	e.printStackTrace();
						//}
						
						//File folderIdx = new File( "output/" + PackerValues.getInstance().getFileName() +"Index.png" );
						//try
						//{
						//	PackerValues.getInstance().getTexturePacker().createTextureIndex();
						//	ImageIO.write(PackerValues.getInstance().getTexturePacker().getTextureIndex(), "png", folderIdx);
						//} catch (IOException e)
						//{
							// TODO Auto-generated catch block
						//	e.printStackTrace();
						//}
						
						//int width = PackerValues.getInstance().getAtlasWidth();
						//int height = PackerValues.getInstance().getAtlasHeight();
						//TexturePacker texturePacker = PackerValues.getInstance().getTexturePacker();
						
						//SaveC saveC = new SaveC( width,height,texturePacker.getCoords() );
						//saveC.saveToFileCoords( "output/" + PackerValues.getInstance().getFileName() );
						
						//PackerValues.getInstance().getAnimatedLabel().setMyText("Atlas saved. Ready for another set of images...");
						
						JOptionPane.showMessageDialog( parent, 
								   "SpriteSheet Saved",
								   "Please load another set of images.",
								   JOptionPane.INFORMATION_MESSAGE );
					
					}
					
				
				
				/*
				JFileChooser fileChooser = new JFileChooser();
				
				FileFilter filter = new FileNameExtensionFilter( "Image files(png, bmp, jpg, tga, gif)", 
																 "png",
																 "bmp",
																 "gif",
																 "jpg", 
																 "jpeg");

        		fileChooser.setFileFilter(filter);
        		fileChooser.setCurrentDirectory(new File(this.getClass().getClassLoader().getResource("").getPath()));
				int returnValue = fileChooser.showSaveDialog(parent);
            	if (returnValue == JFileChooser.APPROVE_OPTION) 
            	{
            		
            	    File folder = fileChooser.getSelectedFile();
            	    
            	    String output = PackerValues.getInstance().getFileName()+".png";
            	    File outputfile = new File(folder.getAbsolutePath() + output);
            	    System.out.println(outputfile);
            	    try
					{
						ImageIO.write(PackerValues.getInstance().getPackerTexture(), "png", folder);
						PackerValues.getInstance().getAnimatedLabel().setMyText("Atlas saved. Ready for another set of images...");
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            	else
            	{
            		
            	}
            	*/
			}
			
			
			
		});
  
	
		JMenuItem saveAsMenuItem = new JMenuItem("Save as...", save);
		saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
		saveAsMenuItem.setToolTipText("Save Atlas in a different folder");
		saveAsMenuItem.setEnabled(false);
		saveAsMenuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				
				JFileChooser fileChooser = new JFileChooser();
				
				FileFilter f = new FileNameExtensionFilter( "JPEG", 
					    									"jpeg" );
				fileChooser.addChoosableFileFilter(f);
				
				f = new FileNameExtensionFilter( "JPG", 
												  "jpg" );
		
				f = new FileNameExtensionFilter( "PNG", 
					    						 "png" );
				fileChooser.addChoosableFileFilter(f);
			
				fileChooser.setCurrentDirectory(new File(this.getClass().getClassLoader().getResource("").getPath()));
				fileChooser.setSelectedFile(new File("texturepack.png"));
				int returnValue = fileChooser.showSaveDialog(parent);
            	if (returnValue == JFileChooser.APPROVE_OPTION) 
            	{
            		
            		FileFilter fileFilter = fileChooser.getFileFilter(); 
            	    File filename = fileChooser.getSelectedFile();
            	    //if (filename.getName().toLowerCase().endsWith(fileFilter.getDescription().toLowerCase()) )
            	    //{
            	    	//File filenameToSave = fileChooser.getSelectedFile().
            	    //}
            	    //else
            	    //{
            	    	
            	    //}
            	    //try
					//{
					//} 
            	    //catch (IOException e)
					//{
						// TODO Auto-generated catch block
					//	e.printStackTrace();
					//}
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
  
	
		file.add(openMenuItem);
		file.add(folderMenuItem);
		file.addSeparator();
		file.add(saveMenuItem);
		file.add(saveAsMenuItem);
		file.addSeparator();
		file.add(resetMenuItem);
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
					    "http://rel.phatcode.net",
					    "About",
					    JOptionPane.PLAIN_MESSAGE);
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


	public void setFrame(IDEmain ideMain)
	{
		this.ideMain = ideMain;
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
	
	
	//public void setTypeNode(DefaultMutableTreeNode)
	
}
