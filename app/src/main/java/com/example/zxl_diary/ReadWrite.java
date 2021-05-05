package com.example.zxl_diary;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReadWrite extends AppCompatActivity implements View.OnClickListener{
    private EditText etTitle,etReadWrite;
    private Button btClear,btSave;
    private String strtime;
    private int i;
    private int index;
    private ArrayList<String> TITLE=new ArrayList<>();
    private ArrayList<String>CONTEXT=new ArrayList<>();
    private ArrayList<String>NUMBER=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_write);


        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-mm-dd//hh:mm:ss");
        Date curDate=new Date(System.currentTimeMillis());
        strtime=formatter.format(curDate);

        Intent it=getIntent();
        index=it.getIntExtra("index",-1);

        db_data helper=new db_data(ReadWrite.this,ReadWrite.this.getExternalFilesDir(null)+"/db_database.db");
        SQLiteDatabase database=helper.getWritableDatabase() ;
        Cursor cursor=database.query("MYSTUDENTDB",null,null,null,null,null,null);
        if (cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                TITLE.add(cursor.getString(2));
                CONTEXT.add(cursor.getString(4));
            }
        }
        findv();

            String[] array1=TITLE.toArray(new String[0]);
            String[] array2=CONTEXT.toArray(new String[0]);
            if(index!=-1)
            {
                etTitle.setText(array1[index]);
                etReadWrite.setText(array2[index]);
            }



        bindButton();


    }
    private void findv(){
        etReadWrite=(EditText)findViewById(R.id.etReadWrite);
        etTitle=(EditText)findViewById(R.id.etTitle);
        btClear=(Button)findViewById(R.id.btClear);
        btSave=(Button)findViewById(R.id.btSave);
    }
    private void bindButton(){
        btClear.setOnClickListener(this);
        btSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        db_data helper=new db_data(ReadWrite.this,ReadWrite.this.getExternalFilesDir(null)+"/db_database.db");
        SQLiteDatabase database=helper.getWritableDatabase() ;
        Cursor cursor=database.query("MYSTUDENTDB",null,null,null,null,null,null);
        if (cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                TITLE.add(cursor.getString(2));
                CONTEXT.add(cursor.getString(4));
                NUMBER.add(cursor.getString(3));
            }
        }

        if(index!=-1)
        {
         if(v.getId()==R.id.btSave)
         {
             ContentValues value2=new ContentValues();
             if(cursor.getCount()>0)
             {
                 while (cursor.moveToNext())
                 {
                     i=cursor.getInt(3);
                     if(i!=0)
                     {
                         i=i+1;
                     }
                 }
             }
             String[] array3=NUMBER.toArray(new String[0]);

             String where="NUMBER"+"="+array3[index];

             value2.put("CONTEXT",etReadWrite.getText().toString());
             value2.put("TITLE",etTitle.getText().toString());
             value2.put("TIME",strtime);
             value2.put("NUMBER",i);

             database.update("MYSTUDENTDB",value2,where,null);
             database.close();


             etTitle.setText("");
             etReadWrite.setText("");
         }

        }





        if(v.getId()==R.id.btClear){
            etTitle.setText("");
            etReadWrite.setText("");

        }
        if(index==-1)
        {
            if(v.getId()==R.id.btSave)
            {
                ContentValues value1=new ContentValues();
                if(cursor.getCount()>0)
                {
                    while (cursor.moveToNext())
                    {
                        i=cursor.getInt(3);
                        if(i!=0)
                        {
                            i=i+1;
                        }
                    }
                }
                value1.put("CONTEXT",etReadWrite.getText().toString());
                value1.put("TITLE",etTitle.getText().toString());
                value1.put("TIME",strtime);
                value1.put("NUMBER",i);
                long i=database.insert("MYSTUDENTDB",null,value1);
                if (i == -1)
                {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                }
        }


        }
        database.close();//清空输入框
        etTitle.setText("");
        etReadWrite.setText("");
    }

    public class db_data extends SQLiteOpenHelper
    {
        public db_data(@Nullable Context context, @Nullable String name)
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
