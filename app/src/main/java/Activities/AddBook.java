package Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.Toast;

import Adapters.Book;
import Adapters.Supplier;

import com.example.sharingbooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class AddBook extends AppCompatActivity implements View.OnClickListener {
    private EditText book_name, book_descriptor, book_borrowTime,book_securityDeposit;
    private ImageView img;
    private String name, description, burrowTime, available, category, author, publishing_year,language, location,securityDeposit,supplier_id;
    private Button add, upload, camera;
    private Spinner bookCategory, bookAuthor, bookPublishingYear ,bookLanguage, bookLocation;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference supplier_ref,book_ref;
    private Supplier book_list_obj;
    private ArrayList<Book> book_list;
    private StorageTask uploadTask;
    private StorageReference storageRef;
    private static final int GET_FROM_GALLERY = 3;
    private static final int GET_FROM_CAMERA = 0;
    byte[] byteData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        //set edit text
        book_name = (EditText) findViewById(R.id.BookName);
        book_descriptor = (EditText) findViewById(R.id.BookDescription);
        book_borrowTime = (EditText) findViewById(R.id.BookDaysBurrow);
        book_securityDeposit = (EditText) findViewById(R.id.BookSecurityDeposit);

        //set button
        add = (Button) findViewById(R.id.AddTheBook);
        upload = (Button) findViewById(R.id.BookUpImage);
        camera = (Button) findViewById(R.id.camera);
        add.setOnClickListener((View.OnClickListener) this);
        upload.setOnClickListener((View.OnClickListener) this);
        camera.setOnClickListener((View.OnClickListener) this);

        //set img
        img = (ImageView) findViewById(R.id.BookImage);

        //set spinner
        bookCategory = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.categoriesArray));
        bookCategory.setAdapter(categoriesAdapter);

        bookAuthor = (Spinner) findViewById(R.id.author_spinner);
        ArrayAdapter<String> authorsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.authorsArray));
        bookAuthor.setAdapter(authorsAdapter);

        bookPublishingYear  = (Spinner) findViewById(R.id.publishing_year_spinner);
        ArrayAdapter<String> publishingYearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.publishingYearsArray));
        bookPublishingYear .setAdapter(publishingYearAdapter);

        bookLanguage  = (Spinner) findViewById(R.id.language_spinner);
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.languagesArray));
        bookLanguage.setAdapter(languagesAdapter);

        bookLocation = (Spinner) findViewById(R.id.location_spinner);
        ArrayAdapter<String> locationsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.locationsArray));
        bookLocation.setAdapter(locationsAdapter);

        //set firebase
        firebaseAuth= FirebaseAuth.getInstance();
        supplier_ref = FirebaseDatabase.getInstance().getReference().child("Suppliers").child(firebaseAuth.getUid());
        storageRef = FirebaseStorage.getInstance().getReference("Images");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.BookUpImage){
            if (uploadTask != null && uploadTask.isInProgress()) makeToast("upload in progress");
            else uploadImage();
        }
        if(v.getId() == R.id.camera){
            if (uploadTask != null && uploadTask.isInProgress()) makeToast("upload in progress");
            else takePicture();
        }
        if(v.getId() == R.id.AddTheBook){
            addBook();
        }
    }

    private void uploadImage() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, GET_FROM_GALLERY);
    }
    private void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, GET_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == GET_FROM_CAMERA) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteData = stream.toByteArray();
            }
            if(requestCode == GET_FROM_GALLERY){
                Uri imguri = data.getData();
                img.setImageURI(imguri);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imguri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byteData = baos.toByteArray();
                }
                catch (FileNotFoundException e) { e.printStackTrace();}
                catch (IOException e) { e.printStackTrace();}
            }
        }
    }

    private void addBook(){
        //set string
        name = book_name.getText().toString();
        description = book_descriptor.getText().toString();
        burrowTime = book_borrowTime.getText().toString();
        available = "yes"; //when the book first added it is available
        category = bookCategory.getSelectedItem().toString();
        author = bookAuthor.getSelectedItem().toString();
        publishing_year = bookPublishingYear .getSelectedItem().toString();
        language = bookLanguage.getSelectedItem().toString();
        location = bookLocation.getSelectedItem().toString();
        securityDeposit = book_securityDeposit.getText().toString();
        supplier_id = firebaseAuth.getUid();

        if(validate()) { //if the supplier fill all the details about the book
            // add book to firebase
            Book book = new Book(name, description, burrowTime, available, category, author, publishing_year,language, location,securityDeposit, supplier_id);
            DatabaseReference book_ref = supplier_ref.child("Books").child(name);
            book_ref.setValue(book);
            // add img to DB
            if(byteData != null){
                String path = firebaseAuth.getUid() + "/" + name;
                uploadTask = storageRef.child(path).putBytes(byteData);
            }

            supplier_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) { //snapshot is the current supplier
                    if (snapshot.exists()) {
                        DataSnapshot book_list_obj = snapshot.child("details").child("book list");
                        ArrayList<Book> book_list1=new ArrayList<>();
                        if (!book_list_obj.exists()) {
                            book_list1 = new ArrayList<>();
                        }
                        else{
                            book_list1 = (ArrayList<Book>) book_list_obj.getValue();
                        }
                        book_list1.add(book);
                        supplier_ref.child("details").child("book list").setValue(book_list1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        if(validate()) { //if the supplier fill all the details
            startActivity(new Intent(AddBook.this, SupplierBooks.class));
            finish();
        }
    }

    private boolean validate(){ //have to fill all the details of the new book
        Boolean validate=false;

        if(name.isEmpty()){
            Toast.makeText(this,"please enter book name",Toast.LENGTH_SHORT).show();
        }
        else if(description.isEmpty()){
            Toast.makeText(this,"please enter book description",Toast.LENGTH_SHORT).show();
        }
        else if(burrowTime.isEmpty()){
            Toast.makeText(this,"please enter book burrow time",Toast.LENGTH_SHORT).show();
        }
        else if(category.isEmpty()){
            Toast.makeText(this,"please enter book category",Toast.LENGTH_SHORT).show();
        }
        else if(author.isEmpty()){
            Toast.makeText(this, "please enter book author", Toast.LENGTH_SHORT).show();
        }
        else if(publishing_year.isEmpty()){
            Toast.makeText(this, "please enter publishing year", Toast.LENGTH_SHORT).show();
        }
        else if(language.isEmpty()){
            Toast.makeText(this, "please enter language", Toast.LENGTH_SHORT).show();
        }
        else if(location.isEmpty()||location.equals("choose location")){
            Toast.makeText(this, "please enter book location", Toast.LENGTH_SHORT).show();
        }
        else if(img.getDrawable() == null){
            Toast.makeText(this, "please add picture of your book", Toast.LENGTH_SHORT).show();
        }
        else{
            validate=true;
        }
        return validate;
    }

    private void makeToast(String m){
        Toast.makeText(AddBook.this, m, Toast.LENGTH_SHORT).show();
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