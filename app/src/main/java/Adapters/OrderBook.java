package Adapters;

public class OrderBook {
    private String  supplierId, clientId, resDate, returnDate, bookName;
    public OrderBook() {}

    public OrderBook(String suppId, String clientId, String resDate, String returnDate, String bookName){
        this.supplierId = suppId;
        this.clientId = clientId;
        this.resDate = resDate;
        this.returnDate = returnDate;
        this.bookName=bookName;
    }

    public OrderBook(OrderBook od){
        this.supplierId = od.supplierId;
        this.clientId = od.clientId;
        this.resDate = od.resDate;
        this.returnDate = od.returnDate;
        this.bookName=od.bookName;
    }


    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getResDate() {return resDate;}
    public void setResDate(String resDate){ this.resDate = resDate;}
    public String getReturnDate() {return returnDate;}
    public void setReturnDate(String returnDate){ this.resDate = returnDate;}
    public String getBookName() {return bookName;}
    public void setBookName(String returnDate){ this.bookName = bookName;}
}