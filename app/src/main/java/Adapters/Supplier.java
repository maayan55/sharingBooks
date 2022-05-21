package Adapters;

import java.util.ArrayList;

public class Supplier {
    private String name, email, phone, id;
    private ArrayList<Book> list_book;

    public Supplier(){}

    public Supplier(String name, String email,String phone,String id){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.id = id;

    }

    public Supplier(String name,String email, String phone,String id, ArrayList<Book> list_book) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.list_book=list_book;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() { return phone;}
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getId() {
        return id;
    }
    public ArrayList<Book> getBookList() { return list_book; }
    public void setBookList(ArrayList<Book> list_book) { this.list_book=list_book; }

}
