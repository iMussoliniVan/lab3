package com.company;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    private Double result[] = new Double[2];
    public GornerTableModel(Double from, Double to, Double step,
                            Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }
    public Double getStep() {
        return step;
    }
    public int getColumnCount() {
        return 4;
    }
    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }
    public Object getValueAt(int row, int col) {
        double x = from + step * row;
        if (col == 0) {
            return x;
        } else if (col == 1) {
            result[0] = 0.0;
            for (int i = 0; i < coefficients.length; i++) {
                result[0] += Math.pow(x, coefficients.length - 1 - i) * coefficients[i];
            }
            return result[0];
        } else if (col == 2) {
            result[1] = 0.0;
            int p = coefficients.length - 1;
            for (int i = 0; i < coefficients.length; i++) {
                result[1] += Math.pow(x, coefficients.length - 1 - i) * coefficients[p--];
            }
            return result[1];
        } else {
            return result[1] - result[0];
        }
    }
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            default:
                return "Значение многочлена";
        }
    }
    public Class<?> getColumnClass(int col) {
        return Double.class;
    }
}
