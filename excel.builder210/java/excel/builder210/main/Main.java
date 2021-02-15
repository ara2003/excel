package excel.builder210.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import excel.builder210.main.Document.Line;

public final class Main {

	public static String[] split(String string){
		return string.replace('^', '_').split("_"); //$NON-NLS-1$
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
        List<Document> documents = new ArrayList<>();
        
        for(File file210 : new File(Messages.getString("nameInputFolder")).listFiles()) { //$NON-NLS-1$
        	documents.add(new Document(file210));
        }

        List<String> personalAccounts = new ArrayList<>();//sorted
        List<String> paymentPeriods = new ArrayList<>();//sorted
        Map<String, Map<String, Double>> mapSum = new HashMap<>();//[paymentPeriod, personalAccount] => sum
        
        for(Document doc : documents)
        	for(Line line : doc.getLines()) {
        		if(!personalAccounts.contains(line.getPersonalAccount()))personalAccounts.add(line.getPersonalAccount());
        		String date =  line.getDatePayment().getMonth() + "." + line.getDatePayment().getYear();
        		if(!paymentPeriods.contains(date))paymentPeriods.add(date);
        	}
        for(String date : paymentPeriods) {
        	Map<String, Double> m = new HashMap<>();
        	for(String ac : personalAccounts) {
        		m.put(ac, .0);
        	}
        	mapSum.put(date, m);
        }
        for(Document doc : documents) {
        	for(Line line : doc.getLines()) {
        		Map<String, Double> ac = mapSum.remove(line.getPaymentPeriod());
        		Double sum = ac.remove(line.getPersonalAccount());
        		sum += line.getOnCurrentAccount();
        		ac.put(line.getPersonalAccount(), sum);
        		mapSum.put(line.getPaymentPeriod(), ac);
        	}
        }

        Collections.sort(personalAccounts, (a, b)->{
        	return (int)((Double.parseDouble(a.replace('-', '.')) - Double.parseDouble(b.replace('-', '.')))*1000);
        });
        Collections.sort(paymentPeriods, (a, b)->{
        	int ay, by, am, bm;
        	ay = Integer.parseInt(a.substring(3));
        	by = Integer.parseInt(b.substring(3));
        	am = Integer.parseInt(a.substring(0, 2));
        	bm = Integer.parseInt(b.substring(0, 2));
        	if(ay != by)return ay - by;
        	return am - bm;
        });
        
       	Workbook book = new HSSFWorkbook();

       	CellStyle cellStyle = book.createCellStyle();
       	cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
       	cellStyle.setWrapText(true);
       	cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
       	CellStyle dateStyle = book.createCellStyle();
       	dateStyle.setAlignment(CellStyle.ALIGN_CENTER);
       	dateStyle.setWrapText(true);
       	dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
       	dateStyle.setDataFormat(book.createDataFormat().getFormat("dd.mm.yyyy"));
       	
       	Sheet listAll = book.createSheet();
       	{
	       	Row head = listAll.createRow(0);
	        Cell pp = head.createCell(0);
	        pp.setCellStyle(cellStyle);
	        pp.setCellValue(Messages.getString("nameIDColon")); //$NON-NLS-1$
	        Cell personalAccount = head.createCell(1);
	        personalAccount.setCellStyle(cellStyle);
	        personalAccount.setCellValue(Messages.getString("namePersonalAccountColon")); //$NON-NLS-1$
	        Cell FullName = head.createCell(2);
	        FullName.setCellStyle(cellStyle);
	        FullName.setCellValue(Messages.getString("nameFullNameColon")); //$NON-NLS-1$
	        Cell payment = head.createCell(3);
	        payment.setCellStyle(cellStyle);
	        payment.setCellValue(Messages.getString("namePaymentColon")); //$NON-NLS-1$
	        Cell persent = head.createCell(4);
	        persent.setCellStyle(cellStyle);
	        persent.setCellValue(Messages.getString("namePersentColon")); //$NON-NLS-1$
	        Cell onCurrentAccount = head.createCell(5);
	        onCurrentAccount.setCellStyle(cellStyle);
	        onCurrentAccount.setCellValue(Messages.getString("nameOnCurrentAccountColon")); //$NON-NLS-1$
	        Cell datePayment = head.createCell(6);
	        datePayment.setCellStyle(cellStyle);
	        datePayment.setCellValue(Messages.getString("nameDatePaymentColon")); //$NON-NLS-1$
	        Cell paymentPeriod = head.createCell(7);
	        paymentPeriod.setCellStyle(cellStyle);
	        paymentPeriod.setCellValue(Messages.getString("namePaymentPeriodColon")); //$NON-NLS-1$
	        Cell paymentOrderNumber = head.createCell(8);
	        paymentOrderNumber.setCellStyle(cellStyle);
	        paymentOrderNumber.setCellValue(Messages.getString("namePaymentOrderNumberColon")); //$NON-NLS-1$
	        Cell paymentOrderDate = head.createCell(9);
	        paymentOrderDate.setCellStyle(cellStyle);
	        paymentOrderDate.setCellValue(Messages.getString("namePaymentOrderDateColon")); //$NON-NLS-1$
       	}
       	{
	        int rowNumber = 1;
	        for(Document doc : documents) {
	        	for(Line line : doc.getLines()) {
		           	Row plat = listAll.createRow(rowNumber++);
		            Cell pp = plat.createCell(0);
		            pp.setCellStyle(cellStyle);
		            pp.setCellValue(rowNumber-1);
		            Cell personalAccount = plat.createCell(1);
		            personalAccount.setCellStyle(cellStyle);
		            personalAccount.setCellValue(line.getPersonalAccount());
		            Cell FullName = plat.createCell(2);
		            FullName.setCellStyle(cellStyle);
		            FullName.setCellValue(line.getFullName());
		            Cell payment = plat.createCell(3);
		            payment.setCellStyle(cellStyle);
		            payment.setCellValue(line.getPayment());
		            Cell persent = plat.createCell(4);
		            persent.setCellStyle(cellStyle);
		            persent.setCellValue(line.getPersent());
		            Cell onCurrentAccount = plat.createCell(5);
		            onCurrentAccount.setCellStyle(cellStyle);
		            onCurrentAccount.setCellValue(line.getOnCurrentAccount());
		            Cell datePayment = plat.createCell(6);
		            datePayment.setCellStyle(dateStyle);
		            datePayment.setCellValue(line.getDatePayment());
		            Cell paymentPeriod = plat.createCell(7);
		            paymentPeriod.setCellStyle(dateStyle);
		            paymentPeriod.setCellValue(line.getPaymentPeriod());
		            Cell paymentOrderNumber = plat.createCell(8);
		            paymentOrderNumber.setCellStyle(cellStyle);
		            paymentOrderNumber.setCellValue(doc.getPaymentOrderNumber());
		            Cell paymentOrderDate = plat.createCell(9);
		            paymentOrderDate.setCellStyle(dateStyle);
		            paymentOrderDate.setCellValue(doc.getPaymentOrderDate());
		        }
	        }
       	}
       	for(int i = 0; i < listAll.getPhysicalNumberOfRows(); i++)listAll.autoSizeColumn(i+1);
       	
       	Sheet listDate = book.createSheet();
     	{
	       	Row head = listDate.createRow(0);
	        Cell personalAccount = head.createCell(0);
	        personalAccount.setCellStyle(cellStyle);
	        personalAccount.setCellValue(Messages.getString("namePersonalAccountColon")); //$NON-NLS-1$
	        int cellNumber = 1;
	        for(String date : paymentPeriods) {
	        	Cell dateCell = head.createCell(cellNumber++);
	        	dateCell.setCellStyle(cellStyle);
	        	dateCell.setCellValue(date);
	        }
        	Cell sumCell = head.createCell(cellNumber++);
        	sumCell.setCellStyle(cellStyle);
        	sumCell.setCellValue("Сумма с Л/С");
     	}
     	{
	        int rowNumber = 1;
     		for(String account : personalAccounts) {
     			Row acCell = listDate.createRow(rowNumber++);
     			Cell name = acCell.createCell(0);
     			name.setCellStyle(cellStyle);
     			name.setCellValue(account);
    	        int cellNumber = 1;
     			double sum = 0;
     			for(String date : paymentPeriods) {
     				Cell dateCell = acCell.createCell(cellNumber++);
     				dateCell.setCellStyle(cellStyle);
     				double value = mapSum.get(date).get(account);
     				sum += value;
     				dateCell.setCellValue(value);
     			}
     			Cell sumCell = acCell.createCell(cellNumber++);
     			sumCell.setCellStyle(cellStyle);
				sumCell.setCellValue(sum);
     		}
     		{
	     		Row sumRow = listDate.createRow(rowNumber++);
	     		Cell nameSumCell = sumRow.createCell(0);
	        	nameSumCell.setCellStyle(cellStyle);
	        	nameSumCell.setCellValue("Сумма");
		        int cellNumber = 1;
	            for(String date : paymentPeriods) {
	         		Cell sumCell = sumRow.createCell(cellNumber++);
	            	sumCell.setCellStyle(cellStyle);
	     			double sum = 0;
	            	for(String account : personalAccounts) {
	            		sum +=  mapSum.get(date).get(account);
	            	}
	            	sumCell.setCellValue(sum);
	            }
	     	}
     		{
	     		Row noPayRow = listDate.createRow(rowNumber++);
	     		Cell nameNoPayCell = noPayRow.createCell(0);
	        	nameNoPayCell.setCellStyle(cellStyle);
	        	nameNoPayCell.setCellValue("не уплата");
		        int cellNumber = 1;
	            for(String date : paymentPeriods) {
	         		Cell sumCell = noPayRow.createCell(cellNumber++);
	            	sumCell.setCellStyle(cellStyle);
	     			List<String> noPayAccounts = new ArrayList<>();
	            	for(String account : personalAccounts) {
	            		if(mapSum.get(date).get(account) <= 0)noPayAccounts.add(account);
	            	}
	            	String res = noPayAccounts.toString();
	            	sumCell.setCellValue(noPayAccounts.size() + " " +res);
	            }
     		}
     	}
        
        book.write(new FileOutputStream(Messages.getString("nameExcelFile"))); //$NON-NLS-1$
        book.close();
        
		//*/
	}

	
	
}
