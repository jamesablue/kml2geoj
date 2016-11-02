/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.glam.kml2geoj;

/**
 *
 * @author James
 */
public class Kml2geoj {
	public boolean quietmode;
	public String name;
	public static String outputPath;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
			Command cmd = new Command();
			
			cmd.run(args);
    }
}
