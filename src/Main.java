import java.io.File;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String[] args){
        File file = new File("text1.txt");
        try(RandomAccessFile rf = new RandomAccessFile(file, "rw")){
            rf.writeChars("Hello");
            rf.seek(10);
            rf.writeChars("WORLD");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}