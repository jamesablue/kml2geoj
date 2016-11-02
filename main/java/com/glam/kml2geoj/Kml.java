/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.glam.kml2geoj;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import kml2geoj.XmlUtility.XmlUtility;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import com.google.gson.Gson;

/**
 *
 * @author jblue1
 */
public class Kml {
    /* Absolute filePath to origin file */
    private String filePath;
    private DocumentBuilderFactory factory;
    private MultiGeometry multigeometry;
    private HashMap<String, String> data;
    private Document doc;
		private ArrayList<Polygon> polygons;
		private String mainCentroid;
		private Polygon mainPolygon;

    /**
     * Takes in an absolute file path to kml file.
     * Builds Kml after parsing .kml file.
     * @param filePath
 * @throws org.xml.sax.SAXException
 * @throws java.io.IOException
 * @throws javax.xml.parsers.ParserConfigurationException
     */
    public Kml(String filePath) throws SAXException, IOException, ParserConfigurationException {
        File input = new File(filePath);
				this.filePath = filePath;
				this.data = new HashMap();
        this.doc = XmlUtility.parse(input);
				this.polygons = new ArrayList();
        
        buildKml();
    }
		public Kml(File file) throws ParserConfigurationException, SAXException, IOException {
			this.filePath = file.getAbsolutePath();
			this.data = new HashMap();
			this.doc = XmlUtility.parse(file);
			this.polygons = new ArrayList();
			
			buildKml();
		}
	
    private void buildKml() {
        Element head;
				NodeList simpleData;
        Node temp;
        NamedNodeMap attributes;
        Node name;
        String attributeName;
				String attributeNodeValue;
				
				// Set the data.
				simpleData = doc.getElementsByTagName("SimpleData");
				
				for (int i=0; i < simpleData.getLength(); i++) {
					// Get node.
					temp = simpleData.item(i);
					// Get data label.
					attributes = temp.getAttributes();
					name = attributes.getNamedItem("name");
					attributeName = name.getNodeValue();
					// Get data matching the label.
					attributeNodeValue = temp.getTextContent();
					// Add to data map.
					this.data.put(attributeName, attributeNodeValue);
				}
				
				// Set the polygon.
				NodeList coordinates = doc.getElementsByTagName("coordinates");
				String coordTextContent;
				Polygon p;
				for (int i=0; i < coordinates.getLength(); i++) {
					coordTextContent = coordinates.item(i).getTextContent();
					p = new Polygon(coordTextContent);
					this.polygons.add(p);
				}
				
				this.mainPolygon = findLargestPolygon();
				this.mainCentroid = mainPolygon.getCentroid();
    }
		
		public Polygon findLargestPolygon() {
			Polygon largest = null;
			double largestArea = -Double.MAX_VALUE;
			
			for (int i=0; i < polygons.size(); i++) {
				if (polygons.get(i).area > largestArea) {
					largest = polygons.get(i);
					largestArea = largest.area;
				}
			}
			
			return largest;
		}
		
		public String toGeoJSON() {
			StringBuilder sb = new StringBuilder();
			
			sb.append("\n{ \"type\": \"Feature\", \n" +
"  \"properties\": { \n");
			if (this.data.get("LOC_ID") == null) {
				sb.append("\"LOC_ID\":\"");sb.append(this.data.get("I_J"));sb.append("\"\n");
			} else {
				sb.append("\"LOC_ID\":");sb.append(this.data.get("LOC_ID"));sb.append(",\n");
				sb.append("\"L1ID\":");sb.append(this.data.get("L1ID"));sb.append(",\n");
				sb.append("\"L1NAME\":\"");sb.append(this.data.get("L1NAME"));sb.append("\",\n");
				sb.append("\"L2ID\":");sb.append(this.data.get("L2ID"));sb.append(",\n");
				sb.append("\"L2NAME\":\"");sb.append(this.data.get("L2NAME"));sb.append("\",\n");
				sb.append("\"L3ID\":");sb.append(this.data.get("L3ID"));sb.append(",\n");
				sb.append("\"L3NAME\":\"");sb.append(this.data.get("L3NAME"));sb.append("\",\n");
				sb.append("\"L4ID\":");sb.append(this.data.get("L4ID"));sb.append(",\n");
				sb.append("\"L4NAME\":\"");sb.append(this.data.get("L4NAME"));sb.append("\",\n");
				sb.append("\"Centroid\":\"");sb.append(this.mainCentroid);sb.append("\"\n");
			}
			
			
			sb.append("},\n");
			sb.append("  \"geometry\": {\n" +
"    \"type\": \"MultiPolygon\", \n" +
"    \"coordinates\": [");
			
			// -1 so we don't have an extra ,.
			for (int i=0; i < this.polygons.size() -1; i++) {
				sb.append("[");
				sb.append(this.polygons.get(i).toGeoJSON());
				sb.append("],");
			}
			sb.append("[");
			sb.append(this.polygons.get(polygons.size()-1).toGeoJSON());
			sb.append("]]\n}}\n");
			
			
			return sb.toString();
		}
		
}
