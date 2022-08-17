package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import Activities.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import Adapters.Book;
import Adapters.Supplier;

import java.util.ArrayList;
import java.util.Objects;

public class SupplierRegister extends AppCompatActivity{
    private  EditText supplierName, supplierPassword, supplierEmail, supplierPhone;
    private Button register;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private String name, email, password, phone;
    private ArrayList<Book> book_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_register);
        //text
        supplierName = (EditText) findViewById(R.id.supplierName);
        supplierEmail = (EditText) findViewById(R.id.EmailInput);
        supplierPassword = (EditText) findViewById(R.id.PasswordInput);
        supplierPhone = (EditText) findViewById(R.id.PhoneNumberInput);
        //buttons
        register = (Button) findViewById(R.id.registerSupplier);
        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validate()) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            System.out.println("1111111111111111111111111");
                            if (task.isSuccessful()) {
                                String userId =task.getResult().getUser().getUid();
                                Supplier user = new Supplier(name, email, phone, userId, book_list);
                                myRef.child("Suppliers").child(firebaseAuth.getUid()).child("details").setValue(user);
                                myRef.child("Suppliers").child(Objects.requireNonNull(firebaseAuth.getUid())).child("details").child("book_list");
                                Toast.makeText(SupplierRegister.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SupplierRegister.this, MainSupplier.class));
                                finish();
                            } else {
                                Toast.makeText(SupplierRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean validate (){
        Boolean validate=false;
        name = supplierName.getText().toString();
        phone = supplierPhone.getText().toString();
        email = supplierEmail.getText().toString().trim();
        password = supplierPassword.getText().toString().trim();

        if(name.isEmpty()){
            Toast.makeText(this,"please enter your name",Toast.LENGTH_SHORT).show();
        }
        else if(email.isEmpty()){
            Toast.makeText(this,"please enter your email",Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty()){
            Toast.makeText(this,"please enter your password",Toast.LENGTH_SHORT).show();
        }
        else if(phone.isEmpty()){
            Toast.makeText(this,"please enter your phone",Toast.LENGTH_SHORT).show();
        }
        else{
            validate=true;
        }

        return validate;
    }
}