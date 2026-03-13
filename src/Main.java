import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("https://example.com/files/video.mp4");
        String[] file = url.getPath().split("/");
        System.out.println(file[file.length-1]);
    }
}