import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;


class DownloadWorker implements Runnable{
    URL url;
    String fileName;
    long startByte;
    long endByte;
    AtomicLong totalDownloaded; //Works Like A Synchronized Counter

    DownloadWorker(URL url, String fn, long sb, long eb, AtomicLong td){
        this.url = url;
        this.fileName = fn;
        this.startByte = sb;
        this.endByte = eb;
        this.totalDownloaded = td;
    }

    @Override
    public void run() {

        try(RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){
            String threadName = Thread.currentThread().getName();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String byteReq = "bytes="+startByte+"-"+endByte;
            con.setRequestProperty("Range", byteReq);
            //System.out.println(threadName+"- Range: "+byteReq);

            int responseCode = con.getResponseCode();
            //System.out.println(threadName+"- Response-Code: "+responseCode);
            if(responseCode != 206){
                System.out.println("Error: Server Doesn't support partial content sharing");
                return;
            }

            InputStream inp = con.getInputStream();

            //RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
            raf.seek(startByte);

            byte[] buffer = new byte[4096];
            int bytesRead;
            long remaining = endByte - startByte + 1;

            while(remaining > 0 && (bytesRead = inp.read(buffer)) != -1){
                if(bytesRead > remaining){
                    bytesRead = (int) remaining;
                }
                raf.write(buffer, 0, bytesRead);
                remaining -= bytesRead;

                totalDownloaded.addAndGet(bytesRead);
            }
            inp.close();
            con.disconnect();
        }
//        catch(SocketException se){
//            try {
//                Thread.sleep(1);
//                Thread.currentThread().run();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
