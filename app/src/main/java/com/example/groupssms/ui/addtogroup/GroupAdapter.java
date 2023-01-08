package com.example.groupssms.ui.addtogroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupssms.GroupModel;
import com.example.groupssms.OnClickItemEventListener;
import com.example.groupssms.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    List<GroupModel> groupModelList;
    OnClickItemEventListener onClickItemEventListener;
    public GroupAdapter(List<GroupModel> groupModelList, OnClickItemEventListener onClickItemEventListener) {
        this.groupModelList = groupModelList;
        this.onClickItemEventListener = onClickItemEventListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder groupViewHolder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addtogroup_group, parent, false), onClickItemEventListener);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupModel groupModel = groupModelList.get(position);
        holder.setData(groupModel);
    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView groupImageView;
        TextView groupNameTextView, groupDescriptionTextView;

        OnClickItemEventListener onClickItemEventListener;
        public GroupViewHolder( View itemView, OnClickItemEventListener onClickItemEventListener) {
            super(itemView);

            groupImageView = itemView.findViewById(R.id.item_addtogroup_group_imageImageView);
            groupNameTextView = itemView.findViewById(R.id.item_addtogroup_group_nameTextView);
            groupDescriptionTextView = itemView.findViewById(R.id.item_addtogroup_group_descriptionTextView);

            this.onClickItemEventListener = onClickItemEventListener;
            itemView.setOnClickListener(this);
        }

        public void setData(GroupModel groupModel) {
            groupNameTextView.setText(groupModel.getName());
            groupDescriptionTextView.setText(groupModel.getDescription());

            if(groupModel.getImage() != null){
                Picasso.get().load(groupModel.getImage()).into(groupImageView);
            }
        }

        @Override
        public void onClick(View view) {
            onClickItemEventListener.onClickItemEvent(getAdapterPosition());
        }
    }
}
