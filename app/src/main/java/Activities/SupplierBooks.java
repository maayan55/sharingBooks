package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sharingbooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.Book;

public class SupplierBooks extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private Button addBook;
    private DatabaseReference userRef;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> bookItemName;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_books);
        setUIViews();
        setAdapter();
        showBooks();
    }

    private void setUIViews(){
        listView = (ListView) findViewById(R.id.bookList);
        //set button
        addBook = (Button) findViewById(R.id.addBook);
        addBook.setOnClickListener((View.OnClickListener) this);
        //set database
        firebaseAuth= FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Suppliers").child(firebaseAuth.getUid());
    }

    private void setAdapter() {
        bookItemName = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookItemName);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SupplierBooks.this, BookDetailsSupplier.class);
                i.putExtra("book",bookItemName.get(position)); //send book object to the next activity(BookDetailsClient)
                startActivity(i);
            }
        });
    }

    private void showBooks() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild("Books")) {  //if the supplier dont have any books yet
                    bookItemName.add("You don't have books yet");
                    arrayAdapter.notifyDataSetChanged();
                }
                else {
                    for (DataSnapshot book: snapshot.child("Books").getChildren()) {
                        Book b = book.getValue(Book.class);
                        bookItemName.add(b.getName());
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addBook){
            startActivity(new Intent(SupplierBooks.this, AddBook.class));
        }
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
            Intent intent = new Intent(this, MainSupplier.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}