import java.io.*;
import java.net.URLDecoder;

public class IOTest {

    public static void main(String[] args) throws IOException {
        File f = new File("C:\\Users\\manager\\Desktop\\srepaychk20171018.dat");
/*        InputStream input = new FileInputStream(f);
        byte[] bytes=new byte[input.available()];
        StringBuffer sb = new StringBuffer();
        while (input.read(bytes)!=-1) {
            for (byte b : bytes) {
                sb.append((char)b);
            }
        }
        System.out.println(sb);*/
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"GBK"));
        StringBuffer sb = new StringBuffer();
        String s;
        while ((s = reader.readLine()) != null) {
            sb.append(URLDecoder.decode(s,"GBK"));
            sb.append("\n");
        }
        System.out.println(sb);
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
//        writer.newLine();
//        writer.write("123456789");
//        writer.flush();

    }

}
