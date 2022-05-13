package com.loki.credo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.loki.credo.R;
import com.loki.credo.SettingsActivity;
import com.loki.credo.database.MainDao;
import com.loki.credo.database.RoomDB;
import com.loki.credo.models.Contacts;
import com.loki.credo.utils.ContactClickListener;

import java.util.List;

public class CallContactsAdapter extends RecyclerView.Adapter<CallContactsAdapter.CallContactViewHolder> {

    private Context context;
    private List<Contacts> contacts;

    private ContactClickListener mListener;

    RoomDB database;

    public CallContactsAdapter(Context context, List<Contacts> contacts, ContactClickListener contactClickListener) {
        this.context = context;
        this.contacts = contacts;
        this.mListener = contactClickListener;
    }

    @NonNull
    @Override
    public CallContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.emergency_contact_layout, parent, false);
        return new CallContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallContactViewHolder holder, int position) {

        holder.mContact.setText(contacts.get(position).getContact());

        holder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context.getApplicationContext(), SettingsActivity.class);
                intent.putExtra("position", holder.getAdapterPosition());
                mListener.onClick(view, contacts.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class CallContactViewHolder extends RecyclerView.ViewHolder{

        TextView mContact;
        CheckBox mChecked;
        ImageView mMore;

        public CallContactViewHolder(@NonNull View itemView) {
            super(itemView);

            mChecked = itemView.findViewById(R.id.new_check);
            mContact = itemView.findViewById(R.id.emerge_new);
            mMore = itemView.findViewById(R.id.menu_select);
        }
    }

}
