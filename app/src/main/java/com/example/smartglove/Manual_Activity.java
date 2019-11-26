package com.example.smartglove;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;

public class Manual_Activity extends SairSystem {

    private User user;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_layout);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        pdfView = findViewById(R.id.pdfv);
        pdfView.fromAsset("manual_aplicativo.pdf").load();

        ToolbarBack();
    }

    private void ToolbarBack() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbarBack);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_dp);
        getSupportActionBar().setTitle("Manual");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(Manual_Activity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
