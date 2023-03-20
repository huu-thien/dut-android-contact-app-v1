package com.mycontactapp.viewmodel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mycontactapp.R;
import com.mycontactapp.model.AppDatabase;
import com.mycontactapp.model.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements Filterable {

    private AppDatabase db;

    private Context context;
    private List<Contact> contactList;
    private List<Contact> contactListOld;
    ShapeDrawable background;
    private List<Contact> contactListSearch;

    public ContactAdapter(Context context) {
        this.context = context;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        this.contactListOld = contactList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        String firstCharacter = String.valueOf(contact.getFirstName().charAt(0)).toUpperCase();
        holder.tvName.setText(contact.getFirstName() + " " + contact.getLastName());
        holder.tvCircle.setText(firstCharacter);
        holder.tvCharacter.setText(firstCharacter);

        background = new ShapeDrawable(new OvalShape());
        background.getPaint().setColor(Color.GRAY);
        holder.tvCircle.setBackground(background);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString().trim();
                db = AppDatabase.getInstance(context);
                if (strSearch.isEmpty()) {
                    contactList = contactListOld;
                } else {
                    contactListSearch = db.contactDao().findUserWithName(strSearch);
                    contactList = contactListSearch;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactList = (List<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvCircle;
        public TextView tvCharacter;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvCircle = (TextView) view.findViewById(R.id.tv_circle);
            tvCharacter = (TextView) view.findViewById(R.id.tv_character);
        }
    }
}
