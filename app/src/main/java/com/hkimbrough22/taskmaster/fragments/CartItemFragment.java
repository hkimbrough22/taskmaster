package com.hkimbrough22.taskmaster.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hkimbrough22.taskmaster.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartItemFragment extends Fragment {


    public CartItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CartItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartItemFragment newInstance() {
        CartItemFragment fragment = new CartItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_item, container, false);
    }
}