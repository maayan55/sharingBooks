package Activities;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.sharingbooks.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.Client;
import Adapters.Book;
import Adapters.OrderBook;
import Adapters.Supplier;

public class BookDetailsClient extends AppCompatActivity implements View.OnClickListener{
    private TextView book_name, book_description,book_category,book_author,book_publishing_year, book_language, book_location, book_burrow_days,book_securityDeposit, book_available;
    //ask about category
    private Button burrow,supp_info;
    private String client_email,supplier_email;
    private ImageView img;
    private DatabaseReference suppRef, mainRef,orderRef;
    private FirebaseAuth firebaseAuth;
    private String suppId;
    private Book book;
    private LocalDate current_date;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details_client);
        //set text
        book_name = (TextView) findViewById(R.id.book_name_text);
        book_available = (TextView) findViewById(R.id.available_book);
        book_description = (TextView) findViewById(R.id.description_book);
        book_category = (TextView) findViewById(R.id.category_book);
        book_author = (TextView) findViewById(R.id.author_book);
        book_publishing_year = (TextView) findViewById(R.id.publishing_year_book);
        book_language = (TextView) findViewById(R.id.language_book);
        book_location= (TextView) findViewById(R.id.location_book);
        book_burrow_days = (TextView) findViewById(R.id.burrow_days_book);
        book_securityDeposit = (TextView) findViewById(R.id.security_deposit_book);
        current_date=java.time.LocalDate.now();


        //set img
        img = (ImageView) findViewById(R.id.book_image);
        //set button
        burrow = (Button) findViewById(R.id.burrow_book);
        burrow.setOnClickListener((View.OnClickListener) this);
        supp_info =(Button) findViewById(R.id.supplier_info);
        supp_info.setOnClickListener((View.OnClickListener) this);
        //set id
        Bundle book_bundle=getIntent().getExtras();
        Book book=(Book) book_bundle.get("book");
        suppId = book.getSupp_id();

        //set firebase
        firebaseAuth= FirebaseAuth.getInstance();
        mainRef = FirebaseDatabase.getInstance().getReference();
        suppRef = FirebaseDatabase.getInstance().getReference("Suppliers").child(suppId).child("Books").child(book.getName());

        getBookDetails();
    }


    private void getBookDetails() {
        suppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    book = snapshot.getValue(Book.class);
                    book_name.setText(book.getName());
                    book_description.setText( book.getDescription());
                    book_burrow_days.setText( book.getBurrowTime());
                    book_available.setText( book.getAvailable());
                    book_author.setText( book.getAuthor());
                    book_publishing_year.setText( book.getPublishingYear());
                    book_language.setText( book.getLanguage());
                    book_location.setText( book.getLocation());
                    book_securityDeposit.setText( book.getSecurity_deposit());
                   // book_size.setText(( book.getSize()));
                    getImg();
                }
            }

            private void getImg(){

                String newPath = suppId + "/" + book_name.getText();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("Images");

                final long ONE_MEGABYTE = (long) Math.pow(1024, 10);
                storageRef.child(newPath).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        img.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) { }
                });
            }

            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void burrowBook() { //didnt implement yet
        if (book.getAvailable().equals("yes")) {
            Book update_book = book;
            update_book.setAvailable("no");
            suppRef.setValue(update_book);
            suppRef = FirebaseDatabase.getInstance().getReference("Suppliers").child(suppId);
            orderRef = suppRef;
            suppRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ArrayList<Book> update_list = new ArrayList<>();
//                        for (DataSnapshot book3 : snapshot.child("details").child("book list").getChildren()) {
//                            if (!book.getValue(Book.class).getName().equals(update_book.getName())) {
//                                update_list.add(book3.getValue(Book.class));
//                            }
//                        }
//                        suppRef.child("details").child("book list").setValue(update_list);
                        String client_id=FirebaseAuth.getInstance().getUid();
                        String supplier_id=suppId;
                        String burrow_date=current_date.toString();
                        String return_date=(current_date.plusDays(Integer.parseInt(book.getBurrowTime()))).toString();
                        OrderBook order_book=new OrderBook(supplier_id,client_id,burrow_date,return_date,update_book.getName());

                        orderRef.child("orders").push().setValue(order_book);
                        mainRef.child("Clients").child(client_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Client client=snapshot.getValue(Client.class);
                                client_email=client.getEmail();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        mainRef.child("Suppliers").child(supplier_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Supplier supplier=snapshot.getValue(Supplier.class);
                                supplier_email=supplier.getEmail();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            startActivity(new Intent(BookDetailsClient.this, acceptReserve.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.burrow_book) { //didnt implement yet
            burrowBook();
        }
        if (v.getId() == R.id.supplier_info) { //send to the next page the id of the supplier of the book
            Intent i = new Intent(BookDetailsClient.this, SupplierInfo.class);
            i.putExtra("supp_id", suppId);
            startActivity(i);
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