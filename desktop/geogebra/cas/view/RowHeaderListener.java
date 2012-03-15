package geogebra.cas.view;

import geogebra.main.Application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RowHeaderListener extends MouseAdapter implements KeyListener, ListSelectionListener, MouseMotionListener {

	private final CASTable table;
	private final JList rowHeader;
	private int mousePressedRow;
	private boolean rightClick;

	public RowHeaderListener(CASTable table, JList rowHeader) {
		this.table = table;
		this.rowHeader = rowHeader;
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		rightClick = Application.isRightClick(e);
		table.stopEditing();
		mousePressedRow = rowHeader.locationToIndex(e.getPoint());
		rowHeader.requestFocus();
	}

	public void mouseDragged(MouseEvent e) {
		e.consume();

		// update selection
		int mouseDraggedRow = rowHeader.locationToIndex(e.getPoint());

		// make sure mouse pressed is initialized, this may not be the case
		// after closing the popup menu
		if (mousePressedRow < 0) {
			table.stopEditing();
			mousePressedRow = mouseDraggedRow;
		}

		rowHeader.setSelectionInterval(mousePressedRow, mouseDraggedRow);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		e.consume();
		mousePressedRow = -1;

		// int mouseReleasedRow = rowHeader.locationToIndex(e.getPoint());

		// update selection if:
		// mouseReleasedRow is not selected yet
		// or
		// we did a left click without drag
		// if (!rowHeader.isSelectedIndex(mouseReleasedRow) || !rightClick &&
		// !dragged) {
		// rowHeader.setSelectedIndex(mouseReleasedRow);
		// }

		// handle right click
		if (rightClick) {
			RowHeaderPopupMenu popupMenu = new RowHeaderPopupMenu(rowHeader,
					table);
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		e.consume();
	}

	public void keyPressed(KeyEvent e) {
		boolean undoNeeded = false;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
			int[] selRows = rowHeader.getSelectedIndices();
			if (selRows.length > 0) {
				for (int i = selRows.length - 1; i >= 0; i--) {
					table.deleteRow(selRows[i]);
				}
				undoNeeded = true;
			}
			break;
		}

		if (undoNeeded) {
			// store undo info
			table.app.storeUndoInfo();
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}


	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		int minIndex = lsm.getMinSelectionIndex();
        int maxIndex = lsm.getMaxSelectionIndex();
        if(minIndex == maxIndex)
        	table.startEditingRow(minIndex);
	}
}
