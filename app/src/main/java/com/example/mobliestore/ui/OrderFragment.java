package com.example.mobliestore.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobliestore.R;
import com.example.mobliestore.model.Order;
import com.example.mobliestore.model.OrderAdapter;
import com.example.mobliestore.utils.DataManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    RecyclerView recyclerView;
    DataManager dataManager;
    OrderAdapter orderAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        View v= inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = v.findViewById(R.id.rcl_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataManager= new DataManager(getContext());
        loadOrder();
        return v;
    }
    private void loadOrder() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        int userId= sharedPreferences.getInt("user_id",-1);
        List<Order> ordersList= dataManager.getAllOrders(userId);
        orderAdapter= new OrderAdapter(getContext(),ordersList);
        recyclerView.setAdapter(orderAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrder();
    }
}