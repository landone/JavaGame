package main;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import render.*;
import display.DisplayManager;

public class Main {
	
	public static void main(String[] args){
		
		DisplayManager.create("Test", 1200, 675, 100);
		MainRender render = new MainRender();
		
		float[] vertices = new float[]{
			-0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f,	
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f
		};
		
		float[] texCoords = new float[]{
				0, 0,
				0, 1,
				1, 1,
				1, 0
		};
		
		float[] normals = new float[]{
				0f, 0f, 1f,
				0f, 0f, 1f,
				0f, 0f, 1f,
				0f, 0f, 1f
		};
		
		int[] indices = new int[]{
				0, 1, 2,
				3, 0, 2
		};
		
		Mesh mesh = GLLoader.loadToVAO(vertices, texCoords, normals, indices, new MTexture(GLLoader.loadTexture("grass")));
		mesh.getTexture().setUseFakeLighting(true);
		Model model = new Model(mesh, new Vector3f(0,0,-5), 0f, 0f, 0f, 1f);
		
		while(!Display.isCloseRequested()){
			
			render.processModel(model);
			render.render();
			
			DisplayManager.update();
		}
		
		render.cleanUp();
		GLLoader.cleanUp();
		DisplayManager.close();
	}
}
