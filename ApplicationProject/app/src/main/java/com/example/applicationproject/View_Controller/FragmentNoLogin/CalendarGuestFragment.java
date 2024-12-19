package com.example.applicationproject.View_Controller.FragmentNoLogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.applicationproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarGuestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarGuestFragment extends Fragment {

    private boolean isCalendarVisible = true;
    private LinearLayout layoutCalendar;
    private MenuItem visibleItem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarGuestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarGuestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarGuestFragment newInstance(String param1, String param2) {
        CalendarGuestFragment fragment = new CalendarGuestFragment();
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
        View view = inflater.inflate(R.layout.fragment_calendar_guest, container, false);

        layoutCalendar = view.findViewById(R.id.layoutCalendarGuest);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_layout, menu);
        visibleItem = menu.findItem(R.id.visible);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sapXep) {
            Toast.makeText(requireContext(), "Sắp xếp", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.about) {
            Toast.makeText(requireContext(), "About", Toast.LENGTH_SHORT).show();
            return true;
        }else if(item.getItemId() == R.id.visible) {

            //Xử lý ẩn hiện lịch
            if (isCalendarVisible) {
                layoutCalendar.setVisibility(View.GONE);
                visibleItem.setIcon(R.drawable.up_arrow);
            } else {
                layoutCalendar.setVisibility(View.VISIBLE);
                visibleItem.setIcon(R.drawable.down_arrow);
            }
            isCalendarVisible = !isCalendarVisible;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}