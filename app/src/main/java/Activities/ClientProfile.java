package Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.sharingbooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Adapters.Client;

public class ClientProfile extends AppCompatActivity implements View.OnClickListener{
    EditText client_name, client_phone;
    Button btnUpdate;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        //set edit text
        client_name = findViewById(R.id.nameClient);
        client_phone = findViewById(R.id.phoneClient);
        //set button
        btnUpdate =  findViewById(R.id.updateClient);
        btnUpdate.setOnClickListener((View.OnClickListener)this);
        //set DB ref
        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Clients").child(firebaseAuth.getUid());
        SetTextDetails();
    }



    private void SetTextDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //snapshot is the client object
                Client client = snapshot.child("details").getValue(Client.class);
                client_name.setText(client.getName());
                client_phone.setText(client.getPhone());
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.updateClient){
            updateClientDetails();
        }
    }

    private void updateClientDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //snapshot is the client
                if (snapshot.exists()) {
                    Client client = snapshot.child("details").getValue(Client.class);
                    String update_name = client_name.getText().toString();
                    String update_phone = client_phone.getText().toString();
                    client.setName(update_name);
                    client.setPhone(update_phone);
                    databaseReference.child("details").setValue(client);

                    Toast.makeText(ClientProfile.this, "Update", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ClientProfile.this,MainClient.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
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