package com.datting_package.Flirty_Datting_App.Models;

public class InfoListModel {

    private boolean isSelected;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String animal) {
        this.text = animal;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
