package it.iuland.tetris;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;

public class Log {

	private String log;

	public Log(){
		this.log = "";
	}

	public void toLog(Object caller, String msg){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("it"));
		Date date = new Date();
		this.log += dateFormat.format(date) + " - " + caller.getClass().getName() + " - " + msg + "\n";

		//Stampo il messaggio
		android.util.Log.v(caller.getClass().getName(), msg);
	}

	//	public void save(String message){
	//		this.toLog(this, "Bug Report Message: " + message);
	//		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("it"));
	//		Date date = new Date();
	//		File persistentLog = new File(Environment.getExternalStorageDirectory()+File.separator+"TetrisOnlineApp",
	//										dateFormat.format(date) + "log.txt");
	//	    persistentLog.mkdirs();
	//		try {
	//			FileOutputStream outStream = new FileOutputStream(persistentLog);
	//			OutputStreamWriter outWriter = new OutputStreamWriter(outStream);
	//			outWriter.append(this.log);
	//			outWriter.close();
	//			outStream.close();		
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//	}

	public boolean save(Context context, String message) throws IOException{		
		this.toLog(this, "Bug Report Message: " + message);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", new Locale("it"));
		Date date = new Date();
		String fileName = dateFormat.format(date) + "_log.txt";

		File pathsd = Environment.getExternalStorageDirectory();
		File directory = new File(pathsd.getAbsolutePath() + "/TetrisApp/");
		if(!directory.exists())
			directory.mkdir();

		File file = new File(directory, fileName);
		FileOutputStream outStreamer = new FileOutputStream(file);
		OutputStreamWriter outWriter = new OutputStreamWriter(outStreamer);
		outWriter.write(this.log);
		outWriter.flush();
		outWriter.close();
		return true;
	}
}
