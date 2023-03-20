package com.mycontactapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mycontactapp.databinding.ActivityMainBinding;
import com.mycontactapp.model.AppDatabase;
import com.mycontactapp.model.Contact;
import com.mycontactapp.model.ContactDao;
import com.mycontactapp.viewmodel.ContactAdapter;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private ActivityMainBinding binding;
    private ContactAdapter contactAdapter;
    private ContactDao contactDao;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        initRecyclerView();
        loadListContact();

    }

    private void initRecyclerView() {
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.rvContacts.addItemDecoration(dividerItemDecoration);

        contactAdapter = new ContactAdapter(this);
        binding.rvContacts.setAdapter(contactAdapter);

        db = AppDatabase.getInstance(this.getApplicationContext());
    }

    private void loadListContact() {
        List<Contact> contactList = db.contactDao().getAll();
        contactAdapter.setContactList(contactList);
    }

    private void saveNewContact(String firstName, String lastName, String phone, String mail) {
        Contact contact = new Contact(firstName, lastName, phone, mail);
        db.contactDao().insert(contact);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_left);
        getMenuInflater().inflate(R.menu.new_contact_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.btn_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contactAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void view_onClick(View view) {
        Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
    }

    public void btn_add_onClick(View view) {
        Intent intent = new Intent(this, NewContactActivity.class);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String firstName = data.getStringExtra("firstName").toString();
                String lastName = data.getStringExtra("lastName").toString();
                String phone = data.getStringExtra("phone").toString();
                String mail = data.getStringExtra("mail").toString();
                saveNewContact(firstName, lastName, phone, mail);
                Toast.makeText(MainActivity.this, "Add successfully", Toast.LENGTH_SHORT).show();
                loadListContact();
            }
        }
    });

}