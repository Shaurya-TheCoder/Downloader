import java.util.ArrayList;
import java.util.List;

public class FileSizeSpliter {
    private static final int THREADS = 4;
    public static void main(String[] args) {
        fileSpilter(10);
    }
    public static List<long[]>  fileSpilter(long fileSize){
        long chunkSize = fileSize/THREADS;
        long start = 0;
        List<long[]> chunks = new ArrayList<>();
        for(int i = 1; i <= THREADS; i++){
            long end = start+chunkSize-1;
            if (i == THREADS)
                end = fileSize-1;
            //System.out.println("Thread-"+i+": "+start+"->"+end);
            chunks.add(new long[]{start, end});
            start = end+1;
        }
        return chunks;
    }

}
