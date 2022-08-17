package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import Activities.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

import Adapters.Client;

public class ClientRegister extends AppCompatActivity {
    private  EditText clientName, clientPassword, clientEmail, clientPhone; //the details the new client fill
    private Button register;
    String name, email, password, phone;
    DatabaseReference myRef;
    FirebaseAuth fireBaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
        clientName = (EditText) findViewById(R.id.fullName);
        clientPassword = (EditText) findViewById(R.id.password);
        clientEmail = (EditText) findViewById(R.id.email);
        register =(Button) findViewById(R.id.submit);
        clientPhone = (EditText) findViewById(R.id.Phone);
        fireBaseAuth= FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(validate()){
                    String client_email= clientEmail.getText().toString().trim();
                    String client_password= clientPassword.getText().toString().trim();

                    fireBaseAuth.createUserWithEmailAndPassword(client_email,client_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        String client_id=task.getResult().getUser().getUid();
                                        name = clientName.getText().toString();
                                        email= clientEmail.getText().toString().trim();
                                        password= clientPassword.getText().toString().trim();
                                        phone = clientPhone.getText().toString();
                                        Client user = new Client(name, email, phone,client_id);
                                        myRef.child("Clients").child(client_id).child("details").setValue(user);
                                        Toast.makeText(ClientRegister.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ClientRegister.this, MainClient.class));
                                    }
                                    else{
                                        Toast.makeText(ClientRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                        task.getException();
                                    }
                                }
                            });
                }
            }
        });

    }

    private boolean validate(){
        Boolean validate=false;

        String name= clientName.getText().toString();
        String password= clientPassword.getText().toString();
        String email= clientEmail.getText().toString();
        String phone= clientPhone.getText().toString();

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