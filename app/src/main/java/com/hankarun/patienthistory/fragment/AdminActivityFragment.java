package com.hankarun.patienthistory.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hankarun.patienthistory.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AdminActivityFragment extends Fragment {

    public AdminActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);

        ListView menuList = (ListView) rootView.findViewById(R.id.menuListView);

        String list[] = new String[]{"Hastalar","Soru Grupları","Sorular","Seçenekler"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,list);
        menuList.setAdapter(adapter);

        return rootView;
    }
}
