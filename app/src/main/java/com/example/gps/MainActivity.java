package com.example.gps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.gps.Data.Location_data;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity  implements  View.OnClickListener{

    // Locaation情報のリスト
    ArrayList<String> arrayList = new ArrayList<>();

    // RecyclerView
    private RecyclerView recyclerView;

    private static int datacnt = 0;

    // 緯度を表示するTextViewフィールド。
    private TextView _tvLocation;
    // マップ表示ボタン
    private FloatingActionButton  map;
    // 緯度経度
    private FloatingActionButton location;
    // 画像
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView)findViewById(R.id.img);
        GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(img);
        Glide.with(this).load(R.raw.background).into(target);

        // recyclerView
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rLayoutManager);
        //GridLayoutManager rLayoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL,false);
        //rLayoutManager.setSpanSizeLookup(new Custom_Spansize(4, 2 ,1));

        recyclerView.setLayoutManager(rLayoutManager);
        (new ItemTouchHelper(callback)).attachToRecyclerView(recyclerView);

        // viewの設定
        map = (FloatingActionButton ) findViewById(R.id.map_btn);
        location = (FloatingActionButton )findViewById(R.id.location_btn);
        location.setOnClickListener(this);
        map.setOnClickListener(this);

        // インスタンス生成
        android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(LOCATION_SERVICE);
        com.example.gps.LocationManager lmg = new com.example.gps.LocationManager();

        // 許可するかしないか
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1000);
            return;
        }

        // Location 情報の更新
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lmg);
    }
    /**
     * スワイプイベントなどの処理
     * */
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            // 横にスワイプされたら要素を消す
            int swipedPosition = viewHolder.getAdapterPosition();
            MyAdapter adapter = (MyAdapter) recyclerView.getAdapter();
            adapter.remove(swipedPosition);
            if(adapter.getItemCount() == 0){
                img.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * クリック処理
     * */
    public void onClick(View view){
        if(view.getId() == R.id.map_btn) {
            img.setVisibility(View.INVISIBLE);
            Log.d("debug","button1, Perform action on click");

            final EditText editText = new EditText(this);
            editText.setHint("例: コンビニ ATM");
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("MAP検索")
                    .setMessage("空白でOKボタンを押すと現在地が表示されます！")
                    .setView(editText)
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //キャンセルボタンを押したときの処理
                            img.setVisibility(View.VISIBLE);
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //ＯＫボタンを押したときの処理
                            // お好きな処理をどうぞ
                            onMapSearchButtonClick(editText.getText().toString());
                        }
                    })
                    .show();
        }
         else if(view.getId() == R.id.location_btn) {
            Log.d("debug","button2, Perform action on click");

            // シングルトンのデータクラス生成
            Location_data data = Location_data.getInstance();

            try{
                if (data._latitude !=0.0) {
                    img.setVisibility(View.INVISIBLE);
                    Log.d("debug","try");

                    String str_location = getNowDate() +"" +
                            "\n-------------nowlocation-------------\n" +
                            "緯度" + Double.toString(data._latitude) + "\n" +
                            "軽度" + Double.toString(data._longitude) + "\n" +
                            "精度" + Double.toString(data._accuracy) + "\n" +
                            "高度" + Double.toString(data._altitude) + "\n" +
                            "速度" + Double.toString(data._speed) + "\n";
                    //"時刻" + data.sdf.format(date) + "\n";

                    arrayList.add(str_location);
                    datacnt++;
                }
                else{
                    Toast.makeText(this , "まだGPSの値が取得できていません", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.d("debug",e.toString());
            }

            // specify an adapter (see also next example)
            RecyclerView.Adapter rAdapter = new MyAdapter(arrayList);
            recyclerView.setAdapter(rAdapter);
        }
    }

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.<br>
     */
    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    // リザルト処理
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(LOCATION_SERVICE);
            com.example.gps.LocationManager locationListener = new com.example.gps.LocationManager();
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    /**
     * 地図検索ボタンがタップされたときの処理メソッド。
     */
    public void onMapSearchButtonClick(String searchWord) {
        try {
            searchWord = URLEncoder.encode(searchWord, "UTF-8");
            String uriStr = "geo:0,0?q=" + searchWord;
            Uri uri = Uri.parse(uriStr);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch(UnsupportedEncodingException ex) {
            Log.e("IntentStartActivity", "検索キーワード変換失敗", ex);
        }
    }

    /**
     * 現在地の地図表示ボタンがタップされたときの処理メソッド。
     * @param view 画面部品
     */
    public void onMapShowCurrentButtonClick(View view) {
        Location_data data = Location_data.getInstance();

        String uriStr = "geo:" + data._latitude + "," + data._longitude;
        Uri uri = Uri.parse(uriStr);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        img.setVisibility(View.VISIBLE);
        Log.d("LifecycleLesson", "Activity::onResume");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        img.setVisibility(View.VISIBLE);
        Log.v("LifeCycle", "onRestart");
    }
}
