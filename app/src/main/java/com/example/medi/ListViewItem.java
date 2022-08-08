package com.example.medi;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private String productName;
    public Boolean enabled;

    public void setName(String name) {
        productName = name;
    }
    public void setEnabled(Boolean isEnabled) { enabled = isEnabled; }

    public String getName() {
        return this.productName;
    }
    public Boolean getEnabled() { return this.enabled; }
}
