// A nice utility JTextPane class that disables wordwrapping
// by  Rob Camick
// https://tips4java.wordpress.com/2009/01/25/no-wrap-text-pane/

package net.phatcode.rel.ide;

import java.awt.Dimension;

import javax.swing.JTextPane;

public class JTextPanePlus extends JTextPane
{
    /**
     * 
     */
    private static final long serialVersionUID = -6663148837793010349L;

    @Override
    public boolean getScrollableTracksViewportWidth() {
        // Only track viewport width when the viewport is wider than the preferred width
        return getUI().getPreferredSize(this).width 
            <= getParent().getSize().width;
    };

    @Override
    public Dimension getPreferredSize() {
        // Avoid substituting the minimum width for the preferred width when the viewport is too narrow
        return getUI().getPreferredSize(this);
    };
}
