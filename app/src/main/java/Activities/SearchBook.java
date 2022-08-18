package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import Activities.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import Adapters.Book;

public class SearchBook extends AppCompatActivity implements View.OnClickListener{
    private TextView clientName,title;
    private EditText namebook, nameauthor;
    private Spinner category_spinner,publishing_year_spinner,language_spinner, location_spinner;
    private Button search_book_button;
    private FirebaseAuth firebaseAuth;
    private ArrayAdapter<String> categoriesAdapter,locationsAdapter,authorsAdapter,publishingYearAdapter,languagesAdapter;
    private DatabaseReference suppRef,clientRef;
    private ArrayList<Book> list_books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        //set text
        clientName = (TextView) findViewById(R.id.client_name);
        title = (TextView) findViewById(R.id.mainActivity_title);
        //set button
        search_book_button = (Button)findViewById(R.id.search_button);
        search_book_button.setOnClickListener((View.OnClickListener)this);
        //set firebase
        firebaseAuth=FirebaseAuth.getInstance();
        suppRef = FirebaseDatabase.getInstance().getReference("Suppliers");
        //set name of the bbok
        namebook = (EditText) findViewById(R.id.name_of_book);
        nameauthor = (EditText) findViewById(R.id.name_of_author);
        //set category_spinner
        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.categoriesArray));
        categoriesAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item);
        category_spinner.setAdapter(categoriesAdapter);
        //set location_spinner
        location_spinner = (Spinner) findViewById(R.id.location_spinner);
        locationsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.locationsArray));
        locationsAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item);
        location_spinner.setAdapter(locationsAdapter);
        //set author_spinner
//        author_spinner = (Spinner) findViewById(R.id.author_spinner);
//        authorsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.authorsArray));
//        authorsAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item);
//        author_spinner.setAdapter(authorsAdapter);
        //set publishing_year_spinner
        publishing_year_spinner = (Spinner) findViewById(R.id.publishing_year_spinner);
        publishingYearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.publishingYearsArray));
        publishingYearAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item);
        publishing_year_spinner.setAdapter(publishingYearAdapter);

        //set language_spinner
        language_spinner = (Spinner) findViewById(R.id.language_spinner);
        languagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.languagesArray));
        languagesAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item);
        language_spinner.setAdapter(languagesAdapter);



        list_books=new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_button){
            searchBook();
        }
    }

    private void searchBook() {
        String category, location,author,publishing_year,language, name;
        name = namebook.getText().toString();
        author = nameauthor.getText().toString();
        category = category_spinner.getSelectedItem().toString();
        location = location_spinner.getSelectedItem().toString();
//        author= author_spinner.getSelectedItem().toString();
        publishing_year = publishing_year_spinner.getSelectedItem().toString();
        language= language_spinner.getSelectedItem().toString();

        suppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //for on the books
                list_books=new ArrayList<>();
                for (DataSnapshot sup : snapshot.getChildren()) {
                    for (DataSnapshot book : sup.child("Books").getChildren()) {
                        Book book1 = book.getValue(Book.class);
                        System.out.println(book1.getName() instanceof String);
                        System.out.println("name of the book" + name);
                        if (book1.getAvailable().equals("yes")) {
                            int counter = 0;
                            if (book1.getName().toLowerCase().contains(name) || name.equals("") ) {
                                counter++;
                            }
                            if (book1.getCategory().equals(category) || category.equals("choose book category")) {
                                counter++;
                            }
                            if (book1.getLocation().equals(location) || location.equals("choose book burrow location")) {
                                counter++;
                            }
                            if (book1.getAuthor().toLowerCase().contains(author) || author.equals("")) {
                                counter++;
                            }
                            if (book1.getPublishingYear().equals(publishing_year) || publishing_year.equals("choose book publishing year")) {
                                counter++;
                            }
                            if (book1.getLanguage().equals(language) || language.equals("choose book language")) {
                                counter++;
                            }
                            if (counter == 6) {
                                list_books.add(book1);
                            }
                        }
                    }
                }
                Intent i = new Intent(SearchBook.this, BookSearchResult.class);
                i.putExtra("list", list_books);
                startActivity(i);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,loginActivity.class));
        }
        if(item.getItemId() == R.id.MyProfile){
            Intent intent = new Intent(this, ClientProfile.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.Home){
            Intent intent = new Intent(this, MainClient.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}