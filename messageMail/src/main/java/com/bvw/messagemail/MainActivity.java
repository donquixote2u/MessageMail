package com.bvw.messagemail;
import java.util.ArrayList;

import com.bvw.android_library.Message_Listview.MessageAdapter;
import com.bvw.android_library.Message_Listview.MessageAdapter.MessageDetails;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
public ArrayList<MessageDetails> msgList;
Message message;

private Handler handler = new Handler() {

    public void handleMessage(Message message) {
      // get android message from service, extract email data object,
      // and add to list view	
      Message returned = new Message();
      returned=Message.obtain(message);
      MessageDetails disp = (MessageDetails) returned.obj;
      msgList.add(disp);
      // Get a handle to the list view
      ListView lv = (ListView) findViewById(R.id.list01);
      lv.setAdapter(new MessageAdapter(msgList , MainActivity.this));
	  } 
    };
 
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    msgList = new ArrayList<MessageDetails>();
    setContentView(R.layout.activity_main);
  }

  public void onClick(View view) {
    Intent intent = new Intent(this, MailService.class);
    // Create a new Messenger for the communication back
    Messenger messenger = new Messenger(handler);
    intent.putExtra("MESSENGER", messenger);
    // intent.setData(Uri.parse("http://www.vogella.com/index.html"));
    // intent.putExtra("urlpath", "http://www.vogella.com/index.html");
    startService(intent);
  }
  

} 