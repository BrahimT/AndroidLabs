package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<Message> list = new ArrayList<>();
    LayoutInflater inflater;
    chatAdapter aListAdapter;
    EditText editText;
    Button sender, Receiver;
    String n, o;
    ListView p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        // vars
        p = (ListView) findViewById(R.id.listview);
        sender = (Button) findViewById(R.id.sendbtn);
        Receiver = (Button) findViewById(R.id.rcvbtn);
        editText = (EditText) findViewById(R.id.editText7);


        aListAdapter = new chatAdapter();
        p.setAdapter(aListAdapter);
        sender.setOnClickListener(e -> {
            list.add(new Message(editText.getText().toString(), true));
            aListAdapter.notifyDataSetChanged();
            editText.setText("");
        });
        Receiver.setOnClickListener(e -> {
            list.add(new Message(editText.getText().toString(), false));
            aListAdapter.notifyDataSetChanged();
            editText.setText("");

        });



        p.setOnItemLongClickListener(((parent, view, position, id) -> {

            AlertDialog.Builder alrtBldr = new AlertDialog.Builder(ChatRoomActivity.this);
            AlertDialog alertDialog = alrtBldr .setTitle("Do you want to delete this message?" )
                    .setMessage("The message position is " + position + "\n the message id is " + id)
                    .setPositiveButton("Delete", (click, args)-> {
                        list.remove(position);
                        aListAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Close", (click,arg)-> {

                    })
                    .create();

            alertDialog.show();
            return true;
        }));

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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        //    View view1 = convertView;
            String s;
            View view2;
            Message message= (Message) getItem(position);
            inflater = getLayoutInflater();

            if (message.getSender()) {
                view2 = inflater.inflate(R.layout.row_send, null);
                TextView txtView = view2.findViewById(R.id.sendText);
                txtView.setText(message.getMessage());
                return  view2;


            } else {
                view2 = inflater.inflate(R.layout.row_receive, null);
                TextView txtView = view2.findViewById(R.id.rcvText);
                txtView.setText(message.getMessage());
                return  view2;
            }

        }


    }//endof list adapter


    public class Message {

        String user_message;
        boolean sender;


        public Message(String user_message, boolean sender) {
            this.user_message = user_message;
            this.sender = sender;
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




    }
}







