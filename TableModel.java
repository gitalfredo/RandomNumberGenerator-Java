package rngbuilder;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
    private ArrayList<String> passList;

    public TableModel(ArrayList<String> passList) {
        this.passList = passList;
    }
    // Other TabelModel methods...

    public void refresh() {
        fireTableDataChanged();
    }

	public ArrayList<String> getPassList() {
		return passList;
	}

	public void setPassList(ArrayList<String> passList) {
		this.passList = passList;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return passList.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 1;
	}
	

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return "Passwords";
	}


	@Override
	public void fireTableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		super.fireTableChanged(e);
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return passList.get(rowIndex);
	}
}