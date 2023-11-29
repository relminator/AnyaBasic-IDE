package net.phatcode.rel.ide;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

public class ContextMenuMouseListener extends MouseAdapter
{
	
	private JPopupMenu popup = new JPopupMenu();

	private Action cutAction;
	private Action copyAction;
	private Action pasteAction;
	private Action undoAction;
	private Action selectAllAction;

	private JTextComponent textComponent;
	private String savedString = "";
	private Actions lastActionSelected;

	private enum Actions
	{
		UNDO, CUT, COPY, PASTE, SELECT_ALL
	};

	public ContextMenuMouseListener()
	{
//		copyAction = textComponent.getActionMap().get("Copy");
//		cutAction = textComponent.getActionMap().get("Cut");
//		pasteAction = textComponent.getActionMap().get("Paste");
//		undoAction = textComponent.getActionMap().get("Undo");
//		selectAllAction = textComponent.getActionMap().get("Select All");
//
//
//		 popup.add (undoAction);
//		 popup.addSeparator();
//		 popup.add (cutAction);
//		 popup.add (copyAction);
//		 popup.add (pasteAction);
//		 popup.addSeparator();
//		 popup.add (selectAllAction);

		 //return popup;
		undoAction = new AbstractAction("Undo")
		{

			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				textComponent.setText("");
				textComponent.replaceSelection(savedString);

				lastActionSelected = Actions.UNDO;
			}
		};

		popup.add(undoAction);
		popup.addSeparator();

		cutAction = new AbstractAction("Cut")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				lastActionSelected = Actions.CUT;
				savedString = textComponent.getText();
				textComponent.cut();
			}
		};

		popup.add(cutAction);

		copyAction = new AbstractAction("Copy")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				lastActionSelected = Actions.COPY;
				textComponent.copy();
			}
		};

		popup.add(copyAction);

		pasteAction = new AbstractAction("Paste")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				lastActionSelected = Actions.PASTE;
				savedString = textComponent.getText();
				textComponent.paste();
			}
		};

		popup.add(pasteAction);
		popup.addSeparator();

		selectAllAction = new AbstractAction("Select All")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				lastActionSelected = Actions.SELECT_ALL;
				textComponent.selectAll();
			}
		};

		popup.add(selectAllAction);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getModifiers() == InputEvent.BUTTON3_MASK)
		{
			if (!(e.getSource() instanceof JTextComponent))
			{
				return;
			}

			textComponent = (JTextComponent) e.getSource();
			textComponent.requestFocus();

			boolean enabled = textComponent.isEnabled();
			boolean editable = textComponent.isEditable();
			boolean nonempty = !(textComponent.getText() == null || textComponent.getText().equals(""));
			boolean marked = textComponent.getSelectedText() != null;

			boolean pasteAvailable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null)
					.isDataFlavorSupported(DataFlavor.stringFlavor);

			undoAction.setEnabled(
					enabled && editable && (lastActionSelected == Actions.CUT || lastActionSelected == Actions.PASTE));
			cutAction.setEnabled(enabled && editable && marked);
			copyAction.setEnabled(enabled && marked);
			pasteAction.setEnabled(enabled && editable && pasteAvailable);
			selectAllAction.setEnabled(enabled && nonempty);

			int nx = e.getX();

			if (nx > 500)
			{
				nx = nx - popup.getSize().width;
			}

			popup.show(e.getComponent(), nx, e.getY() - popup.getSize().height);
		}
	}
}