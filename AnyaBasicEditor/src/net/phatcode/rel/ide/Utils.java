/********************************************************************
 *  Utils.java
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

public class Utils
{

    public static void saveProgram( JTextPanePlus textPane,
            String currentFileName,
            String currentFileNameFolder )
    {
        
           if( currentFileName.equals("") || currentFileNameFolder.equals(""))
            {
                System.out.println("Error saving!");
                System.out.println("FileName: " + currentFileName);
                System.out.println("Folder: " + currentFileNameFolder);
                
                return;
            }
            try 
            {
                String text = textPane.getText();
                BufferedWriter out;
                out = new BufferedWriter(new FileWriter(currentFileNameFolder 
                        + "/" + currentFileName));
                out.write(text);
                out.close();
            } 
            catch (IOException e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

                        
        
    }

    public static String getCurrentLineText( JTextPane textPane  )
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
            return text;
//          ed = 0;
//          int i = 0;
//          while( i < text.length()  )
//          {
//              if((text.charAt(i) == '\n') ) break;
//              ed++;
//              i++;
//          }
                
//            System.out.println("Text:  " + text);
//            System.out.println("Caret: " + caretPos);
//            System.out.println("Start: " + s);
//            System.out.println("End:   " + ed);
//            System.out.println("Txted:   " + textPane.getText(s, ed));
            
            //Element root =textPane.getDocument().getDefaultRootElement();
            //Element first = root.getElement(rowNum-1);
            //String txt = textPane.getDocument().getText(first.getStartOffset(), first.getEndOffset());
            //System.out.println("Txt:   " + txt);
            
        } catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "";
        
    }
}
