package com.example.zxl_diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView tvNotive,textView2,textView3;
    private ImageView imageView1,imageView2;
    private EditText etPassword;
    private Button btLogin, btCheck, btManage,reset;
    private String TAG = "zxlzxl";
    private boolean loginFlag = false;

    private ArrayList<String> TITLE=new ArrayList<>();
    private ArrayList<String>CONTEXT=new ArrayList<>();
    private ArrayList<String>TIME=new ArrayList<>();
    private int index;
    private boolean flag=true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findv();
        bindButton();
        btCheck.setVisibility(View.GONE);
        btManage.setVisibility(View.GONE);
        reset.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);

    }

    private void bindButton()
    {
        btLogin.setOnClickListener(this);
        btCheck.setOnClickListener(this);
        btManage.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    private void findv()
    {
        tvNotive = (TextView) findViewById(R.id.tvNotice);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btCheck = (Button) findViewById(R.id.btCheck);
        btManage = (Button) findViewById(R.id.btManage);
        reset=(Button)findViewById(R.id.reset);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        textView2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);


    }
    private void Read(String[] array1,String[] array2,String[] array3)
    {


        AlertDialog contect = new AlertDialog.Builder(this)
                .setTitle(array1[index])
                .setMessage(array2[index]+'\n'+array3[index])
                .create();
        contect.show();
    }


    @Override
    public void onClick(View v)
    {
        db_data helper=new db_data(MainActivity.this,MainActivity.this.getExternalFilesDir(null)+"/db_database.db");
        SQLiteDatabase database=helper.getWritableDatabase() ;
        if (loginFlag == false)
        {
            if (etPassword.getText().toString().equals("zxl"))
            {
                tvNotive.setText("等候多时");
                loginFlag = true;
                etPassword.setVisibility(View.GONE);
                btLogin.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                imageView1.setVisibility(View.GONE);

                textView3.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                btCheck.setVisibility(View.VISIBLE);
                btManage.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
            } else
                {
                AlertDialog ErrorDialog1 = new AlertDialog.Builder(this)
                        .setMessage("密码错误")
                        .create();
                ErrorDialog1.show();
                }
        }
            if(v.getId()==R.id.btCheck)
            {
                Cursor cursor = database.query("MYSTUDENTDB", null, null, null, null, null, null);
                if (cursor.getCount() > 0)
                {
                    if(flag)
                    {
                        while (cursor.moveToNext())
                        {
                            TITLE.add(cursor.getString(2));
                            CONTEXT.add(cursor.getString(4));
                            TIME.add(cursor.getString(1));
                        }
                        flag=false;
                    }

                    String[] array1=TITLE.toArray(new String[0]);
                    String[] array2=CONTEXT.toArray(new String[0]);
                    String[] array3=TIME.toArray(new String[0]);


                    AlertDialog check = new AlertDialog.Builder(this)
                            .setTitle("选择日记")
                            .setSingleChoiceItems(array1, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    index=which;
                                }
                            })
                            .setPositiveButton("查看", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Read(array1,array2,array3);
                                }
                            })
                            .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                database.delete("MYSTUDENTDB","TITLE"+"="+array1[index],null);
                                database.close();
                                }
                            })
                            .setNeutralButton("更改", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent it=new Intent(MainActivity.this,ReadWrite.class);
                                    it.putExtra("index",index);
                                    startActivity(it);

                                }
                            })
                            .create();
                    check.show();
                } else {
                    AlertDialog ErrorDialog2 = new AlertDialog.Builder(this)
                            .setMessage("没有数据")
                            .create();
                    ErrorDialog2.show();
                }

            }
           if(v.getId()==R.id.btManage) {

               Intent it = new Intent(MainActivity.this, ReadWrite.class);
               startActivity(it);
           }
           if(v.getId()==R.id.reset){
               flag=true;
               TITLE=new ArrayList<>();
               CONTEXT=new ArrayList<>();
           }

        }

    public class db_data extends SQLiteOpenHelper
    {
        public db_data(@Nullable Context context,@Nullable String name)
        {
            super(context,name,null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            String DataBase_SQL="CREATE TABLE " + "MYSTUDENTDB" + " (" + "_id" + " INTEGER primary key autoincrement, " +  "TIME" + " text not null, "+"TITLE"
                    + " text not null, " + "NUMBER" + " text not null, " + "CONTEXT" +" text not null" +");";
            db.execSQL(DataBase_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}

