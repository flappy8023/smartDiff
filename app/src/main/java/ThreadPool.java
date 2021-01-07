import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yaochuan on 2017-8-18.
 */

public class ThreadPool {
	private static final ThreadPool sInstance = new ThreadPool();
	private ExecutorService mExecutorService;
	public static ThreadPool get() {
		return sInstance;
	}

	private ThreadPool() {
		mExecutorService = Executors.newFixedThreadPool(12);
	}

	public void submit(Runnable r) {
		mExecutorService.submit(r);
	}
}
