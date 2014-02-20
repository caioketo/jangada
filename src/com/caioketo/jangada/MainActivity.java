package com.caioketo.jangada;

import com.caioketo.jangada.network.TCPClient;
import com.caioketo.jangada.NetworkMessageProtos.ChatPacket;
import com.caioketo.jangada.NetworkMessageProtos.Messages;
import com.caioketo.jangada.NetworkMessageProtos.NetworkMessage;
import com.caioketo.jangada.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public TCPClient mTcpClient;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new connectTask().execute("");
        
        findViewById(R.id.btnEnviar).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				EditText edt = ((EditText)findViewById(R.id.editText1));
				Messages.Builder mensagens = Messages.newBuilder();
				NetworkMessage.Builder message = NetworkMessage.newBuilder();
				ChatPacket.Builder chat = ChatPacket.newBuilder();
				chat.setMessage(edt.getText().toString());
				message.setChatPacket(chat.build());
				mensagens.addNetworkmessage(message.build());
				mTcpClient.sendMessage(mensagens.build().toByteArray());
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public class connectTask extends AsyncTask<String,String,TCPClient> {    	 
        @Override
        protected TCPClient doInBackground(String... message) { 
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {				
				@Override
				public void messageReceived(byte[] message) {
					// TODO Auto-generated method stub		
					// Parse message
				}
			});
            mTcpClient.run(); 
            return null;
        }
    }
}
