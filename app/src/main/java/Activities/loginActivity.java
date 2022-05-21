package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sharingbooks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import Adapters.Client;
import Adapters.Supplier;


public class loginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginClient,loginSupplier, clientRegist,supplierRegist;
    private EditText userEmail,userPassword;
    DatabaseReference user_ref;
    FirebaseAuth fireBaseAuth;
//    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //text
        userPassword= (EditText) findViewById(R.id.password);
        userEmail= (EditText) findViewById(R.id.Email);
        //buttons
        loginClient=(Button)findViewById(R.id.loginClient);
        loginClient.setOnClickListener(this);
        loginSupplier=(Button)findViewById(R.id.loginSupplier);
        loginSupplier.setOnClickListener(this);
        clientRegist=(Button)findViewById(R.id.clientRegist);
        clientRegist.setOnClickListener(this);
        supplierRegist=(Button)findViewById(R.id.supplierRegist);
        supplierRegist.setOnClickListener(this);
        //firebase
        fireBaseAuth= FirebaseAuth.getInstance();
        user_ref = FirebaseDatabase.getInstance().getReference();

    }
    private void validateClient(String username , String userPassword) {
        fireBaseAuth.signInWithEmailAndPassword(username,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if the user is client
                            for(DataSnapshot user: snapshot.child("Clients").getChildren()){ //move over all clients
                                String id = Objects.requireNonNull(user.child("details").getValue(Client.class)).getUserId();
                                if(id.equals(fireBaseAuth.getUid())){
                                    Toast.makeText(loginActivity.this, "Sign successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(loginActivity.this, MainClient.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                //if the user is not exist
                else{
                    Toast.makeText(loginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    task.getException();
                }
            }
        });

    }
    private void validateSupplier(String username , String userPassword) {
        fireBaseAuth.signInWithEmailAndPassword(username,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if the user is supplier
                            for(DataSnapshot user: snapshot.child("Suppliers").getChildren()){
                                String id = Objects.requireNonNull(user.child("details").getValue(Supplier.class)).getId();
                                if(id.equals(fireBaseAuth.getUid())){
                                    Toast.makeText(loginActivity.this, "Sign successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(loginActivity.this, MainSupplier.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                //if the user is not exist
                else{
                    Toast.makeText(loginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    task.getException();
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        if(v==loginClient){
            validateClient(userEmail.getText().toString(),userPassword.getText().toString());
        }
        if(v==loginSupplier){
            validateSupplier(userEmail.getText().toString(),userPassword.getText().toString());
        }
        if(v==clientRegist){
            startActivity(new Intent(this, ClientRegister.class));
        }
        if(v==supplierRegist){
            startActivity(new Intent(this, SupplierRegister.class));
        }
    }
}