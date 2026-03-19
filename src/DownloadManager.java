import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

class DownloadManager {
//    private List<Thread> threads = new ArrayList<>();
    private  ExecutorService executor;
    public void startWorker(List<long []> chunks, URL url, String fileName, AtomicLong tb) throws InterruptedException {
        int THREADS = chunks.size();
        executor = Executors.newFixedThreadPool(THREADS);
        for(long[] chunk : chunks){
            executor.submit(new DownloadWorker(url, fileName, chunk[0], chunk[1], tb));
            Thread.sleep(200);
        }
        executor.shutdown();
    }
    public void waitForCompletion() throws InterruptedException{
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
}
