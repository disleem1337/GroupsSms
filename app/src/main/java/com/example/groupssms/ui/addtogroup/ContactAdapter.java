package com.example.groupssms.ui.addtogroup;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupssms.ContactModel;
import com.example.groupssms.OnClickItemEventListener;
import com.example.groupssms.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    List<ContactModel> contactModelList;
    OnClickItemEventListener onContactClickListener;

    public ContactAdapter(List<ContactModel> contactModelList, OnClickItemEventListener onContactClickListener) {
        this.contactModelList = contactModelList;
        this.onContactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addtogroup_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactModel contactModel = contactModelList.get(position);
        holder.setData(contactModel);
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageImageView;
        TextView nameTextView, numberTextView;
        public ContactViewHolder(View view) {
            super(view);

            imageImageView = view.findViewById(R.id.item_addtogroup_contact_imageImageView);
            nameTextView = view.findViewById(R.id.item_addtogroup_contact_nameTextView);
            numberTextView = view.findViewById(R.id.item_addtogroup_contact_numberTextView);

            view.setOnClickListener(this);
        }

        public void setData(ContactModel contactModel) {
            nameTextView.setText(contactModel.getName());
            numberTextView.setText(contactModel.getNumber());

            if(contactModel.getImage() != null) {
                imageImageView.setImageURI(Uri.parse(contactModel.getImage()));
            }
        }

        @Override
        public void onClick(View view) {
            onContactClickListener.onClickItemEvent(getAdapterPosition());
        }
    }
}
