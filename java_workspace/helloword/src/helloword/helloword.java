package helloword;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class helloword extends TimerTask {

	@Override
	public void run() {
		while (Runtime.getRuntime().maxMemory()!=1790) {
			try {
				
			//	Thread.sleep(1000);
		
					System.out.print("Total Memory Now"
							+ Runtime.getRuntime().);
					System.out.print("Max Memory"
							+ Runtime.getRuntime().maxMemory());
					System.out.print("Heating !!! ");
					maini();
		

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.print("YOU ARE RIGHT  ");
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String... s) {
		Timer timer = new Timer();
		timer.schedule(new helloword(), 1, 5000);

	}

	public static void maini() throws IOException, InterruptedException {
		System.out
				.println("Total Memory Now"
						+ (((Runtime.getRuntime().totalMemory()) / 1024) / 1024)
						+ "MB");
		System.out.println("Max Memory"
				+ (((Runtime.getRuntime().maxMemory()) / 1024) / 1024) + "MB");
		ArrayList<helloword> objects = new ArrayList<helloword>();
		ArrayList<String> bytes = new ArrayList<String>();
		@SuppressWarnings("unused")
		int i = 0;
		for (;;) {
			objects.add(new helloword());
			File file = new File("D:\\office_free_2013.exe");
			File file2 = new File("D:\\office_free_2013.exe");
			File file3 = new File("D:\\office_free_2013.exe");
			File file4 = new File("D:\\office_free_2013.exe");
			bytes.add(ReadFileInByteArrayWithFileInputStream.read(file)
					+ ReadFileInByteArrayWithFileInputStream.read(file2)
					+ ReadFileInByteArrayWithFileInputStream.read(file3)
					+ ReadFileInByteArrayWithFileInputStream.read(file4));

			System.out.print("-");
			new hello2(new helloword());

		}

	}
}