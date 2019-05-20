package com.SriSaiKrishnConstruction.model;

public class Sites {
    String product_id;
    String product_name;
    String remaining;
    private boolean checked=false;

    public Sites(String product_id, String product_name, String remaining, boolean checked) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.remaining = remaining;
        this.checked = checked;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
