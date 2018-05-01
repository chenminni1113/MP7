package edu.illinois.cs.cs125.mp7;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter to handle the results activity.
 */
public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    /** Key to put the item name into a new intent. */
    public static final String KEY_NAME = "name";
    /** Key to put a URL to a thumbnail of the item into a new intent. */
    public static final String KEY_IMGURL = "img url";

    /** Dataset for the adapter. */
    private List<ResultsList> resultsLists;
    private Context context;

    /**
     * Inner class for the ViewHolder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public ImageView itemPhoto;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize view objects
            itemName = itemView.findViewById(R.id.itemName);
            itemPhoto = itemView.findViewById(R.id.itemPhoto);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public ResultsAdapter(List<ResultsList> resultsLists, Context context) {
        this.resultsLists = resultsLists;
        this.context = context;
    }

    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.results_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ResultsList resultsList = resultsLists.get(position);
        holder.itemName.setText(resultsList.getName());
        Picasso.get().load(resultsList.getImgUrl()).into(holder.itemPhoto);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ResultsList resultsList1 = resultsLists.get(position);
                Intent skipIntent = new Intent(v.getContext(), ResultsActivity.class);
                skipIntent.putExtra(KEY_NAME, resultsList1.getName());
                skipIntent.putExtra(KEY_IMGURL, resultsList1.getImgUrl());
                v.getContext().startActivity(skipIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultsLists.size();
    }
}
