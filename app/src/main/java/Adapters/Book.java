package Adapters;

import java.io.Serializable;


public class Book  implements Serializable {
    private String name, description, burrowTime, available, category, author,language, publishing_year, location, security_deposit, supp_id;
    private static final long serialVersionUID = 1234L;


    public Book(){}

    public Book(String name, String description, String burrowTime,String available, String category,
                String author, String publishing_year,String language, String location, String security_deposit, String supp_id){
        this.name = name;
        this.description = description;
        this.burrowTime = burrowTime;
        this.available = available;
        this.category = category;
        this.author = author;
        this.language = language;
        this.publishing_year = publishing_year;
        this.location = location;
        this.security_deposit = security_deposit;
        this.supp_id = supp_id;
    }
    //set & get
    public String getName () { return this.name;}
    public void setName (String name) { this.name = name;}
    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}
    public String getBurrowTime() {return this.burrowTime;}
    public void setBurrowTime(String burrowTime) {this.burrowTime = burrowTime;}
    public String getAvailable() {return this.available;}
    public void setAvailable(String available) {this.available = available;}
    public String getCategory() {return this.category;}
    public void setCategory(String category) {this.category = category;}
    public String getAuthor() {return this.author;}
    public void setAuthor1(String author) {this.author = author;}
    public String getLanguage() {return this.language;}
    public void setLanguage(String language) {this.language = language;}
    public String getPublishingYear() {return this.publishing_year;}
    public void setPublishingYear(String publishing_year) {this.publishing_year = publishing_year;}
    public String getLocation() {return this.location;}
    public void setLocation(String location) {this.location = location;}
    public String getSecurity_deposit(){
        return this.security_deposit;
    }
    public void setSecurity_deposit(String security_deposit) {this.security_deposit = security_deposit;}
    public String getSupp_id() {return this.supp_id;}
}