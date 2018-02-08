package com.hadarfem.fixit.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadarfem.fixit.R;
import com.hadarfem.fixit.cache.IPictureCache;
import com.hadarfem.fixit.dal.interfaces.IUpdater;
import com.hadarfem.fixit.dal.models.Bid;
import com.hadarfem.fixit.interfaces.IBidsBoardActivity;
import com.hadarfem.fixit.interfaces.IBidFilterable;
import com.hadarfem.fixit.tasks.DownloadImageTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tzach & Nadav on 1/24/2018.
 */

public class ArrayBidAdapter extends ArrayAdapter<Bid> implements IBidFilterable {
    private List<Bid> bids;
    private List<Bid> filteredBids;
    private Context context;
    private Filter filter;
    private String lastFilterLiteral;
    private View progressBar;
    private View listView;
    private IPictureCache pictureCache;

    public ArrayBidAdapter(Activity activity, View progressBar, View listView, IPictureCache pictureCache) {
        this(activity, new ArrayList<Bid>(), progressBar, listView, pictureCache);
    }

    private ArrayBidAdapter(Activity activity, List<Bid> bids, View progressBar,
                            View listView, IPictureCache pictureCache) {
        super(activity, R.layout.bid_row, bids);

        this.filteredBids = bids;
        this.bids = new ArrayList<>(filteredBids);

        this.context = activity;
        this.filter = new BidFilter();
        this.lastFilterLiteral = "";
        this.progressBar = progressBar;
        this.listView = listView;
        this.pictureCache = pictureCache;

        if (!(activity instanceof IBidsBoardActivity)) {
            throw new RuntimeException("Activity must implement IBidsBoardActivity");
        }

        ((IBidsBoardActivity) activity).registerForBids(new IUpdater<Bid>() {
            @Override
            public void update(List<Bid> entities) {
                updateBids(entities);
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Bid bid = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bid_row, parent, false);
        }

        TextView userNameView = (TextView) convertView.findViewById(R.id.bidderUserNameView);
        TextView bidPriceView = (TextView) convertView.findViewById(R.id.bidPriceView);
        ImageView pictureView = (ImageView) convertView.findViewById(R.id.userPicture);
        TextView dateView = (TextView) convertView.findViewById(R.id.date);
        TextView problemTitleView = (TextView) convertView.findViewById(R.id.problemTitleForBid);

        if (userNameView != null) {
            userNameView.setText(bid.getBidderUserName());
        }

        if (bidPriceView != null) {
            bidPriceView.setText(Integer.toString(bid.getPrice()) + "₪");
        }

        if (problemTitleView != null) {
            problemTitleView.setText(bid.getTitle());
        }

        if (pictureView != null && bid.getPictureUrl() != null && !bid.getPictureUrl().isEmpty()) {
            pictureView.setImageResource(R.mipmap.ic_unknown_user);

            new DownloadImageTask(pictureView, pictureCache, (int) context.getResources().getDimension(R.dimen.user_picture_width), (int) context.getResources().getDimension(R.dimen.user_picture_height)).
                    execute(bid.getPictureUrl());
        }

        if (dateView != null && bid.getDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
            String dateString = dateFormat.format(bid.getDate());
            dateView.setText(dateString);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public
    @NonNull
    Filter getFilter() {
        return filter;
    }

    @Override
    public void setFilter(String filter) {
        getFilter().filter(filter);

        lastFilterLiteral = filter;
    }

    private void updateBids(List<Bid> bids) {
        if (this.listView.getVisibility() == View.INVISIBLE) {
            this.listView.setVisibility(View.VISIBLE);
        }

        if (this.progressBar.getVisibility() == View.VISIBLE) {
            this.listView.setVisibility(View.INVISIBLE);
        }

        this.bids.clear();

        for (Bid bid : bids) {
            this.bids.add(bid);
            this.filteredBids.add(bid);
        }

        notifyDataSetChanged();

        getFilter().filter(lastFilterLiteral);
    }

    private class BidFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Bid> filteredList = new ArrayList<Bid>();
            String filter = constraint.toString().toLowerCase();

            if (constraint != null && bids != null) {
                for (Bid bid : bids) {
                    if (bid.getTitle() != null && bid.getTitle().toLowerCase().contains(filter)) {
                        filteredList.add(bid);
                    }
                }

                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            filteredBids.clear();

            for (Bid bid : (List<Bid>) results.values) {
                filteredBids.add(bid);
            }

            notifyDataSetChanged();
        }
    }
}
