/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.glam.kml2geoj;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This is a class to sort out the options for interpreting the command line.
 * @author jblue1
 */
public class Command {
	public boolean quietmode;
	public String name;
	public String outputPath;
	
	public void run(String[] args) {
		
		readCommands(args);
		
		String dirName = this.name;
		String outputPath = this.outputPath;
		try {
			//kml = new Kml("C:\\Users\\jblue1\\Documents\\NetBeansProjects\\kml2geoj\\src\\main\\java\\com\\glam\\kml2geoj\\maryland.kml");

			String begining = beginGeoJSON();
			ArrayList<Kml> aryKml = readDirectory(outputPath);
			//aryKml.add(kml);

			//Write to file because copying is just too large.
			//Path output = Paths.get("L1.json");
			//Files.write(output, Arrays.asList(toGeoJSONString(begining, aryKml)), Charset.forName("UTF-8"));

			PrintWriter writer = new PrintWriter(outputPath + "\\"+dirName+".json", "UTF-8");
			writer.println(toGeoJSONString(begining, aryKml));
			writer.close();

			System.out.println(toGeoJSONString(begining, aryKml));

		} catch (SAXException | IOException | ParserConfigurationException ex) {
			Logger.getLogger(Kml2geoj.class.getName()).log(Level.SEVERE, null, ex);
		}
			
			
			
//			Gson gson = new Gson();
//			try {
//				gson.toJson(kml, new FileWriter("C:\\Users\\jblue1\\Documents\\kml.json"));
//			} catch (IOException ex) {
//				Logger.getLogger(Kml2geoj.class.getName()).log(Level.SEVERE, null, ex);
//			}
	}
	
		/*
		Method to parse and understand the commandline arguments;
		Finds directory name by splitting filepath depending on user's os.
		*/
		public void readCommands(String[] args) {
			String os = System.getProperty("os.name");
			String[] filePath;
			
			this.outputPath = args[0];
			if (os.contains("Windows")) {
				filePath = this.outputPath.split("\\\\");
			} else {
				filePath = this.outputPath.split("/");
			}
			this.name = filePath[filePath.length - 1];
		}
	
	public static String toGeoJSONString(String begining, ArrayList<Kml> kmls) {
			StringBuilder sb = new StringBuilder();
			sb.append(begining);
			
			for (int i=0; i < kmls.size()-1; i++) {
				sb.append(kmls.get(i).toGeoJSON());
				sb.append(",");
			}
			sb.append(kmls.get(kmls.size()-1).toGeoJSON());
			
			sb.append("]\n}");
			
			return sb.toString();
		}
		
		public static String beginGeoJSON() {
			StringBuilder sb = new StringBuilder();
			sb.append("{\n" +
"\"type\": \"FeatureCollection\",\n" +
"\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \"urn:ogc:def:crs:OGC:1.3:CRS84\" } },\n\n" +
"\"features\": [");
			
			return sb.toString();
		}
    
		public static ArrayList<Kml> readDirectory(String Path) throws ParserConfigurationException, SAXException, IOException {
			ArrayList<Kml> toReturn = new ArrayList();
			File d = new File(Path);
			File[] filesInD = d.listFiles();
			
			for (int i=0; i < filesInD.length; i++) {
				toReturn.add(new Kml(filesInD[i]));
			}
			
			return toReturn;
		}
}
