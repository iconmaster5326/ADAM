package info.iconmaster.adam.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import info.iconmaster.adam.entity.Assembly.JointType;
import info.iconmaster.adam.game.AdamGame;
import info.iconmaster.adam.util.WorldCoord;

public interface ConcreteEntity extends Entity {
	public Color getColor();
	
	@Override
	public default void draw(AdamGame game, Graphics g, int x, int y, double pixPerUnit, JointType side) {
		Point size = drawSize(game, pixPerUnit, side);
		
		g.setColor(getColor());
		g.fillRect(x-size.x/2, y-size.y/2, size.x, size.y);
		
		g.setColor(Color.BLACK);
		g.drawRect(x-size.x/2, y-size.y/2, size.x, size.y);
	}
}
