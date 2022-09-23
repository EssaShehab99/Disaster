package com.example.form;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.form.adapter.CustomAdapter;
import com.example.form.model.DisasterModel;
import com.example.form.model.HelpCenterModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;

import java.util.ArrayList;

public class DisasterFragment extends Fragment {
    CustomAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView disasterRecyclerView;
    static  ArrayList<DisasterModel> disasterList=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disaster, container, false);
        disasterRecyclerView = view.findViewById(R.id.disaster_list);

        adapter =new CustomAdapter(getActivity(),disasterList);
        adapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(getActivity(), DisasterMap.class);
            intent.putExtra("position", position);
            startActivity(intent);
        });
        initial();
        return view;
    }
    void insertToLocal() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        disasterRecyclerView.setLayoutManager(linearLayoutManager);
        disasterRecyclerView.setAdapter(adapter);

    }

    public void initial() {
        try {
            db.collection("disasters").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    disasterList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        try {

                            disasterList.add(DisasterModel.fromMap(document.getData(), document.getId()));
                            insertToLocal();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                            helpCenterModels.add(helpCenterModel);
                    }
                }
            });
        } catch (Exception ignored) {
        }

//        addMarkersToMap(helpCenterModels);
    }
}