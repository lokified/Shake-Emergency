package com.loki.credo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.loki.credo.adapters.CallContactsAdapter;
import com.loki.credo.database.RoomDB;
import com.loki.credo.models.Contacts;
import com.loki.credo.utils.ContactClickListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TextView addContact;
    EditText mContact;
    Button mAdd;
    String contact;

    private AlertDialog dialog;

    private RecyclerView mContactRecycler;
    private CallContactsAdapter mContactsAdapter;

    RoomDB database;

    List<Contacts> contactsList = new ArrayList<>();
    Contacts selectedContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        database = RoomDB.getInstance(this);
        contactsList = database.mainDao().getAll();

        addContact = findViewById(R.id.add_contact);
        mContactRecycler = findViewById(R.id.emergency_recycler);


        setUpContacts(contactsList);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showPopupAdd();
            }
        });

    }

    //show popup to add
    private void showPopupAdd() {

        showPopup();

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact = mContact.getText().toString();

                addContactNumber(contact);

            }
        });

    }

    //adds new contact
    private void addContactNumber(String contact) {
        Contacts contacts = new Contacts();
        contacts.setContact(contact);

        database.mainDao().insert(contacts);

        dialog.dismiss();
        contactsList.clear();
        contactsList.addAll(database.mainDao().getAll());
        mContactsAdapter.notifyDataSetChanged();
    }


    //sets contacts in the recycler
    private void setUpContacts(List<Contacts> contacts) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mContactRecycler.setLayoutManager(layoutManager);

        mContactsAdapter = new CallContactsAdapter(this, contacts, contactClickListener);
        mContactRecycler.setAdapter(mContactsAdapter);
    }


    private final ContactClickListener contactClickListener = new ContactClickListener() {
        @Override
        public void onClick(View view, Contacts contacts) {
            selectedContact = new Contacts();
            selectedContact = contacts;
            showPopup(view);
        }
    };


    //shows popup menu for edit/delete
    private void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.more_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        int itemId = menuItem.getItemId();

        //edits contacts
        if (itemId == R.id.update) {
            showPopupEdit();
        }

        //deletes the contact
        if (itemId == R.id.delete) {
            deleteContact();
        }

        return true;
    }

    private void deleteContact() {
        database.mainDao().delete(selectedContact);
        contactsList.clear();
        contactsList.addAll(database.mainDao().getAll());
        mContactsAdapter.notifyDataSetChanged();
        Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
    }


    //show popup to edit the contacts
    private void showPopupEdit() {

        showPopup();

        mContact.setText(selectedContact.getContact());
        mAdd.setText("Update");

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact = mContact.getText().toString();
                database.mainDao().update(selectedContact.getId(), contact);

                contactsList.clear();
                contactsList.addAll(database.mainDao().getAll());
                mContactsAdapter.notifyDataSetChanged();
                dialog.dismiss();
                Toast.makeText(SettingsActivity.this, "updated", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //popup dialog
    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

        View popup = LayoutInflater.from(SettingsActivity.this).inflate(R.layout.contact_popup, null);

        mContact = popup.findViewById(R.id.et_contact);
        mAdd = popup.findViewById(R.id.btn_add_contact);

        builder.setView(popup);
        dialog = builder.create();
        dialog.show();
    }

}