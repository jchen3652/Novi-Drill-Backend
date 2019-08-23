/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import graphics.ImagePanel;
import interpreter.Dot;
import interpreter.SetCard;
import writer.PDFGenerator;

@Controller
@SpringBootApplication
public class Main {

	private static SetCard card = null;
	static ArrayList<String> performers = new ArrayList<String>();
	private static String setCardText;
	private static final String movement2Location = "./Novi2019M2Coords.pdf";

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);

		builder.headless(false);

		ConfigurableApplicationContext context = builder.run(args);

		// SpringApplication.run(Main.class, args);
	}

	@RequestMapping("/")
	String index() {
		return "index";
	}

	// @GetMapping("get-location")
	// String getLocation() {
	// // URL location =
	// // Main.class.getProtectionDomain().getCodeSource().getLocation();
	// // return location.getFile();
	//
	// // File currentDirectory = new File(new File(".").getAbsolutePath());
	// // System.out.println(currentDirectory.getCanonicalPath());
	// // return this.getClass().getClassLoader().getResource("").getPath();
	//
	// }

	@RequestMapping(value = "/getpdf", method = RequestMethod.GET)
	public void generateReport(HttpServletResponse response) throws Exception {

		File file = null;

		try (InputStream in = URI.create("https://rocky-dawn-70703.herokuapp.com/Novi2019M2Coords.pdf").toURL()
				.openStream()) {
			Files.copy(in, Paths.get("test"));

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		
		file = Paths.get("Novi2019M2Coords.pdf").toFile();

		File toReturn = null;

		card = new SetCard(file);
		setCardText = card.toString();

		String performer = "C1";

		System.out.println("Generation for " + performer + " is beginning");
		System.out.println("Getting performer information");
		ArrayList<Dot> dots = card.getPerformer(performer);
		// for (int i = 0; i < dots.size(); i++) {
		// System.out.println(dots.get(i).getXYCoords()[0] + "," +
		// dots.get(i).getXYCoords()[1]);
		// System.out.print(i != 0 && dots.get(i).getXYCoords().equals(dots.get(i -
		// 1).getXYCoords()));
		// }

		System.out.println("Generating images");
		ArrayList<Dot> dispDots = new ArrayList<>();

		for (int i = 0; i < dots.size(); i++) {
			dispDots.clear();
			if (i != 0) {
				dispDots.add(dots.get(i - 1));
			}
			dispDots.add(dots.get(i));

			try {

				ImagePanel.createAndShowGui(dispDots);
			} catch (Exception e) {

			}
			// ImageHandler.saveImage("Set" + dots.get(i).getSetNumber(),
			// ImagePanel.getInstance());
		}

		System.out.println("Beginning PDF generation");
		try {
			toReturn = PDFGenerator.generatePDF(dots, performer + "DrillSheet", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Generation for " + performer + " ended");

		byte[] data = readFully(new FileInputStream(toReturn));

		streamReport(response, data, "my_report.pdf");

	}

	@RequestMapping(value = { "/activate/{key}" }, method = RequestMethod.GET)
	public @ResponseBody String activate(@PathVariable(value = "key") String key) {

		return key;
	}

	@RequestMapping("/db")
	String db(Map<String, Object> model) {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
			stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
			ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

			ArrayList<String> output = new ArrayList<String>();
			while (rs.next()) {
				output.add("Read from DB: " + rs.getTimestamp("tick"));
			}

			model.put("records", output);
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	@Bean
	public DataSource dataSource() throws SQLException {
		if (dbUrl == null || dbUrl.isEmpty()) {
			return new HikariDataSource();
		} else {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(dbUrl);
			return new HikariDataSource(config);
		}
	}

	protected void streamReport(HttpServletResponse response, byte[] data, String name) throws IOException {

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + name);
		response.setContentLength(data.length);

		response.getOutputStream().write(data);
		response.getOutputStream().flush();
	}

	public static byte[] readFully(InputStream stream) throws IOException {
		byte[] buffer = new byte[8192];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int bytesRead;
		while ((bytesRead = stream.read(buffer)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		return baos.toByteArray();
	}

	// public static byte[] loadFile(String sourcePath) throws IOException {
	// InputStream inputStream = null;
	// try {
	// inputStream = new FileInputStream(sourcePath);
	// return readFully(inputStream);
	// } finally {
	// if (inputStream != null) {
	// inputStream.close();
	// }
	// }
	// }

}
