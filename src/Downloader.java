import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.FileOutputStream;

//"https://ignca.gov.in/Asi_data/279.pdf"
public class Downloader{
    public static void main(String[] args) throws MalformedURLException{
        //URL object pointing to the resource
        if(args.length == 0){
            System.out.println("Usage: java Downloader <URL>");
            return;
        }
        URL url = new URL(args[0]);
        String[] urlPath = url.getPath().split("/");
        String fileName = urlPath[urlPath.length-1];

        try {
            //opening a connection to the url
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream(); // inputStream
            FileOutputStream output = new FileOutputStream(fileName);
            ByteConverter bc = new ByteConverter();

            long fileLength = con.getContentLengthLong();

            String fileSize = bc.getSize(fileLength);

            byte[] buffer = new byte[4096];
            int BytesRead;
            long downloaded = 0L;
            int prevProg = 0;

            System.out.println("Downloading: "+fileName);
            System.out.println("Size: "+fileSize);

            long startTime = System.currentTimeMillis();
            while((BytesRead = input.read(buffer)) != -1){
                output.write(buffer, 0, BytesRead);
                downloaded += BytesRead;

                //speed calculation
                long currentTime = System.currentTimeMillis();
                long elapsedTime = (currentTime - startTime)/1000;
                float speed = 0;
                if(elapsedTime > 0)
                    speed = (float)(downloaded/(1024f*1024f*elapsedTime));

                int progress = (int)((downloaded * 100) / fileLength);
                if(progress > prevProg){
                    System.out.printf("\r%s %d%% | %.2f MB/s",createBar(progress),progress, speed);
                    prevProg = progress;
                }
            }
            input.close();
            output.close();


            String contentType = con.getContentType();

//            System.out.println();

//            System.out.println("Downloaded Bytes: "+downloaded+" Byte");
//            System.out.println("Content-Type: "+ contentType);
            con.disconnect();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    static String createBar(int progress){
        int barwidth= 20;
        int filled = (progress*barwidth)/100;
        int empty = barwidth - filled;

        StringBuilder bar = new StringBuilder();
        bar.append("[");
        bar.append("█".repeat(Math.max(0, filled)));
        bar.append("░".repeat(Math.max(0, empty)));
        bar.append("]");

        return bar.toString();
    }
}
