package org.xdevs23.net;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ProgressBar;

import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.io.stream.InputStreamUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import io.xdevs23.cornowser.browser.updater.UpdateActivity;


public class DownloadUtils {
	
	public static ProgressBar progressUpdateBar = null;

    public static final int
            defaultBuf  =   8192,
            oneKiloByte =   1024;

    /**
     * Set the progress bar to control
     * @param id Id of the progressbar
     * @param context Actual context
     */
	public static void setProgressBar( @IdRes int id, Context context ) {
		View view = new View(context);
		progressUpdateBar = (ProgressBar) view.findViewById(id);
	}
	
	private static void logt(String loginfo) {
		Logging.logd(loginfo);
	}
	
	public static void downloadFile( String dUrl, String dest ) {
		try {
			DownloadAsync AsyncDownloader = new DownloadAsync();
			AsyncDownloader.execute(dUrl, dest);
		} catch (Exception e) {
			logt("(downloadFile) Exception thrown: " + e.getMessage() + " " + e.getStackTrace());
        }
	}
	
	public static String downloadString(String url, String defaultValue) {
		String dS = defaultValue;
		try {
			DownloadStringAsync asyncSDownloader = new DownloadStringAsync();
			dS = asyncSDownloader.execute(new String[] {url}).get();
		} catch(Exception ex) {
			logt("Exception thrown: " + StackTraceParser.parse(ex));
		}
		
		return dS;
	}

    public static String downloadString(String url) {
        return downloadString(url, "");
    }

    public static InputStream getInputStreamForConnection(String url) throws IOException {
        URL ur = new URL(url);
        URLConnection connection = ur.openConnection();

        connection.connect();

        return new BufferedInputStream(ur.openStream(), defaultBuf);
    }

	//////////////////////////////////////////////
	
	static class DownloadStringAsync extends AsyncTask < String, Integer, String > {

        private void logt(String loginfo) {
            Logging.logd("(DownloadAsync) " + loginfo);
        }

        @Override
        protected String doInBackground(String... fDUrl) {
            String doUrl = "[NOTHING]";
            String resultString = "";
            try {
                if (fDUrl == null)
                    logt("fDUrl is null");


                logt("Converting url...");

                doUrl = fDUrl[0];

                logt("Connecting...");

                URL url = new URL(doUrl);
                URLConnection connection = url.openConnection();

                connection.connect();

                logt("Preparing download for " + doUrl);
                InputStream input = new BufferedInputStream(url.openStream(),
                        512);

                logt("Downloading...");


                resultString = InputStreamUtils.readToString(input);

                if(resultString.lastIndexOf("\n") == resultString.length() - 1 ||
                        resultString.lastIndexOf("\r") == resultString.length() - 1)
                    resultString = resultString.substring(0, resultString.length() - 1);

                input.close();

                logt("Download finished");
            } catch (Exception e) {
                logt("Error: " + e.getMessage());
            }
            return resultString;
        }
    }
	
	//////////////////////////////////////////////

	static class DownloadAsync extends AsyncTask < String, String, String > {

        private long lengthOfFile = 1L;
		
		private void logt(String loginfo) {
			Logging.logd("(DownloadAsync) " + loginfo);
		}
		
		@Override
		protected String doInBackground(String... fDUrl) {
			int count;
			String doUrl = "[NOTHING]";
		    try {
		    	if(fDUrl == null) {
		    		logt("fDUrl is null");
		    	}
		    	
	    	  	logt("Converting url...");
	    	  	
	    	  	doUrl = fDUrl[0];
	    	  	
	    	  	logt("Connecting...");

	    	  	URL url = new URL(doUrl);
	            URLConnection connection = url.openConnection();
	            
	            connection.connect();
	            
	            try {
	            	lengthOfFile = Long.parseLong(connection.getHeaderField("Content-Length"));
	            } catch(Exception ex) {
	            	logt("Error getting content-length, using custom length");
	            	lengthOfFile = Long.MAX_VALUE;
	            }
	            
	            
	            logt("Total bytes to download: " + String.valueOf(lengthOfFile));
	            
	            logt("Preparing download for " + doUrl);
	            InputStream input = new BufferedInputStream(url.openStream(),
	                    										defaultBuf);
	            
	
	            logt("Making needed directories...");
	            
	            File dirFile = new File( fDUrl[1].substring(0, fDUrl[1].lastIndexOf('/')));
	            dirFile.mkdirs();
	            
	            String newSaveU = fDUrl[1];
				
				logt("Downloading...");
	
	            OutputStream output = new FileOutputStream(newSaveU);
	            
	            byte data[] = new byte[oneKiloByte];
	                        
	            long total = 0;
	
	            while ( (  count = input.read(data)  ) != -1 ) {
	                total += count;
	
	                publishProgress( String.valueOf(total) );
	                output.write(data, 0, count);
	            }
	
	
	            output.flush();
	
	            output.close();
	            input.close();
	            
	            logt("Download finished");
		      } catch ( Exception e ) {
		    	  logt("Error: " + e.getMessage());
		      }
			return "";
		}
		
		@Override
	    protected void onProgressUpdate(String... progress) {
			long pr = Long.parseLong(progress[0]);
			long pd = (long) (Math.round((double)( (   pr * 100  ) / lengthOfFile)));
			
			logt(String.valueOf((int)pd));
			
			UpdateActivity.updateProgress((int) pd);
	    }
		
	    @Override
	    protected void onPostExecute(String result) {
	    	logt("Result: " + result);
	    	UpdateActivity.endProgress();
	    	logt("Starting installation...");
	    	UpdateActivity.startUpdateInstallation();
	    	progressUpdateBar = null;
	
	    }
		
	}

}
