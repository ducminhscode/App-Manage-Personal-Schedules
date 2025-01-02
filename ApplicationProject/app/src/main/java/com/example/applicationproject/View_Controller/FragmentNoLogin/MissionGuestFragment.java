package com.example.applicationproject.View_Controller.FragmentNoLogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.UserLogin.Login;
import com.example.applicationproject.View_Controller.Utils.CheckingNetWork;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MissionGuestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MissionGuestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MissionGuestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MissionGuestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MissionGuestFragment newInstance(String param1, String param2) {
        MissionGuestFragment fragment = new MissionGuestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mission_guest, container, false);

        setHasOptionsMenu(true);

        loadData(view);

        return view;

    }

    private void loadData(View view) {
        if (CheckingNetWork.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "Có kết nối", Toast.LENGTH_SHORT).show();
            FloatingActionButton fabGuest = view.findViewById(R.id.fabGuest);
            fabGuest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(requireContext(), Login.class));
                }
            });
        } else {
            Toast.makeText(requireContext(), "Không có kết nối", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mission_layout, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Tìm kiếm...");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.theLoai) {
            Toast.makeText(requireContext(), "Thể Loại", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.thongke) {
            Toast.makeText(requireContext(), "About", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}