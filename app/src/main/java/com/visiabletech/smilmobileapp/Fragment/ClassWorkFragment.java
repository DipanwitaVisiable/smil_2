package com.visiabletech.smilmobileapp.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visiabletech.smilmobileapp.Adapter.ClassWorkAdapter;
import com.visiabletech.smilmobileapp.Pogo.ClassWorkModel;
import com.visiabletech.smilmobileapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassWorkFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<ClassWorkModel> arrayList = new ArrayList<>();
    String[] text={"English Grammer","Hindi Grammer","Bangali Grammer"};

    public ClassWorkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_class_work, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        for (int i=0; i<text.length; i++)
        {
            ClassWorkModel classWorkModel = new ClassWorkModel(text[i]);
            arrayList.add(classWorkModel);
        }

        recyclerView.setAdapter(new ClassWorkAdapter(getContext(),arrayList));



        return view;
    }

}
