package com.mycontactapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mycontactapp.databinding.ActivityNewContactBinding;

public class NewContactActivity extends AppCompatActivity {

    private ActivityNewContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewContactBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_new_contact_menu, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_cancel);
        return true;
    }

    public void cancel_onClick(View view) {
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tv_save:
                String firstName = binding.editTextFirstName.getText().toString().trim();
                String lastName = binding.editTextLastName.getText().toString().trim();
                String phone = binding.editTextPhone.getText().toString().trim();
                String mail = binding.editTextMail.getText().toString().trim();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("firstName", firstName);
                returnIntent.putExtra("lastName", lastName);
                returnIntent.putExtra("phone", phone);
                returnIntent.putExtra("mail", mail);
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}