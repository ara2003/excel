package excel.builder210.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public strictfp class Document {
	
	private final Date date;
	private final int id;
	private final List<Line> pays;
	
	public Document(Document document) {
		this.id = document.id;
		this.date = document.date;
		this.pays = document.pays;
	}
	
	public Document(String head, List<String> pays) {
		this.pays = new ArrayList<>(pays.size());
		for(String line : pays) {
			this.pays.add(new Line(Main.split(line)));
		}
		String[] colons = Main.split(head);
		date = DateParser.parse(colons[10]);
		id = Integer.parseInt(colons[9]);
	}
	
	public Document(File file) throws IOException {
		this.pays = new ArrayList<>();
		InputStream reader = new FileInputStream(file);
		String encoding = System.getProperty("console.encoding", "cp1251");
		Scanner scan = new Scanner(reader, encoding);
    	String[] colons = Main.split(scan.nextLine());
		this.date = DateParser.parse(colons[10]);
		this.id = Integer.parseInt(colons[9]);
		while(scan.hasNext()) {
			pays.add(new Line(Main.split(scan.nextLine())));
    	}
    	reader.close();
    	scan.close();
	}
	
	public strictfp class Line {

		private final String personalAccount, fullName, paymentPeriod;
		private final double payment, onCurrentAccount;
		private final Date datePayment;

		public Line(String[] line) {
			if(line[2].charAt(0)=='0')line[2] = line[2].substring(1);
			if(line[2].charAt(0)=='0')line[2] = line[2].substring(1);
			personalAccount = line[2];
			fullName = line[3];
			paymentPeriod = line[5];
			double p1 = Double.parseDouble(line[6]);
			if(p1 > 100) p1 = Math.round(p1/100)/100.;
			payment = p1;
			double p2 = Double.parseDouble(line[8]);
			if(p2 > 100) p2 = Math.round(p2/100)/100.;
			onCurrentAccount = p2;
			datePayment = DateParser.parse(line[9]);
		}

		public String getPersonalAccount() {
			return personalAccount;
		}

		public String getFullName() {
			return fullName;
		}

		public double getPayment() {
			return payment;
		}

		public double getPersent() {
			return getPayment() - getOnCurrentAccount();
		}

		public double getOnCurrentAccount() {
			return onCurrentAccount;
		}

		public String getPaymentPeriod() {
			return paymentPeriod;
		}

		public Date getDatePayment() {
			return datePayment;
		}

	}

	public List<Line> getLines() {
		return pays;
	}

	public Date getPaymentOrderDate() {
		return date;
	}

	public int getPaymentOrderNumber() {
		return id;
	}
}
