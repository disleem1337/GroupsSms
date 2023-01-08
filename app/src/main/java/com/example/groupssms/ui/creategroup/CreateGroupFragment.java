package com.example.groupssms.ui.creategroup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.groupssms.GroupModel;
import com.example.groupssms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateGroupFragment extends Fragment {

    EditText groupNameEditText, groupDescriptionEditText;
    ImageView groupImageView;
    Button createGroupButton;
    RecyclerView groupsRecyclerView;

    Uri filePath;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage mStorage;

    ArrayList<GroupModel> groupModelArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_group, container, false);

        groupNameEditText = view.findViewById(R.id.creategroup_groupNameEditText);
        groupDescriptionEditText = view.findViewById(R.id.creategroup_groupDescriptionEditText);
        groupImageView = view.findViewById(R.id.creategroup_groupImageImageView);
        createGroupButton = view.findViewById(R.id.creategroup_createGroupButton);
        groupsRecyclerView = view.findViewById(R.id.creategroup_groupsRecyclerView);

        groupModelArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        filePath = data.getData();
                        groupImageView.setImageURI(filePath);
                    }
                }
        );

        groupImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

        createGroupButton.setOnClickListener(v -> {
            String groupName = groupNameEditText.getText().toString();
            String groupDescription = groupDescriptionEditText.getText().toString();

            if (groupName.isEmpty()) {
                Toast.makeText(getContext(), "Grup ismi gerekli", Toast.LENGTH_SHORT).show();
                return;
            }
            if (groupDescription.isEmpty()) {
                Toast.makeText(getContext(), "Grup açıklaması gerekli", Toast.LENGTH_SHORT).show();
                return;
            }
            if (filePath != null) {

                StorageReference storageReference = mStorage.getReference().child("resmiler/" + UUID.randomUUID().toString());

                storageReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Toast.makeText(getContext(), "Resim yüklendi", Toast.LENGTH_SHORT).show();

                        CreateGroup(groupName, groupDescription, downloadUrl);
                    });
                });
                return;
            }else {
                CreateGroup(groupName, groupDescription, null);
            }
        });

        FetchGroups();
        return view;
    }

    private void CreateGroup(String name, String description, String image) {
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/" + userId + "/" + "groups").add(new HashMap<String, Object>(){{
            put("name", name);
            put("description", description);
            put("image", image);
            put("numbers", new ArrayList<String>());
        }}).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Grpu başarıyla oluşturuldu", Toast.LENGTH_SHORT).show();

            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                GroupModel groupModel = new GroupModel( name, description, image, (List<String>)documentSnapshot.get("numbers"), documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
                groupsRecyclerView.getAdapter().notifyItemInserted(groupModelArrayList.size() - 1);
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Grup oluşturulamadı", Toast.LENGTH_SHORT).show();
        });
    }

    private void FetchGroups(){
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/" + userId + "/" + "groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelArrayList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getString("image"), (List<String>)documentSnapshot.get("numbers"),documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
            }

            groupsRecyclerView.setAdapter(new GroupAdapter(groupModelArrayList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            groupsRecyclerView.setLayoutManager(linearLayoutManager);

        });
    }
}