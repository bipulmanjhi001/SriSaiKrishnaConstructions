package com.SriSaiKrishnConstruction.model;

public class Stockist {
    private String id,product,total,out,remarks;

    public Stockist(String id, String product, String total, String out, String remarks) {
        this.id = id;
        this.product = product;
        this.total = total;
        this.out = out;
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
