/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author nima
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer{
  
    private int mRowId;
    private Color mColor = null;
    
    public CustomTableCellRenderer(int rowId, Color color){
        mRowId = rowId;
        mColor = color;
    }
    
    @Override
    public Component getTableCellRendererComponent (JTable table, 
        Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
        if (row == mRowId && column == 5)
            cell.setForeground(mColor);
        return cell;
   }
}
