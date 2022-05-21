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
import android.widget.ListView;
import android.widget.Toast;

import com.example.sharingbooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Adapters.Book;

public class BookSearchResult extends AppCompatActivity {
    private ListView listViewBooks;
    private DatabaseReference supplier_ref;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> search_result_list;
    private ArrayList<Book> books_list;
    private Bundle bundle;
    private ArrayAdapter<String> arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search_result);
        //set text
        listViewBooks = (ListView) findViewById(R.id.supplier_books);
        //set database
        firebaseAuth = FirebaseAuth.getInstance();
        supplier_ref = FirebaseDatabase.getInstance().getReference("Suppliers");
        //set list results
        bundle = this.getIntent().getExtras(); //list_books
        books_list = new ArrayList<>();
        search_result_list = new ArrayList<>();
        books_list = (ArrayList<Book>) bundle.get("list");
        supplier_ref=FirebaseDatabase.getInstance().getReference().child("Suppliers").child(FirebaseAuth.getInstance().getUid()).child("Books");

        setAdapter();
        showBooks();
    }

    private void setAdapter() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, search_result_list);
        listViewBooks.setAdapter(arrayAdapter);
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BookSearchResult.this, BookDetailsClient.class);
                i.putExtra("book",books_list.get(position)); //send book object to the next activity(BookDetailsClient)
                startActivity(i);
            }
        });
    }

    private void showBooks() {
        for(Book b: books_list){
            search_result_list.add(b.getName());
            arrayAdapter.notifyDataSetChanged();
        }
        if (search_result_list.isEmpty()) {
            Toast.makeText(BookSearchResult.this, "No results", Toast.LENGTH_SHORT).show();

            arrayAdapter.notifyDataSetChanged();
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
            Intent intent = new Intent(this, MainClient.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}