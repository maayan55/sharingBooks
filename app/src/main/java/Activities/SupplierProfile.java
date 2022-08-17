package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Activities.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapters.Supplier;


public class SupplierProfile extends AppCompatActivity implements View.OnClickListener{
    EditText sup_name, sup_phone;
    Button btnUpdate;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_profile);
        setUIViews();
        setDetails();
    }

    private void setUIViews() {
        //set edit text
        sup_name = findViewById(R.id.sup_name);
        sup_phone = findViewById(R.id.sup_phone);
        //set button
        btnUpdate =  findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener((View.OnClickListener)this);
        //set DB ref
        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Suppliers").child(firebaseAuth.getUid());
    }

    private void setDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //snapshot is the supplier
                Supplier s = snapshot.child("details").getValue(Supplier.class);
                sup_name.setText(s.getName());
//                sup_address.setText(s.getAddress());
                sup_phone.setText(s.getPhone());
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnUpdate){
            updateDetails();
        }
    }

    private void updateDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //snapshot is the supplier
                if (snapshot.exists()) {
                    Supplier s = snapshot.child("details").getValue(Supplier.class);
                    String uName, uPhone;
                    uName = sup_name.getText().toString();
                    uPhone = sup_phone.getText().toString();
                    s.setName(uName);
                    s.setPhone(uPhone);
                    databaseReference.child("details").setValue(s);
                    Toast.makeText(SupplierProfile.this, "Update", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SupplierProfile.this,MainSupplier.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    //**menu bar**

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SupplierProfile.this,loginActivity.class));
    }

    public void openMain(){
        Intent intent = new Intent(this, MainSupplier.class);
        startActivity(intent);
    }

    public void openPrivateZone(){
        Intent intent = new Intent(this, SupplierProfile.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Logout){
            Logout();
        }
        if(item.getItemId() == R.id.MyProfile){
            openPrivateZone();
        }
        if(item.getItemId() == R.id.Home){
            openMain();
        }
        return super.onOptionsItemSelected(item);
    }
}