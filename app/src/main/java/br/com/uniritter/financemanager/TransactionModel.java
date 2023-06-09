package br.com.uniritter.financemanager;

public class TransactionModel {
    private String id;
    private String note;
    private String amount;
    private String category;
    private String type;
    private String date;

    public TransactionModel() {
    }

    public TransactionModel(String id, String note, String amount, String category, String type, String date) {
        this.id = id;
        this.note = note;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
