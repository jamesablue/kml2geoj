/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.glam.kml2geoj;

import java.util.ArrayList;

/**
 *
 * @author jblue1
 */
public class Polygon {
	ArrayList<Double> longitudes;
	double minLon;
	ArrayList<Double> latitudes;
	double minLat;
	private int size;
	double area;
	String centroid;
	
	/**
	 * coordinates should be a string in the form of:
	 * -75.90417,37.89972 -75.89778,37.89722 -75.90083,37.89222...
	 * positive or negative floating points, separated by ,s and pairs 
	 * separated by spaces.
	 * @param coordinates 
	 */
	public Polygon(String coordinates) {
		this.longitudes = new ArrayList();
		this.minLon = Double.MAX_VALUE;
		this.latitudes = new ArrayList();
		this.minLat = Double.MAX_VALUE;
		
		String[] pairs;
		String[] pair;
		Double lat, lon;
		
		pairs = coordinates.split(" ");
		
		this.size = pairs.length;
		
		for (int i=0; i < pairs.length; i++) {
			pair = pairs[i].split(",");
			
			lon = Double.parseDouble(pair[0]);
			lat = Double.parseDouble(pair[1]);
			
			// We find the minimum coordinates so that we can calculate the area of the polygon.
			if (lon < minLon)
				minLon = lon;
			if (lat < minLat)
				minLat = lat;
			
			longitudes.add(lon);
			latitudes.add(lat);
		}
		
		
		buildCentroid();
	}
	
	public void buildCentroid() {
		ArrayList<Double> x = longitudes;
		ArrayList<Double> y = latitudes;
		double summationx = 0.0; double summationy = 0.0;
		Double cX,cY;
		double tempx = 0.0; double tempy = 0.0;
		double rightSide;
		double area;
		int size = x.size();

		for (int i=0; i < size-1; i++) {
			rightSide = (x.get(i)*y.get(i+1) - x.get(i+1)*y.get(i));
			
			tempx = ( (x.get(i)+x.get(i+1)) * rightSide );
			summationx += tempx;
			
			tempy = ( (y.get(i)+y.get(i+1)) * rightSide );
			summationy += tempy;
		}
		rightSide = (x.get(size-1)*y.get(0) - x.get(0)*y.get(size-1));
			
		tempx = ( (x.get(size-1)+x.get(0)) * rightSide );
		summationx += tempx;

		tempy = ( (y.get(size-1)+y.get(0)) * rightSide );
		summationy += tempy;
		
		
		area = this.getArea();
		
		cX = 1/(6 * area) * summationx;
		cY = 1/(6 * area) * summationy;
		
		System.out.println("cX: " + cX.toString() + ", cY: " + cY.toString());
	}

	private double getArea() {
		double summation = 0.0;
		double temp = 0.0;
		int size = longitudes.size();
		for (int i=0; i < size-1 ; i++) {
			// (xi*yi+1 - xi+1yi)
			temp = (longitudes.get(i)*latitudes.get(i+1) - longitudes.get(i+1)*latitudes.get(i));
			summation += temp;
			//System.out.println(longitudes.get(i)+"*"+latitudes.get(i+1)+"-"+longitudes.get(i+1)+"*"+latitudes.get(i)+"="+temp);
			System.out.println(longitudes.get(i)*latitudes.get(i+1)+"-"+longitudes.get(i+1)*latitudes.get(i)+"="+temp);
		}
		temp = ((longitudes.get(size-1)*latitudes.get(0)) - (longitudes.get(0)*latitudes.get(size-1)));
		summation += temp;
		//System.out.println(longitudes.get(size-1)+"*"+latitudes.get(0)+"-"+longitudes.get(0)+"*"+latitudes.get(size-1)+"="+temp);
		System.out.println(longitudes.get(size-1)*latitudes.get(0)+"-"+longitudes.get(0)*latitudes.get(size-1)+"="+temp);
		
		System.out.println(summation);
		
		this.area = 0.5 * Math.abs(summation);
		return 0.5 * summation;
	}
	
	/**
	 * Function to normalize the negative lon and lat relative to the minimum.
	 * @param d
	 * @return 
	 */
	public double normalize(double d) {
		
		
		return 0.0;
	}
	
	public String toGeoJSON() {
		StringBuilder sb = new StringBuilder();
		
		if (longitudes == null || latitudes == null) {
			return null;
		}
		
		sb.append("[");
		
		// -1 because the last coordinate in the array can't have a ,.
		for (int i=0; i < (this.size -1); i++) {
			sb.append("[");
			sb.append(longitudes.get(i).toString());
			sb.append(",");
			sb.append(latitudes.get(i).toString());
			sb.append("],");
		}
		sb.append("[");
		sb.append(longitudes.get(this.size-1).toString());
		sb.append(",");
		sb.append(latitudes.get(this.size-1).toString());
		sb.append("]");
		
		sb.append("]");
		
		return sb.toString();
	}


	public String getCentroid() {
		return this.centroid;
	}
}
