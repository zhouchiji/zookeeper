import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Thread1 extends Thread {
    BlockingDeque<String> bs=new LinkedBlockingDeque<>();
    @Override
    public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println(i);
            }
    }
}
