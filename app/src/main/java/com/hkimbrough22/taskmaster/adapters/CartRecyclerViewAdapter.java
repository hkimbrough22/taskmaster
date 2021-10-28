package com.hkimbrough22.taskmaster.adapters;

import static com.hkimbrough22.taskmaster.activities.MainActivity.TASK_EXTRA_STRING;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hkimbrough22.taskmaster.R;
import com.hkimbrough22.taskmaster.activities.TaskDetailsActivity;
import com.hkimbrough22.taskmaster.models.CartItem;

import java.util.List;

//Step 4.
public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartItemViewHolder> {

    AppCompatActivity associatedActivity;
    List<CartItem> cartItemList;


    public CartRecyclerViewAdapter(AppCompatActivity associatedActivity, List<CartItem> cartItemList){
        this.associatedActivity = associatedActivity;
        this.cartItemList = cartItemList;
    }

    //Step 7 here
    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_cart_item, parent,  false);
        CartItemViewHolder cartItemViewHolder = new CartItemViewHolder(fragment);

       return cartItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
//        Change the data when the viewholder/fragments cycle
        CartItem cartItem = cartItemList.get(position);
        View cartItemFragment = holder.itemView;
        TextView cartItemFragmentTextView = cartItemFragment.findViewById(R.id.cartItemFragmentTextView);
        cartItemFragmentTextView.setText(cartItem.toString());

        cartItemFragment.setOnClickListener(view -> {
            Intent taskDetailsIntent = new Intent(associatedActivity, TaskDetailsActivity.class);
            taskDetailsIntent.putExtra(TASK_EXTRA_STRING, cartItem.getItemName());
            associatedActivity.startActivity(taskDetailsIntent);
        });
    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    //Step 8 here
    //Step 11 here too
    public static class CartItemViewHolder extends RecyclerView.ViewHolder{
        public CartItem cartItem;

        //TODO: add some data variables later
        public CartItemViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
