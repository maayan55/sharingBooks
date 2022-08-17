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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import Adapters.Supplier;
import static Activities.MyNotification.CHANNEL_CUSTOMER_ORDER;

public class MainSupplier extends AppCompatActivity  implements View.OnClickListener{
    private DatabaseReference messageRef;
    private String ID;
    private TextView displayName;
    private Button supp_profile, books_list;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference supplier_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_supplier);
        setViews();
        ID = firebaseAuth.getUid();
        checkMessage();
    }

    private void setViews(){
        //set button
        supp_profile =(Button) findViewById(R.id.supplierProfile);
        books_list =(Button) findViewById(R.id.bookList);
//        report_client = (Button) findViewById(R.id.report_client);

        supp_profile.setOnClickListener((View.OnClickListener) this);
        books_list.setOnClickListener((View.OnClickListener) this);
        //set text
        displayName = (TextView) findViewById(R.id.hello);
        //set firebase
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        supplier_ref = mFirebaseDatabase.getReference().child("Suppliers").child(firebaseAuth.getUid()).child("details");
        supplier_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name =snapshot.getValue(Supplier.class).getName();
                displayName.setText("Hey "+name+"! what do you want to do?");
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    };

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.supplierProfile){
            startActivity(new Intent(MainSupplier.this, SupplierProfile.class));
        }
        if(v.getId() == R.id.bookList){
            startActivity(new Intent(MainSupplier.this, SupplierBooks.class));
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
            Intent intent = new Intent(this, MainSupplier.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteMessages(String ID)
    {
        messageRef.child(ID).removeValue();
    }

    private void checkMessage()
    {
        messageRef = FirebaseDatabase.getInstance().getReference().child("messages");
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(ID)) {
                    String Title = snapshot.child(ID).child("Title").getValue().toString();
                    String Text = snapshot.child(ID).child("Text").getValue().toString();
                    deleteMessages(ID);
                    sendNotification(Title ,Text);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void sendNotification(String Title , String Text){
        NotificationManagerCompat NM = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(MainSupplier.this, CHANNEL_CUSTOMER_ORDER)
                .setSmallIcon(R.drawable.logo1)
                .setContentTitle(Title)
                .setContentText(Text)
                .build();
        NM.notify(2,notification);
    }
}