package info.iconmaster.adam.entity.base;

import static org.junit.Assert.*;

import org.junit.Test;

import info.iconmaster.adam.entity.Assembly;
import info.iconmaster.adam.util.Vector3;

public class Tests {

	@Test
	public void test1() {
		BasicEntity e1 = new BasicEntity("one", new Vector3(1, 2, 3), 5);
		BasicEntity e2 = new BasicEntity("two", new Vector3(10, 20, 30), 50);
		BasicEntity e3 = new BasicEntity("three", new Vector3(100, 200, 300), 500);
		
		BasicAssembly ass = new BasicAssembly("assembly");
		ass.parts.add(e1); ass.parts.add(e2); ass.parts.add(e3);
		
		ass.joints.add(new Assembly.Joint(e1, Assembly.JointType.FRONT, e2));
		ass.joints.add(new Assembly.Joint(e2, Assembly.JointType.TOP, e3));
		
		BasicWorld world = new BasicWorld("Earth", 5000, 1000);
		world.entities().put(ass, new Vector3(10001, 20002, 30003));
		
		System.out.println(world.entities.get(ass));
	}
}
