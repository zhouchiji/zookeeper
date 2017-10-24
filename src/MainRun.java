import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainRun {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
       /* Thread1 t1=new Thread1();
        t1.start();
        Thread.sleep(1000);
        t1.interrupt();*/
/*        AtomicBoolean ab=new AtomicBoolean(false);
        for (int i=0;i<10;i++){
            ab.set(true);
        }
        System.out.println(ab.get());*/
        /**
         * dead lock
         */
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        class MainCall implements Callable {

            @Override
            public Object call() throws Exception {
                Future f1 = null;
                int result = 0;
                f1 = exec.submit(new Thread1());
                f1.get();// dead lock happen
                return result;
            }
        }
        Future f = exec.submit(new MainCall());
        System.out.println(f.get());
    }
}

