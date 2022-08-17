package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import Activities.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapters.Supplier;

public class SupplierInfo extends AppCompatActivity {
    private EditText supp_name, supp_email, supp_phone;
    private Supplier supplier;
    private DatabaseReference supplier_ref;
    private FirebaseAuth firebaseAuth;
    private String supp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_info);

        //edit text
        supp_email = (EditText) findViewById(R.id.email_sup);
        supp_phone = (EditText) findViewById(R.id.phone_sup);
        supp_name = (EditText) findViewById(R.id.supplier_name);

        //get info(supp id) from previous activity
        Intent intent=getIntent();
        supp_id=intent.getStringExtra("supp_id");
        supplier_ref = FirebaseDatabase.getInstance().getReference("Suppliers").child(supp_id).child("details");
        supplier_ref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    supplier = snapshot.getValue(Supplier.class);
                    supp_email.setText(supplier.getEmail());
                    supp_phone.setText(supplier.getPhone());
                    supp_name.setText(supplier.getName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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