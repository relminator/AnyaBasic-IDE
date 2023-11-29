package net.phatcode.rel.ide;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

public class Utils
{

    public String getCurrentLineText( JTextPane textPane  )
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
