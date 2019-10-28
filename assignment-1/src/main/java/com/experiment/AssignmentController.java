/**
 * 
 */
package com.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.opencsv.CSVWriter;

/**
 * @author Ameet K
 *
 */

@Component
public class AssignmentController {

	@Autowired
	PropertiesControl propCont;

	@Autowired
	JavaMailSender sender;
	org.slf4j.Logger logr = org.slf4j.LoggerFactory.getLogger(AssignmentController.class);

	@Scheduled(cron = "0 0 16 * * ?")

	@Scheduled(cron = "0 0 12 * * ?")

	@Scheduled(cron = "0 0 08 * * ?")

	// @Scheduled(fixedRate = 1000)
	public void performExperiment() {

		String[] currArray = propCont.getCurrArray();
		String urlPre = "https://eodhistoricaldata.com/api/real-time/";
		String token = propCont.getToken();
		String urlSuf = "?api_token=" + token + "&fmt=json";

		URL obj;
		HttpURLConnection con;
		BufferedReader in;

		String inputLine;
		StringBuffer response = new StringBuffer();

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd_hhmm");
		String fileName = "obsval_" + formatter.format(date);

		String fileNameWithPath = "src/" + fileName + ".csv";
		File file = createFile(fileNameWithPath);

		FileWriter outputfile;

		CSVWriter writer;

//For loop to go through the currencies required
		for (int i = 0; i < currArray.length; i++) {

			try {

				obj = new URL(urlPre + currArray[i] + urlSuf);
				con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", "Mozilla/10.0");

				try {
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				} catch (Exception e) {
					logr.trace("Exception Occured" + e.toString());

				}
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				} // while

				System.out.println(response.toString());
				JSONObject myResponse = new JSONObject(response.toString());
				try {
					outputfile = new FileWriter(file, true);
					writer = new CSVWriter(outputfile);

					// add data to csv
					String[] data1 = { myResponse.getString("code"), "" + myResponse.getDouble("close") };
					writer.writeNext(data1);
					// closing writer connection
					writer.close();

					// closing connection
					in.close();

				} catch (IOException e) {
					sendEmailException(e);
					logr.trace("Exception Occured" + e.toString());

				}

			} // try
			catch (Exception e) {
				System.out.println("Can not Access Data, See log for Excecption");
				sendEmailException(e);
				logr.trace("Exception Occured" + e.toString());
			} // catch

		} // for loop

		try {
			sendEmail(file);
		} catch (Exception e) {
			System.out.println("Can not send email with attachment. See log for Excecption");
			sendEmailException(e);
			logr.trace("Exception Occured" + e.toString());
		}

	}

	// Method to create CSV file

	public File createFile(String nameWithPath) {

		File file = new File(nameWithPath);
		// Create the file
		try {
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}

			// create FileWriter object with file as parameter
			FileWriter outputfile = new FileWriter(file);

			// create CSVWriter object filewriter object as parameter
			CSVWriter writer = new CSVWriter(outputfile);

			// adding header to csv
			String[] header = { "FOREX", "VALUE" };
			writer.writeNext(header);
			writer.close();

		} // try
		catch (IOException e) {
			System.out.println("Can not Create File ");
			sendEmailException(e);
			logr.trace("Exception Occured" + e.toString());

		} // catch

		return file;
	}

	// Method to send email
	private boolean sendEmail(File attachment) throws Exception {
		MimeMessage message = sender.createMimeMessage();

		// Enable the multipart flag!

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(propCont.getEmailTo());
		helper.setText("Please find the attached file for forex rates. \n Thanks");
		helper.setSubject("Forex Rates");

		helper.addAttachment(attachment.getName(), attachment);

		try {
			sender.send(message);
		} catch (Exception e) {
			logr.error("Exception Occured");
			sendEmailException(e);
			return false;
		} // catch

		return true;

	}// sendEmail

	// Method to Send Exception Email
	private void sendEmailException(Exception ex) {

		try {
			MimeMessage message = sender.createMimeMessage();

			// Enable the multipart flag

			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(propCont.getEmailTo());
			helper.setText("Exception Occured. Please see details. \n" + ex.getLocalizedMessage());
			helper.setSubject("Exception Occured");

			sender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		} // catch

	}// sendEmailException

}
