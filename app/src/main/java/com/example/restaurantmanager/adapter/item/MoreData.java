package com.example.restaurantmanager.adapter.item;

import com.example.restaurantmanager.enums.MoreMenuEnum;

public class MoreData {

    private String label;
    private int icon;
    private MoreMenuEnum id;
    private boolean enabled;

    public MoreData(String label, int icon, MoreMenuEnum id, boolean enabled) {
        this.label = label;
        this.icon = icon;
        this.id = id;
        this.enabled = enabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public MoreMenuEnum getId() {
        return id;
    }

    public void setId(MoreMenuEnum id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
