package com.example.groupssms.ui.sendmessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupssms.MessageModel;
import com.example.groupssms.OnClickItemEventListener;
import com.example.groupssms.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    List<MessageModel> messageModelList;
    OnClickItemEventListener onClickItemEventListener;

    public MessageAdapter(List<MessageModel> messageModelList, OnClickItemEventListener onClickItemEventListener) {
        this.messageModelList = messageModelList;
        this.onClickItemEventListener = onClickItemEventListener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sendmessage_message, parent, false), onClickItemEventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        holder.setData(messageModel);
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView, descriptionTextView;
        OnClickItemEventListener onClickItemEventListener;
        public MessageViewHolder( View itemView, OnClickItemEventListener onClickItemEventListener) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.item_sendmessage_message_nameTextView);
            descriptionTextView = itemView.findViewById(R.id.item_sendmessage_message_descriptionTextView);
            this.onClickItemEventListener = onClickItemEventListener;

            itemView.setOnClickListener(this);
        }

        public void setData(MessageModel messageModel){
            nameTextView.setText(messageModel.getName());
            descriptionTextView.setText(messageModel.getDescription());
        }

        @Override
        public void onClick(View view) {
            onClickItemEventListener.onClickItemEvent(getAdapterPosition());
        }
    }
}
