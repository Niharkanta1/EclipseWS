package com.overwolf;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import com.overwolf.util.DrawUtils;

public final class Main {
	
	public static void main(String... args) {	
		System.out.println("Starting overwolf....");
		Locale.setDefault(new Locale("en", "US"));
		try {
			boolean demop = false;
			for (String arg : args) {
				if (arg.equalsIgnoreCase("-nooverlay") || arg.equalsIgnoreCase("-disableoverlay"))
					DrawUtils.enableOverlay = false;
				if (arg.equalsIgnoreCase("-demop"))
					demop = true;
			}
			
			//DrawUtils.enableOverlay = false;
			if (!DrawUtils.enableOverlay)
				System.out.println("Disabling overlay!");

			if(demop) {
				System.out.println("demop : true");
				System.exit(0);				
			}

			Engine engine = new Engine();
			engine.init(args);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("Closing Overwolf...");
		}
	}
}
