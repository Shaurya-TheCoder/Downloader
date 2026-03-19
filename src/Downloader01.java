import java.net.URL;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

//"https://ignca.gov.in/Asi_data/279.pdf"
public class Downloader01 {
    public static void main(String[] args) throws Exception {
        //URL object pointing to the resource
        if (args.length == 0) {
            System.out.println("Usage: java Downloader <URL>");
            return;
        }
        URL url = new URL(args[0]);
        String[] urlPath = url.getPath().split("/");
        String fileName = urlPath[urlPath.length - 1];
        AtomicLong totalBytes =  new AtomicLong(0);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        long fileLength = con.getContentLengthLong();

        ByteConverter bc = new ByteConverter();
        String fileSize = bc.getSize(fileLength);

        System.out.println("Downloading: "+fileName);
        System.out.println("Size: "+fileSize);
        System.out.println();

        List<long[]> chunks = FileSizeSpliter.fileSpilter(fileLength);

        Downloader.preAllocateFile(fileName, fileLength);
        ProgressTracker tracker = new ProgressTracker(totalBytes, fileLength);
        tracker.start();

        DownloadManager manager = new DownloadManager();
        manager.startWorker(chunks, url, fileName, totalBytes);

        manager.waitForCompletion();
        tracker.interrupt();

        System.out.println("\nDownload Complete!");
    }
}