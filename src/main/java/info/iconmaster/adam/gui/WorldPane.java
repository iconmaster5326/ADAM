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
	
	AdamGui adamGui;
	int viewDistance = 5;
	
	public WorldPane(AdamGui adamGui) {
		this.adamGui = adamGui;
		
		addMouseWheelListener((ev)->{
			viewDistance += ev.getUnitsToScroll()/ev.getScrollAmount();
			
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
			
			int xinc = getWidth()/viewDistance;
			int yinc = getHeight()/viewDistance;
			
			for (int x = 0; x < viewDistance; x++) {
				for (int y = 0; y < viewDistance; y++) {
					Chunk ch = world.getChunk(new ChunkCoord(center.pos.x+x-viewDistance/2, center.pos.y+y-viewDistance/2));
					
					g.setColor(new Color(0, x*50 % 255, y*50 % 255));
					g.fillRect(x*xinc, y*yinc, xinc, yinc);
				}
			}
			
			g.setColor(Color.RED);
			for (int x = 0; x < viewDistance; x++) {
				for (int y = 0; y < viewDistance; y++) {
					Chunk ch = world.getChunk(new ChunkCoord(center.pos.x+x-viewDistance/2, center.pos.y+y-viewDistance/2));
					
					for (Map.Entry<Entity, Vector3> entry : ch.entities.entrySet()) {
						double px = entry.getValue().x % World.VEC3_TO_CHUNK_SCALE / World.VEC3_TO_CHUNK_SCALE;
						double py = entry.getValue().z % World.VEC3_TO_CHUNK_SCALE / World.VEC3_TO_CHUNK_SCALE;
						double pw = entry.getKey().getSize().x / World.VEC3_TO_CHUNK_SCALE;
						double ph = entry.getKey().getSize().z / World.VEC3_TO_CHUNK_SCALE;
						
						g.drawRect((int) (x*xinc+px*xinc), (int) (y*yinc+py*yinc), (int) (pw*xinc), (int) (ph*yinc));
					}
				}
			}
		}
	}
}
