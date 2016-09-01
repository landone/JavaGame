package render;

public class Mesh {
	
	private int vaoID, vertexCount;
	private MTexture texture;
	
	public Mesh(int vaoID, int vertexCount, MTexture texture){
		this.texture = texture;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public MTexture getTexture(){
		return texture;
	}
	
}
