package com.example.designapptest.model;

public class LoadImageModel {
    String path;
    boolean checked;

    public LoadImageModel(String path, boolean checked) {
        this.path = path;
        this.checked = checked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


}
