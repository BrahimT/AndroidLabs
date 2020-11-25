package com.example.androidlabs;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<Message> list = new ArrayList<>();
    int id1, id2, id3;
    SQLiteDatabase db;
    LayoutInflater inflater;
    chatAdapter aListAdapter;
    EditText editText;
    Button sender, Receiver;
    String c2 = "";
    int n;
    String o;
    Message message;
    long z;
    ListView p;
    String[] save_Results;
    androidx.fragment.app.Fragment Fragment;
    public static final String ACTIVITY_NAME = "ChatRoomActivity";
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    private DetailFragment dFragment;
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        // vars
        p = (ListView) findViewById(R.id.listview);
        sender = (Button) findViewById(R.id.sendbtn);
        Receiver = (Button) findViewById(R.id.rcvbtn);
        editText = (EditText) findViewById(R.id.editText7);
        isTablet = (findViewById(R.id.frame) != null);
        MyOpener dbHelper = new MyOpener(this);

        db = dbHelper.getWritableDatabase();
        String[] save_Results = {MyOpener.ColumnID, MyOpener.MESSAGE, MyOpener.TYPE};
        Cursor c = db.query(false, MyOpener.Tabe_Name, save_Results, null, null, null, null, null, null);
        id1 = c.getColumnIndex(MyOpener.ColumnID);
        id2 = c.getColumnIndex(MyOpener.MESSAGE);
        id3 = c.getColumnIndex(MyOpener.TYPE);

        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();
            n = c.getInt(id1);
            o = c.getString(id2);
            z = c.getInt(id3);

            if (z == 0) {
                list.add(new Message(o, false, z));
            } else {
                list.add(new Message(o, true, z));
            }
        }

        aListAdapter = new chatAdapter();
        p.setAdapter(aListAdapter);
        printCursor(c, 1);

        sender.setOnClickListener(e -> {
            ContentValues cv = new ContentValues();
            cv.put(MyOpener.MESSAGE, editText.getText().toString());
            cv.put(MyOpener.TYPE, 1);
            long id = db.insert(MyOpener.Tabe_Name, null, cv);
            list.add(message = new Message(editText.getText().toString(), true, id));
            aListAdapter.notifyDataSetChanged();
            editText.setText("");


            //  list.add(new Message(editText.getText().toString(), true));
            //   aListAdapter.notifyDataSetChanged();
            //   editText.setText("");
        });
        Receiver.setOnClickListener(e -> {
            //   list.add(new Message(editText.getText().toString(), false));
            //   aListAdapter.notifyDataSetChanged();
            //  editText.setText("");
            ContentValues cv = new ContentValues();
            cv.put(MyOpener.MESSAGE, editText.getText().toString());
            cv.put(MyOpener.TYPE, 0);
            long id = db.insert(MyOpener.Tabe_Name, null, cv);

            list.add(message = new Message(editText.getText().toString(), false, id));
            aListAdapter.notifyDataSetChanged();
            editText.setText("");


        });
                p.setOnItemClickListener((parent, view, position, id) -> {

                    Bundle dataToPass = new Bundle();
                    dataToPass.putString(ITEM_SELECTED, list.get(position).user_message );
                    dataToPass.putInt(ITEM_POSITION, position);
                    dataToPass.putLong(ITEM_ID, id);

                    if( isTablet) { //both the list and details are on the screen:

                        dFragment = new DetailFragment(); //add a DetailFragment

                        dFragment.setArguments( dataToPass ); //pass it a bundle for information
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, Fragment) //Add the fragment in FrameLayout
                                .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {

                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
                });

        p.setOnItemLongClickListener(((parent, view, position, id) -> {

            AlertDialog.Builder alrtBldr = new AlertDialog.Builder(ChatRoomActivity.this);
            AlertDialog alertDialog = alrtBldr .setTitle("Do you want to delete this message?" )
                    .setMessage("The message position is " + position + "\n the database id is " + id)
                    .setPositiveButton("Delete", (click, args)-> {
                        list.remove(position);
                        aListAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Close", (click, arg) -> {
                        finish();
                    })
                    .create();

            alertDialog.show();
            return true;
        }));


    }


    protected void printCursor(Cursor c, int version) {
        String[] columnNames = c.getColumnNames();
        Log.d("PrintCursor", "The number of columns in the cursor: " + c.getColumnCount());
        Log.d("PrintCursor", "The database version number: " + db.getVersion());
        Log.d("PrintCursor", "The number of rows in the cursor: " + c.getCount());
        for (int a = 0; a < c.getColumnCount(); a++) {
            Log.d("PrintCursor", "The name of columns in the cursor: " + c.getColumnName(a));
        }

        if (!c.isAfterLast()) {
            Log.d("PrintCursor", "Print out each row of results in the cursor: " + DatabaseUtils.dumpCurrentRowToString(c));
        }
    }


    class chatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //    View view1 = convertView;
            String s;
            View view2;
            Message message = (Message) getItem(position);
            inflater = getLayoutInflater();

            if (message.getSender()) {
                view2 = inflater.inflate(R.layout.row_send, null);
                TextView txtView = view2.findViewById(R.id.sendText);
                txtView.setText(message.getMessage());
                return view2;


            } else {
                view2 = inflater.inflate(R.layout.row_receive, null);
                TextView txtView = view2.findViewById(R.id.rcvText);
                txtView.setText(message.getMessage());
                return view2;
            }


        }


    }//endof list adapter
    class MyOpener extends SQLiteOpenHelper {
        public static final String Database = "Database";
        public static final String Tabe_Name = "Message";
        public static final String ColumnID = "id";
        public static final String MESSAGE = "MESSAGE";
        public static final String TYPE = "TYPE";



        public MyOpener(Context ctx) {
            super(ctx, Database, null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " +Tabe_Name+"("+ColumnID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+MESSAGE+" TEXT,"+TYPE+" INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL("DROP TABLE IF EXISTS " + Tabe_Name);
            onCreate(db);
        }
        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
            if(oldVer>newVer){
                db.execSQL("DROP TABLE IF EXISTS " + Tabe_Name);
                onCreate(db);
            }
        }
    }// end SQLiteOpenHelper





    public class Message {

        String user_message;
        boolean sender;
        long id;



        public Message(String user_message, boolean sender,long id) {
            this.user_message = user_message;
            this.sender = sender;
            this.id=id;
        }

        public String getMessage() {
            return user_message;
        }

        public void setMessage(String user_message) {
            this.user_message = user_message;
        }

        public boolean getSender() {
            return sender;
        }
        public long getId() {
            return id;
        }



    }
}







