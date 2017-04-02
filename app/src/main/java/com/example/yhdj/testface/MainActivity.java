package com.example.yhdj.testface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.aip.face.AipFace;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    protected static final int GET_HEAD_IMG = 1001;
    private static final int CROP_HEAD = 1002;
    private Bitmap bmp;
    private String path;
    private Uri imgUri;
    //设置APPID/AK/SK
    public static final String APP_ID = "9532759";
    public static final String API_KEY = "ItrqyhiVh5HCRLlKecrXB9SZ";
    public static final String SECRET_KEY = "0WtsdiSA3joN67YDALXH0YRaqMDv5lS3";
    private AipFace client;
    private FaceView iv_first_face;
    private FaceView iv_second_face;
    private Button btn_begin;
    private String path1;
    private String path2;
    private boolean isFirst = false;
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private Gson gson = new Gson();
    private Button btnCompare;
    private FaceRecognizeOneBean mFaceRecognizeOneBean = new FaceRecognizeOneBean();
    private ProgressDialog mProgressDialog;
    private JSONObject response;
    private Handler mHandler;
    private ArrayList<String> list = new ArrayList<>();
    private Button btn_verify;
    private FaceverifyUserBean mFaceverifyUserBean = new FaceverifyUserBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClient();
        initViews();
        initHander();
    }

    private void initHander() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mFaceRecognizeOneBean = (FaceRecognizeOneBean) msg.obj;
                getRec(mFaceRecognizeOneBean);
                return true;
            }
        });
    }

    private void initViews() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("识别中！！！");
        mProgressDialog.setTitle("人脸识别");
        mProgressDialog.setCancelable(false);
        iv_first_face = (FaceView) findViewById(R.id.iv_first_face);
        iv_second_face = (FaceView) findViewById(R.id.iv_second_face);
        btn_begin = (Button) findViewById(R.id.btn_begind);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        btnCompare = (Button) findViewById(R.id.btn_compare);
        btn_verify = (Button) findViewById(R.id.btn_verify);

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        verifyUser(client);
                    }
                }).start();

            }
        });

        iv_first_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst = true;
                getImg();


            }
        });

        iv_second_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImg();
            }
        });

        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        faceRecognize(client);
                    }
                }).start();


            }
        });

        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        faceRecognize2(client);
                    }
                }).start();
            }
        });
    }

    //人脸检测
    public void faceRecognize(final AipFace client) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.show();
            }
        });

        // 自定义参数定义
        final HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "1");
        options.put("face_fields", "expression,age,beauty,faceshape,gender,glasses,landmark,race,qualities");

        //age、beauty、expression、faceshape、gender、glasses、landmark、race、qualities

        // 参数为本地图片路径


        response = client.detect(path, options);
        mFaceRecognizeOneBean = gson.fromJson(response.toString(), FaceRecognizeOneBean.class);
        mProgressDialog.dismiss();
        Message message = Message.obtain();
        message.obj = mFaceRecognizeOneBean;
        mHandler.sendMessage(message);
        list.add(path1);
        client.addUser("bb", "bbb", "abc", list);
        Log.i("bbbb", "faceRecognize: " + response.toString());


    }

    //人脸比对
    public void faceRecognize2(AipFace client) {
        // 参数为本地图片路径
        ArrayList<String> pathArray = new ArrayList<String>();
        pathArray.add(path1);
        pathArray.add(path2);
        Log.e("aaaaaaaaaaaaa", "faceRecognize2: " + path1);
        JSONObject response = client.match(pathArray);
        final FaceRecognizeTwoBean a = gson.fromJson(response.toString(), FaceRecognizeTwoBean.class);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMyAdapter = new MyAdapter(a);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setAdapter(mMyAdapter);
            }
        });

        Log.i("dddddd", "faceRecognize2: " + response.toString());
    }

    private void getRec(FaceRecognizeOneBean mFaceRecognizeOneBean) {
        for (int i = 0; i < mFaceRecognizeOneBean.getResult_num(); i++) {
            Log.e("cccccccc", "getRec: " + "getLeft: " + mFaceRecognizeOneBean.getResult().get(i).getLocation().getLeft()
                    + "getTop: " + mFaceRecognizeOneBean.getResult().get(i).getLocation().getTop()
                    + "getWidth: " + mFaceRecognizeOneBean.getResult().get(i).getLocation().getWidth()
                    + "top: " + mFaceRecognizeOneBean.getResult().get(i).getLocation().getTop()
                    + "getHeight: " + mFaceRecognizeOneBean.getResult().get(i).getLocation().getHeight());

            Rect rect = new Rect((int) (mFaceRecognizeOneBean.getResult().get(i).getLocation().getLeft() / 1.5)
                    , (int) (mFaceRecognizeOneBean.getResult().get(i).getLocation().getTop() / 2)
                    , (int) (mFaceRecognizeOneBean.getResult().get(i).getLocation().getWidth() + mFaceRecognizeOneBean.getResult().get(i).getLocation().getLeft() / 1.5)
                    , (int) (mFaceRecognizeOneBean.getResult().get(i).getLocation().getHeight() + mFaceRecognizeOneBean.getResult().get(i).getLocation().getTop() / 2));
//Rect rect = new Rect(180,30,280,150);
            iv_first_face.drawFace(rect);
            getImageWidthHeight(path1);
        }
    }

    private void initClient() {
        // 初始化一个FaceClient
        client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.addGroupUser("abc", "aa");
            }
        }).start();


    }

    private void getImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GET_HEAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_HEAD_IMG) {

            if (data != null) {
                imgUri = data.getData();

                //获取图片路径
                String proj[] = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(imgUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();

                if (isFirst) {
                    iv_first_face.setImageURI(imgUri);
                    path1 = path;
                    Log.e("bbbbbbbbbbbbbb", "onActivityResult: " + path1);
                    isFirst = false;
                } else {
                    iv_second_face.setImageURI(imgUri);
                    path2 = path;
                }
            }


            //裁剪图片
//            Intent intent = new Intent();
//            intent.setAction("com.android.camera.action.CROP");
//            intent.setDataAndType(imgUri, "image/*");
////            intent.putExtra("crop", true);
////            intent.putExtra("aspectX", 1);
////            intent.putExtra("aspectY", 1);
////            intent.putExtra("outputX", 200);
////            intent.putExtra("outputY", 200);
//            intent.putExtra("return-data", true);
//
//            startActivityForResult(intent, CROP_HEAD);

        }

//        if (requestCode == CROP_HEAD) {
//            if (data == null) {
//                return;
//            }
//            Bundle bundle = data.getExtras();
//            bmp = bundle.getParcelable("data");


    }
    // }

    public static void getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        Log.e("ddddddd", "getImageWidthHeight: " + options.outHeight + " " + options.outWidth);
    }


    public void verifyUser(AipFace client) {

        ArrayList<String> path = new ArrayList<String>();

        path.add(path2);
        HashMap<String, Object> options = new HashMap<String, Object>(1);
        options.put("top_num", path.size());
        JSONObject res = client.verifyUser("bb", path, options);

        Log.e("ffffff", "verifyUser: " + res.toString());
        mFaceverifyUserBean = gson.fromJson(res.toString(),FaceverifyUserBean.class);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               if(mFaceverifyUserBean.getResults().get(0) >= 80){
                   Toast.makeText(MainActivity.this, "认证成功！！！", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(MainActivity.this, "认证失败！！！", Toast.LENGTH_SHORT).show();

               }
            }
        });
    }
}
