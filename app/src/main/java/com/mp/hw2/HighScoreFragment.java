package com.mp.hw2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreFragment extends Fragment {
    View inflate;
    private RecyclerView recyclerView;
    private List<HighModel> list;

    private static OnItemSelected onItemSelected;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (inflate == null) {
            inflate = inflater.inflate(R.layout.fragment_high_score, container, false);
        }

        recyclerView = inflate.findViewById(R.id.list);
        list = new SPManager(requireContext()).getL("coins");
        if(list == null) list = new ArrayList<>();

        Comparator<HighModel> comparator = new Comparator<HighModel>() {

            @Override
            public int compare(HighModel highModel, HighModel t1) {
                Integer h1 = highModel.getScore();
                Integer h2 = t1.getScore();
                return h1.compareTo(h2);
            }
        };

// And then sort it using collections.sort().
        Collections.sort(list, comparator);

        Collections.reverse(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new ListAdapter());
        return inflate;
    }

    public static void setOnSelectListener(OnItemSelected s){
        onItemSelected = s;
    }

    public interface OnItemSelected{
        void onSelected(double lat, double lon);
    }
    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyHolder>{

        @NonNull
        @Override
        public ListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(getLayoutInflater().inflate(R.layout.high_score_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ListAdapter.MyHolder holder, int position) {
            holder.score.setText(String.valueOf(list.get(position).getScore()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemSelected.onSelected(
                            list.get(position).getLatitude(),
                            list.get(position).getLongitude()
                            );
                }
            });
        }

        @Override
        public int getItemCount() {
            if(list.size() <= 9){
                return list.size();
            }
            return 10;

        }

        public class MyHolder extends RecyclerView.ViewHolder {
            public TextView score;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                score = itemView.findViewById(R.id.coin);
            }
        }
    }

}