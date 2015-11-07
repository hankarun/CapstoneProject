package com.hankarun.patienthistory.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.activity.AdminSecondActivity;

public class AdminActivityFragment extends Fragment {

    public AdminActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);

        ListView menuList = (ListView) rootView.findViewById(R.id.menuListView);

        String list[] = new String[]{"Hastalar","Soru Grupları","Sorular","Seçenekler"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,list);
        menuList.setAdapter(adapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View frameLayout = getActivity().findViewById(R.id.container_body);
                if (frameLayout == null) {
                    setActivity(position);
                } else {
                    setDrawer(position);
                }
            }
        });

        return rootView;
    }

    public void setActivity(int position){
        Intent myIntent = new Intent(getActivity(), AdminSecondActivity.class);
        myIntent.putExtra("fragment", position);
        getActivity().startActivity(myIntent);
    }

    public void setDrawer(int position){
        Fragment fragment = null;
        Class fragmentClass = null;
        String fragmentName = null;
        //Check other fragment if active

        switch (position) {
            case 0:
                //Open patients activity or fragment
                fragmentClass = PatientListFragment.class;
                fragmentName = "patient";
                break;
            case 1:
                //Open groups activity or fragment
                fragmentClass = GroupListFragment.class;
                fragmentName = "groups";
                break;
            case 2:
                //Open questions activity or fragment
                break;
            case 3:
                //Open settings activity or fragment
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.d("eroor", e.toString());
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment, fragmentName).commit();
    }
}
