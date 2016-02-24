package helloword;



import java.util.Timer;
import java.util.TimerTask;

public class hello2 extends TimerTask
{
	hello2(Object a)
	{
new helloword();
	}
	@Override
	public void run() {
		maini();
	}
	public static void main(String...s)
	{
		 Timer timer = new Timer();
		 timer.schedule(new helloword(), 1, 5000);
		 
	}
public static void maini() {
	
	 for(;;){
	       new helloword();
	       System.out.print("-");
	      // maini();
	       }
}
}
