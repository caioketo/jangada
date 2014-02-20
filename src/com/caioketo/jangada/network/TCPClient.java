package com.caioketo.jangada.network;

import android.util.Log;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

	private byte[] serverMessage;
	public static final String SERVERIP = "192.168.0.9"; //your computer IP address
	public static final int SERVERPORT = 7777;
	private OnMessageReceived mMessageListener = null;
	private boolean mRun = false;

	BufferedOutputStream out;
	BufferedInputStream in;

	public TCPClient(OnMessageReceived listener) {
		mMessageListener = listener;
	}

	public void sendMessage(byte[] message){
		if (out != null) {
			try {
				out.write(message);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopClient(){
		mRun = false;
	}

	public void run() {
		mRun = true;

		try {
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			Log.e("TCP Client", "C: Connecting...");
			Socket socket = new Socket(serverAddr, SERVERPORT);
			try {
				out = new BufferedOutputStream(socket.getOutputStream());
				Log.e("TCP Client", "C: Sent.");
				Log.e("TCP Client", "C: Done.");
				serverMessage = new byte [65536];
				in = new BufferedInputStream(socket.getInputStream());
				while (mRun) {
					int bytesRead = in.read(serverMessage);
					if (bytesRead > 0) {
						mMessageListener.messageReceived(serverMessage);
					}
					bytesRead = -1;
				}
				Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
			} catch (Exception e) {
				Log.e("TCP", "S: Error", e);
			} finally {
				socket.close();
			}
		} catch (Exception e) {
			Log.e("TCP", "C: Error", e);
		}
	}

	public interface OnMessageReceived {
		public void messageReceived(byte[] message);
	}
}
