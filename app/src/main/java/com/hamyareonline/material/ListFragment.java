

package com.hamyareonline.material;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    DBAdapter db;
    SimpleStringRecyclerViewAdapter listAdapter ;
    RecyclerView rv;
    List<sho_items> items ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(R.layout.fragment_list, container, false);
        items = new ArrayList<>();
        db = new DBAdapter(getActivity());
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        db.open();

        switch (getArguments().getString("FRG")){
            case "TITLES":
                items = db.getAllItms();
                break;
        }
        db.close();
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        listAdapter = new SimpleStringRecyclerViewAdapter(getActivity(), items);
        recyclerView.setAdapter(listAdapter);
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<sho_items> mValues;
        Context CONtext;
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public int mBoundID;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position).getTitle();
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<sho_items> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            CONtext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundID = mValues.get(position).getId();
            holder.mTextView.setText(mValues.get(position).getTitle());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_ID, holder.mBoundID);
                    context.startActivity(intent);
                }
            });

            int ImageResource = CONtext.getResources().getIdentifier(mValues.get(position).getImg_adrs(),
                    "drawable", CONtext.getPackageName());

            Glide.with(holder.mImageView.getContext())
                    .load(ImageResource)
                    .fitCenter()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
