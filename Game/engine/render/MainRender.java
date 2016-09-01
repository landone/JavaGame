package render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import toolbox.Maths;

public class MainRender {
	
	private static final float FOV = 70, NEAR_PLANE = 0.1f, FAR_PLANE = 1000f;
	private static final float RED = 0.5f, GREEN = 0.5f, BLUE = 0.5f;
	
	private Matrix4f projectionMatrix;
	
	private DefaultShader shader = new DefaultShader();
	
	private Map<Mesh, List<Model>> entities = new HashMap<Mesh,List<Model>>();
	
	public MainRender(){
		enableCulling();
		createProjectionMatrix();
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(){
		prepare();
		shader.start();
		shader.loadSkyColor(RED, GREEN, BLUE);
		//shader.loadLight(sun);
		//shader.loadViewMatrix(camera);
		render(entities);
		shader.stop();
		
		entities.clear();
	}
	
	private void render(Map<Mesh, List<Model>> entities){
		for(Mesh model : entities.keySet()){
			prepareMesh(model);
			List<Model> batch = entities.get(model);
			for(Model entity :  batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareMesh(Mesh model){
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		MTexture texture = model.getTexture();
		if(texture.isHasTransparency()){
			MainRender.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	private void prepareInstance(Model entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(),
				entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void unbindTexturedModel(){
		MainRender.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	public void processModel(Model entity){
		Mesh entModel = entity.getMesh();
		List<Model> batch = entities.get(entModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Model> newBatch = new ArrayList<Model>();
			newBatch.add(entity);
			entities.put(entModel, newBatch);
		}
	}
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	private void createProjectionMatrix(){
		float aspectRatio = (float)Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);
		projectionMatrix.m33 = 0;
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
	
}
