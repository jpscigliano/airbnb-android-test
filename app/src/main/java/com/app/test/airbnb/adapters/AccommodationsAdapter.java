package com.app.test.airbnb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.test.airbnb.R;
import com.app.test.airbnb.models.Accommodation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juan on 10/03/2017.
 */
public class AccommodationsAdapter extends RecyclerView.Adapter<AccommodationsAdapter.AccommodationsViewHolder> {


    public interface ItemSelecListener {
        void onItemSelected(Accommodation mAccommodation);
    }

    private ArrayList<Accommodation> mAccommodations;
    private Context mContext;
    private final ItemSelecListener listener;

    public AccommodationsAdapter(ArrayList<Accommodation> mAccommodations, Context mContext, ItemSelecListener listener) {
        this.mAccommodations = mAccommodations;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public AccommodationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AccommodationsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accommodation_list, parent, false));
    }


    @Override
    public void onBindViewHolder(AccommodationsViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(mAccommodations.get(position).getImage())
                .into(holder.image);
        holder.title.setText(mAccommodations.get(position).getName());
        holder.subTitle.setText(mAccommodations.get(position).getPropertyType());
        holder.price.setText("" + mAccommodations.get(position).getPrice() + " " + mAccommodations.get(position).getCurrency());
    }


    @Override
    public int getItemCount() {
        return mAccommodations.size();
    }


    public class AccommodationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView image;
        @BindView(R.id.textViewTitle)
        TextView title;
        @BindView(R.id.textViewSubTitle)
        TextView subTitle;
        @BindView(R.id.textViewPrice)
        TextView price;


        public AccommodationsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           listener.onItemSelected(mAccommodations.get(getAdapterPosition()));
        }
    }
}
