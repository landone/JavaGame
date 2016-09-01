package display;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static int WIDTH, HEIGHT, FPS_CAP;
	private static long lastFrameTime;
	private static float timeSinceFrame;
	
	public static void create(String title, int width, int height, int fps){
		WIDTH = width;
		HEIGHT = height;
		FPS_CAP = fps;
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(title);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		timeSinceFrame = 0;
	}
	
	public static void update(){
		Display.sync(FPS_CAP);
		Display.update();
		long lastTime = getCurrentTime();
		timeSinceFrame = (lastTime - lastFrameTime)/1000f;
		lastFrameTime = lastTime;
	}
	
	public static void setTitle(String title){
		Display.setTitle(title);
	}
	
	public static void setFPS(int fps){
		FPS_CAP = fps;
	}
	
	public static void close(){
		Display.destroy();
	}
	
	public static float getFrameTime(){//Time in seconds
		return timeSinceFrame;
	}
	
	public static long getCurrentTime(){//Time in milliseconds
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
}
