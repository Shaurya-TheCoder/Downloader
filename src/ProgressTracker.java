import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ProgressTracker extends Thread{
    AtomicLong td;
    long fileSize;

    ProgressTracker(AtomicLong td, long fz){
        this.td = td;
        this.fileSize = fz;
    }

    @Override
    public void run() {
        long totalBytes;
        long prevBytes = 0;
        long startTime = System.currentTimeMillis();

        while ((totalBytes = td.get()) < fileSize) {
            long currentTime = System.currentTimeMillis();
            long elapsed = (currentTime - startTime) / 1000;


            double speed = 0;
            speed = ((totalBytes - prevBytes) / (1024.0 * 1024.0)) * 2;

            int progress = (int) Math.min(99, (totalBytes * 100) / fileSize)+1;
            System.out.printf("\r%s %3d%% | %6.2f MB/s | ETA: %s", Downloader.createBar(progress),
                    progress, speed, ProgressTracker.taCal(fileSize - totalBytes, speed));
            try {
                prevBytes = totalBytes;
                startTime = currentTime;
                sleep(500);
            } catch (InterruptedException e) {
                System.out.println("\nTracker stopped.");
                return;
            }
        }
    }
    public static String taCal(long remBytes, double speed){
        speed *= 1024 * 1024;

        double etaSeconds = 0;

        if(speed > 0){
            etaSeconds = remBytes/speed;
        }
        long eta = (long) etaSeconds;

        String etaStr;

        if(eta < 60){
            etaStr = eta+"s          ";
        } else if (eta < 3600) {
            etaStr = (eta/60) + "m" + (eta % 60) + "s        ";
        }else{
            etaStr = (eta/3600)+"h"+((eta%3600)/60)+"m       ";
        }
        return  etaStr;
    }
}
