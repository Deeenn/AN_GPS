package com.example.gps;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gps.Data.Location_data;

import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<String> arrayList = new ArrayList<>();

    // ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        TextView mlocation;

        ViewHolder(View v) {
            super(v);
            mlocation = (TextView)v.findViewById(R.id.text_laocation);
        }
    }

    // コンストラクタ
    MyAdapter(ArrayList myDataset) {
        arrayList = myDataset;
    }
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_data, parent, false);

        return new ViewHolder(view);
    }

    // holderにセットする
    // ボタンなどの処理があればここでする
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Location_data data = Location_data.getInstance();
        // 現在の時刻を取得
        Date date = new Date();

        holder.mlocation.setText(arrayList.get(position));
    }

    // listsize
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /**
     * 要素を削除する
     * @param position 削除する位置
     */
    public void remove(int position) {

        arrayList.remove(position);
        notifyItemRemoved(position);
    }
}
