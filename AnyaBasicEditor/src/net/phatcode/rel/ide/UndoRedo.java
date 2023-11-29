package net.phatcode.rel.ide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class UndoRedo
{
    private static final String REDO_KEY = "redo";
    private static final String UNDO_KEY = "undo";

    private KeyStroke undo = KeyStroke.getKeyStroke("control Z");
    private KeyStroke redo = KeyStroke.getKeyStroke("control Y");
   
    private UndoManager undoManager;
    private JTextPane component;
   
   
    private JButton undoButton;
    private JButton redoButton;

    public UndoRedo( JTextPane component, 
                     JButton undoButton,
                     JButton redoButton )
    {
        this.component = component;
        this.undoButton = undoButton;
        this.redoButton = redoButton;
        addUndoFunctionality(component);
        
    }

    public void setUndo(KeyStroke undo)
    {
        this.undo = undo;
    }

    public void setRedo(KeyStroke redo)
    {
        this.redo = redo;
    }

    public void setUndoButton(JButton undoButton)
    {
        this.undoButton = undoButton;
    }

    public void setRedoButton(JButton redoButton)
    {
        this.redoButton = redoButton;
    }

    public void addUndoFunctionality(JTextPane component)
    {
        undoManager = new UndoManager();
        Document document = component.getDocument();
        document.addUndoableEditListener(event -> undoManager.addEdit(event.getEdit()));
        bindUndo();
        bindRedo();

    }

    public void bindRedo()
    {
        component.getActionMap().put(REDO_KEY, new AbstractAction(REDO_KEY)
        {
            private static final long serialVersionUID = -1445905038544078634L;

            @Override
            public void actionPerformed(ActionEvent evt)
            {
                try
                {
                    if (undoManager.canRedo())
                    {
                        undoManager.redo();
                    }
                } catch (CannotRedoException ignore)
                {
                }
                updateButtons();
            }
        });
        component.getInputMap().put(redo, REDO_KEY);
        
        // button
        redoButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    if (undoManager.canRedo())
                    {
                        undoManager.redo();
                    }
                } catch (CannotUndoException ignore)
                {
                }
                updateButtons();
            }
        });

    }

    public void bindUndo()
    {
        component.getActionMap().put(UNDO_KEY, new AbstractAction(UNDO_KEY)
        {
            private static final long serialVersionUID = 145772814601473213L;

            @Override
            public void actionPerformed(ActionEvent evt)
            {
                try
                {
                    if (undoManager.canUndo())
                    {
                        undoManager.undo();
                    }
                } catch (CannotUndoException ignore)
                {
                }
                updateButtons();
            }
        });
        component.getInputMap().put(undo, UNDO_KEY);

        // button
        undoButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    if (undoManager.canUndo())
                    {
                        undoManager.undo();
                    }
                } catch (CannotUndoException ignore)
                {
                }
                updateButtons();
            }
        });
    }

    public void updateButtons()
    {
        undoButton.setEnabled(undoManager.canUndo());
        redoButton.setEnabled(undoManager.canRedo());
    }

}