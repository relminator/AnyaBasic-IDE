// A nice little class that redirects console output to a JTextArea
// Written by:  Nam Ha Minh
// https://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea


package net.phatcode.rel.ide;

import java.io.IOException;
import java.io.OutputStream;
 
import javax.swing.JTextArea;
 
public class CustomOutputStream extends OutputStream 
{
    private JTextArea textArea;
    
    public CustomOutputStream(JTextArea textArea) 
    {
        this.textArea = textArea;
    }
    
    @Override
    public void write(int b) throws IOException 
    {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
