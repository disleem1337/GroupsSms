package com.example.groupssms.ui.addtogroup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupssms.ContactModel;
import com.example.groupssms.GroupModel;
import com.example.groupssms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddToGroupFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    RecyclerView groupsRecyclerView, contactsRecyclerView;
    TextView selectedGroupTextView;

    GroupModel selectedGroup;

    ArrayList<GroupModel> groupModelList;
    ArrayList<ContactModel> contactModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_group, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        groupsRecyclerView = view.findViewById(R.id.addtogroup_groupsRecyclerView);
        contactsRecyclerView = view.findViewById(R.id.addtogroup_contactsRecyclerView);
        selectedGroupTextView = view.findViewById(R.id.addtogroup_selectedGroupTextView);

        groupModelList = new ArrayList<>();
        contactModelList = new ArrayList<>();


        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
            if (isGrant) {
                FetchContacts();
            }else{
                Toast.makeText(getContext(), "Uygulamanın düzgün çalışması için rehber okuma izni gerekli", Toast.LENGTH_SHORT).show();
            }
        }
        );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            launcher.launch(Manifest.permission.READ_CONTACTS);
        }else{
            FetchContacts();
        }

        FetchGroups();
        return view;
    }

    private void FetchGroups(){
        String uid = mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/" + uid + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getString("image"), (List<String>)documentSnapshot.get("numbers"),documentSnapshot.getId());
                groupModelList.add(groupModel);
            }

            groupsRecyclerView.setAdapter(new GroupAdapter(groupModelList, position -> {
                selectedGroup = groupModelList.get(position);
                selectedGroupTextView.setText("Seçili grup: " + selectedGroup.getName());
            }));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            groupsRecyclerView.setLayoutManager(linearLayoutManager);
        });
    }

    private void FetchContacts(){
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        contactModelList.clear();
        while (cursor.moveToNext()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            ContactModel contactModel = new ContactModel(name, number, image);
            contactModelList.add(contactModel);
        }

        contactsRecyclerView.setAdapter(new ContactAdapter(contactModelList, position -> {
            ContactModel contactModel = contactModelList.get(position);

            if(selectedGroup != null){
                new AlertDialog.Builder(getContext())
                        .setTitle("Kişi Ekle")
                        .setMessage(contactModel.getName() + " kişisini " + selectedGroup.getName() + " grubuna eklemek istiyor musunuz?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            mStore.collection("/userdata/" + mAuth.getCurrentUser().getUid() + "/groups").document(selectedGroup.getUid()).update( new HashMap<String, Object>(){{
                                put("numbers", FieldValue.arrayUnion(contactModel.getNumber()));
                            }}).addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Kişi eklendi", Toast.LENGTH_SHORT).show();
                            });
                        })
                        .setNegativeButton("Hayır", null)
                        .show();
            }
        }));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        contactsRecyclerView.setLayoutManager(linearLayoutManager);

    }
}