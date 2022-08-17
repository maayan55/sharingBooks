package Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Activities.R;

public class acceptReserve extends AppCompatActivity implements View.OnClickListener{
    private Button ok_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reserve);
        //set button
        ok_button = (Button) findViewById(R.id.ok);
        ok_button.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ok){
            startActivity(new Intent(acceptReserve.this, SearchBook.class));
        }
    }
}