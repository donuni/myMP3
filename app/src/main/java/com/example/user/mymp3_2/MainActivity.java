package com.example.user.mymp3_2;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static Button btnPlay, btnStop, btnMyList;
    TextView tvPlay, tvTime;
    SeekBar pbMP3;

    MediaPlayer mediaPlayer = new MediaPlayer();
    ArrayList<String> list = new ArrayList<String>();
    static String selectedMP3;
    static final String MP3_PATH =  "/sdcard/";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    //
    static MyDBHelper myDBHelper;
    static SQLiteDatabase sqlDB;
    ArrayList<MainData> mainDataList;
    ArrayList<MainData> myDataList;
    static MainAdapter mainAdapter;
    static MyListAdapter myListAdapter;
    RecyclerView recyclerView;

    //

    View myListView;

    //

    static boolean checkmainList=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("모든 음악 리스트");
        loadAudioList();
        recyclerView = findViewById(R.id.recyclerView);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        btnMyList = findViewById(R.id.btnMyList);
        tvPlay = findViewById(R.id.tvPlay);
        tvTime = findViewById(R.id.tvTime);
        pbMP3 = findViewById(R.id.pbMP3);
        Log.d("메세지","myDBHelper = new MyDBHelper(this);");
        myDBHelper = new MyDBHelper(this);
        mainDataList = new ArrayList<>();
        myDataList = new ArrayList<>();

        Log.d("메세지","mainDataList = new ArrayList<>();");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        Log.d("메세지","ActivityCompat.requestPermissions");


        //데이터 베이스 셋팅 함수
        databaseSetting();


        mainDataListsetting();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("메세지","recyclerView.setLayoutManager(new LinearLayoutManager(this));");
        mainAdapter = new MainAdapter(this,R.layout.list_item,mainDataList);
        Log.d("메세지","mainAdapter = new MainAdapter(this,R.layout.list_item,mainDataList);");
        recyclerView.setAdapter(mainAdapter);
        Log.d("메세지","recyclerView.setAdapter(mainAdapter);");

        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnMyList.setOnClickListener(this);


//        btnPlay.setClickable(true);
//        btnStop.setClickable(false);
        btnPlay.setEnabled(true);
        btnStop.setEnabled(false);
        pbMP3.setProgress(0);
        tvTime.setText("진행시간 : 0");
//        selectedMP3 = list.get(0);


        //My mp3다이얼로그





    }//end of onCreate

    private void databaseSetting() {
        Log.d("메세지","databaseSetting 시작");
        //-----------------------
        File[] files = new File(MP3_PATH).listFiles();
        Log.d("메세지","포문 들어가기 전");
        for (File file : files) {
            sqlDB=myDBHelper.getWritableDatabase();
            String str = "없음";
            String fileName = file.getName();
            String extendName = fileName.substring(fileName.length() - 3);
            if (extendName.equals("mp3")) {
                list.add(fileName);
//                    mainDataList.add(new MainData(fileName,null,null,null));
                Log.d("메세지",fileName);
//                sqlDB.execSQL("INSERT INTO mp3TBL VALUES ( '"+ fileName + "' ,'"+ str + "','"+ str + "', "+ 0 + ");");
                sqlDB.execSQL("INSERT OR REPLACE INTO mp3TBL(fName) VALUES('"+fileName+"');");
                Log.d("메세지","77줄");
//                sqlDB.execSQL("INSERT INTO mp3TBL VALUES ( '"
//                        + edtName.getText().toString() + "' , "
//                        + edtNumber.getText().toString() + ");");
                sqlDB.close();
            }


        }//end of for-----------------------------------
    }


    public void mainDataListsetting() {
        sqlDB=myDBHelper.getWritableDatabase();
        Cursor cursor;
//        cursor = sqlDB.rawQuery("SELECT * FROM mp3TBL ORDER BY gNumber ASC;", null);
        cursor = sqlDB.rawQuery("SELECT * FROM mp3TBL ORDER BY fStar DESC;", null);

//                String strNames = "그룹이름" + "\r\n" + "--------" + "\r\n";
//                String strNumbers = "인원" + "\r\n" + "--------" + "\r\n";
        mainDataList.removeAll(mainDataList);
        while (cursor.moveToNext()) {
//                    strNames += cursor.getString(0) + "\r\n";
//                    strNumbers += cursor.getString(1) + "\r\n";
            mainDataList.add(new MainData(cursor.getString(0),null));
        }
//        mainAdapter.notifyDataSetChanged();
        cursor.close();
        sqlDB.close();
    }
    public void myDataListsetting() {
        sqlDB=myDBHelper.getWritableDatabase();
        Cursor cursor;
//        cursor = sqlDB.rawQuery("SELECT * FROM mp3TBL ORDER BY gNumber ASC;", null);
        cursor = sqlDB.rawQuery("SELECT * FROM mp3TBL WHERE fStar IS NOT NULL ORDER BY fStar DESC;", null);

//                String strNames = "그룹이름" + "\r\n" + "--------" + "\r\n";
//                String strNumbers = "인원" + "\r\n" + "--------" + "\r\n";
        myDataList.removeAll(myDataList);
        while (cursor.moveToNext()) {
//                    strNames += cursor.getString(0) + "\r\n";
//                    strNumbers += cursor.getString(1) + "\r\n";
            myDataList.add(new MainData(cursor.getString(0),cursor.getString(1)));
            Log.d("메세지",cursor.getString(1)+"점의 "+cursor.getString(0));
        }
//        mainAdapter.notifyDataSetChanged();
        cursor.close();
        sqlDB.close();
    }

    public static void addMyListData() {
        selectedMP3=mainAdapter.selectFileNameString;
        sqlDB=myDBHelper.getWritableDatabase();
        Log.d("메세지","add my liast data 1"+mainAdapter.star+"   "+selectedMP3);
        sqlDB.execSQL("UPDATE mp3TBL SET fStar="+mainAdapter.star+" WHERE fName='"+selectedMP3+"'");
        Log.d("메세지","add my liast data 2"+selectedMP3);
    }

    public void deleteMyListData() {
        if(!checkmainList){
            sqlDB=myDBHelper.getWritableDatabase();
            Log.d("메세지","deleteMyListData()1   "+selectedMP3);
            sqlDB.execSQL("UPDATE mp3TBL SET fStar=NULL WHERE fName='"+selectedMP3+"'");
            Log.d("메세지","deleteMyListData()2   "+selectedMP3);

            checkmainList=true;
        }else{
            Toast.makeText(this,"선택하셔야 삭제합니다.",Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnPlay:
                try {
                    if(checkmainList){
                        selectedMP3=mainAdapter.selectFileNameString;
                    }

                    mediaPlayer.setDataSource(MP3_PATH + selectedMP3);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

//                    btnPlay.setClickable(false);
//                    btnStop.setClickable(true);
                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(true);
                    tvPlay.setText("실행중인 음악명:" + selectedMP3);

                    Thread thread = new Thread() {


                        @Override
                        public void run() {
                            if (mediaPlayer == null) {
                                return;
                            }
                            //1.노래에 총 걸리는 시간                             //재생시간
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    tvTime.setText(" 재생시간 " + mediaPlayer.getDuration());
                                    Log.d("메세지"," 재생시간 " +simpleDateFormat.format(mediaPlayer.getDuration())+"");
                                    pbMP3.setMax(mediaPlayer.getDuration());
                                }
                            });


                            while (mediaPlayer.isPlaying()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        //자기가 진행되는 시간
                                        pbMP3.setProgress(mediaPlayer.getCurrentPosition());
                                        tvTime.setText("진행시간 : " +simpleDateFormat.format(mediaPlayer.getDuration())+" / "+ simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                                    }
                                });//end of runOnUiThread() 스레드안에서 화면위젯변경을 할 수 있는 스레드

                                SystemClock.sleep(200);
                            }//end of while
                        }
                    };

                    thread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkmainList=true;
                break;
            case R.id.btnStop:
                mediaPlayer.stop();
                mediaPlayer.reset();
//                btnPlay.setClickable(true);
//                btnStop.setClickable(false);
                btnPlay.setEnabled(true);
                btnStop.setEnabled(false);
                tvPlay.setText("음악");
                pbMP3.setProgress(0);
                tvTime.setText("진행시간 : 0");

                break;

                case R.id.btnMyList:
                    myDataListsetting();
                    //dialog listview
//                    selectFileNameString = arrayList.get(i).getFileName();
//                    starView=View.inflate(context, R.layout.addmylist, null);
//                    final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                    dialog.setTitle("별점 주기");
//                    dialog.setView(starView);
//                    final RatingBar ratingBar =starView.findViewById(R.id.ratingBar);
//                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                        @Override
//                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                            star=v;
//                        }
//                    });
//                    dialog.setPositiveButton("입력", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(context,star+"점으로 My List 등록", Toast.LENGTH_SHORT).show();
//                            MainActivity.addMyListData();
//                        }
//                    });
//                    dialog.show();
                    myListView=View.inflate(this, R.layout.mylist, null);
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("내 재생 목록");
                    dialog.setView(myListView);
                    //리스트뷰
                    ListView listView =myListView.findViewById(R.id.listView);
                    myListAdapter= new MyListAdapter(this,R.layout.mylist_item,myDataList);
                    listView.setAdapter(myListAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedMP3=myDataList.get(i).fileName;
                        }
                    });
                    //리스트뷰 끝
                    dialog.setPositiveButton("재생", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            btnStop.callOnClick();
                            checkmainList=false;
                            btnPlay.callOnClick();
                        }
                    });
                    dialog.setNegativeButton("my list 삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            checkmainList=false;
                            deleteMyListData();
                        }
                    });
                    dialog.show();


                break;
        }
    }

    public class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context) {

            super(context, "mp3DB6", null, 1);


            Log.d("메세지","마이 디비 헬퍼 생성자");
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL("CREATE TABLE mp3TBL (fName CHAR(300) PRIMARY KEY, fStar REAL);");
//            sqLiteDatabase.execSQL("CREATE TABLE mp3TBL (fName CHAR(300) PRIMARY KEY, fSinger CHAR(20),fGenre CHAR(20), fStar INTEGER);");
            Log.d("메세지","마이 디비 헬퍼 온크리에이트 테이블 생성");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS mp3TBL");
            onCreate(sqLiteDatabase);
        }
        public void delete(String name){
            sqlDB=myDBHelper.getWritableDatabase();

            sqlDB.execSQL("DELETE FROM mp3TBL WHERE fName = '"
                            + name + "';");

            sqlDB.close();

            Toast.makeText(getApplicationContext(), name+" 삭제됨",
                        Toast.LENGTH_SHORT).show();
            mainAdapter.notifyDataSetChanged();
        }
    }

    private void loadAudioList() {
        ContentResolver contentResolver = getContentResolver();
        // 음악 앱의 데이터베이스에 접근해서 mp3 정보들을 가져온다.

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
        cursor.moveToFirst();
        System.out.println("음악파일 개수 = " + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            do {
                long track_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                Integer mDuration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String datapath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Log.d("메세지","mId = " + track_id + " albumId = " + albumId + " title : " + title + " album : "
                        + album + " artist: " + artist + " 총시간 : " + mDuration + " data : " + datapath);
                // Save to audioList
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}
