package xlxs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 *
 */
public class ExcelWriter {
	  public static String getDurationBreakdown(long millis)
	    {
	        if(millis < 0)
	        {
	            throw new IllegalArgumentException("Duration must be greater than zero!");
	        }

	        long days = TimeUnit.MILLISECONDS.toDays(millis);
	        millis -= TimeUnit.DAYS.toMillis(days);
	        long hours = TimeUnit.MILLISECONDS.toHours(millis);
	        millis -= TimeUnit.HOURS.toMillis(hours);
	        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
	        millis -= TimeUnit.MINUTES.toMillis(minutes);
	        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

	        StringBuilder sb = new StringBuilder(64);
	        sb.append(days);
	        sb.append(" Days ");
	        sb.append(hours);
	        sb.append(" Hours ");
	        sb.append(minutes);
	        sb.append(" Minutes ");
	        sb.append(seconds);
	        sb.append(" Seconds");

	        return(sb.toString());
	    }
	public static void main(String[] args) throws IOException {
		//@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Sheet 1");
		int rowCount = 0;
		int columnCount = 0;
		Row row = sheet.createRow(rowCount++);
		Cell cell = row.createCell(columnCount++);
		cell.setCellValue(getDurationBreakdown(System.currentTimeMillis()));

		try (FileOutputStream outputStream = new FileOutputStream("D:/TimesNow.xlsx")) {
			workbook.write(outputStream);
		}
	}

}
