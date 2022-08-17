package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Activities.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapters.Client;

public class MainClient extends AppCompatActivity {
    private TextView title;
    private Button clientProfile, serachBook;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference client_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);
        setViews();
    }

    private void setViews() {
        //set button
        clientProfile = (Button) findViewById(R.id.supplierProfile);
        serachBook = (Button) findViewById(R.id.bookList);

        clientProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainClient.this,ClientProfile.class));
            }
        });
        serachBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainClient.this, SearchBook.class));
            }
        });

        //set text
        title = (TextView) findViewById(R.id.hello);
        //set firebase
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        client_ref = mFirebaseDatabase.getReference().child("Clients").child(firebaseAuth.getUid()).child("details");
        client_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = snapshot.getValue(Client.class).getName();
                title.setText("Hey " + name + "! what do you want to do?");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    };

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