/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.glam.kml2geoj;

/**
 *
 * @author jblue1
 */
public class Feature {
	private String type = "Feature";
	
	private Properties properties;
	
	private class Properties {
		private Integer LOC_ID;
		private Integer L1ID;
		private String L1NAME;
		private Integer L2ID;
		private String L2NAME;
		private Integer L3ID;
		private String L3NAME;
		private Integer L4ID;
		private String L4NAME;
		
		public Properties(Integer LOC_ID, Integer L1ID, String L1NAME,
						Integer L2ID, String L2NAME, 
						Integer L3ID, String L3NAME, 
						Integer L4ID, String L4NAME) {
			this.LOC_ID = LOC_ID;
			this.L1ID = L1ID;
			this.L1NAME = L1NAME;
			this.L2ID = L2ID;
			this.L2NAME = L2NAME;
			this.L3ID = L3ID;
			this.L3NAME = L3NAME;
			this.L4ID = L4ID;
			this.L4NAME = L4NAME;
		}
	}
	private class Geometry {
		private String type = "MultiPolygon";
		
		private Polygon[][] coordinates;
	}
}
