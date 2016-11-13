package info.iconmaster.adam.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.JPanel;

import info.iconmaster.adam.entity.Chunk;
import info.iconmaster.adam.entity.Entity;
import info.iconmaster.adam.entity.World;
import info.iconmaster.adam.util.ChunkCoord;
import info.iconmaster.adam.util.Vector3;

public class WorldPane extends JPanel {
	private static final long serialVersionUID = 8272220582389104958L;
	
	public static void drawWorldView(Graphics g, World world, Vector3 subject, int width, int height, int pxPerChunk) {
		double chunksWide = Math.ceil((double) width / (double) pxPerChunk)+2; if (chunksWide%2==0) chunksWide++;
		double chunksHigh = Math.ceil((double) height / (double) pxPerChunk)+2; if (chunksHigh%2==0) chunksHigh++;
		
		double realWidth = pxPerChunk*chunksWide;
		double realHeight = pxPerChunk*chunksHigh;
		
		double chunkCenterX = subject.x / (double) World.VEC3_TO_CHUNK_SCALE;
		double chunkCenterY = subject.z / (double) World.VEC3_TO_CHUNK_SCALE;
		
		double beginX = (width-realWidth)/2 - pxPerChunk*(chunkCenterX-Math.floor(chunkCenterX)) - pxPerChunk/2;
		double beginY = (height-realHeight)/2 - pxPerChunk*(chunkCenterY-Math.floor(chunkCenterY)) - pxPerChunk/2;
		
		for (int x = 0; x < chunksWide; x++) {
			for (int y = 0; y < chunksHigh; y++) {
				Chunk ch = world.getChunk(new ChunkCoord(((int)(chunkCenterX-chunksWide/2))+x, ((int)(chunkCenterY-chunksHigh/2))+y));
				
				int x1 = (int) (beginX+x*pxPerChunk);
				int y1 = (int) (beginY+y*pxPerChunk);
				int x2 = pxPerChunk;
				int y2 = pxPerChunk;
				
				g.setColor(new Color(Math.abs(ch.hashCode())%255, Math.abs(ch.hashCode())/255%255, Math.abs(ch.hashCode())/255/255%255));
				g.fillRect(x1, y1, x2, y2);
			}
		}
		
		g.setColor(Color.RED);
		for (int x = 0; x < chunksWide; x++) {
			for (int y = 0; y < chunksHigh; y++) {
				Chunk ch = world.getChunk(new ChunkCoord(((int)(chunkCenterX-chunksWide/2))+x, ((int)(chunkCenterY-chunksHigh/2))+y));
				
				for (Map.Entry<Entity, Vector3> entry : ch.entities.entrySet()) {
					double px = entry.getValue().x % World.VEC3_TO_CHUNK_SCALE / World.VEC3_TO_CHUNK_SCALE;
					double py = entry.getValue().z % World.VEC3_TO_CHUNK_SCALE / World.VEC3_TO_CHUNK_SCALE;
					double pw = entry.getKey().getSize().x / World.VEC3_TO_CHUNK_SCALE;
					double ph = entry.getKey().getSize().z / World.VEC3_TO_CHUNK_SCALE;
					
					int baseX = (int) (beginX+x*pxPerChunk)+pxPerChunk;
					int baseY = (int) (beginY+y*pxPerChunk)+pxPerChunk;
					
					int x2 = (int) (pw*pxPerChunk);
					int y2 = (int) (ph*pxPerChunk);
					int x1 = (int) (baseX+px*pxPerChunk-x2/2);
					int y1 = (int) (baseY+py*pxPerChunk-y2/2);
					
					g.drawRect(x1, y1, x2, y2);
				}
			}
		}
	}
	
	AdamGui adamGui;
	int viewDistance = 100;
	
	public WorldPane(AdamGui adamGui) {
		this.adamGui = adamGui;
		
		addMouseWheelListener((ev)->{
			viewDistance += ev.getUnitsToScroll()/ev.getScrollAmount();
			if (viewDistance <= 0) viewDistance = 1;
			
			repaint();
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (adamGui.game != null) {
			Chunk center = adamGui.game.player.getParentWithClass(Chunk.class);
			if (center == null) return;
			World world = center.world;
			
			drawWorldView(g, world, center.entities.get(adamGui.game.player), getWidth(), getHeight(), viewDistance);
		}
	}
}
