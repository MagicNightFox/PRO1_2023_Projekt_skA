package cz.uhk.models;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
    private ShoppingList shoppingList;
    public TableModel(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex==3) {
            return Boolean.class;
        }
        return super.getColumnClass(columnIndex);
    }
    @Override
    public int getRowCount() {
        return shoppingList.getItems().size();
    }
    @Override
    public int getColumnCount() {
        return 4;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0)
            return shoppingList.getItems().get(rowIndex).getName();
        else if(columnIndex == 1)
            return shoppingList.getItems().get(rowIndex).getPrice();
        else if(columnIndex == 2)
            return shoppingList.getItems().get(rowIndex).getPieces();
        else if(columnIndex == 3)
            return shoppingList.getItems().get(rowIndex).isBought();

        String s = rowIndex + ":" + columnIndex;
        return s;
    }
    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return "Položka";
        return super.getColumnName(column);
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
        return columnIndex == 3;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex){
        if (columnIndex == 3){
            shoppingList.getItems().get(rowIndex).setBought(!shoppingList.getItems().get(rowIndex).isBought());
            fireTableCellUpdated(rowIndex,columnIndex);
        }
    }

}
