import java.text.DecimalFormat;
public class ByteConverter {
    private static final long THRESHOLD = 1024;

    private static final String[] UNITS = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};

    public String getSize(long bytes){
        if(bytes <= 0){
            return "0 B";
        }
        int unitIndex = 0;
        double size = (double) bytes;

        while(size >= THRESHOLD && unitIndex < UNITS.length){
            size /= THRESHOLD;
            unitIndex++;
        }

        String formatPattern = "#,##0." + new String(new char[2]).replace('\0', '0');
        DecimalFormat decimalFormat = new DecimalFormat(formatPattern);
        return decimalFormat.format(size) + " " + UNITS[unitIndex];
    }
}

