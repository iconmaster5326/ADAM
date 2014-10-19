package com.iconmaster.adam.fight;

import com.iconmaster.adam.body.BodyPart;

/**
 *
 * @author iconmaster
 */
public class IsAliveResult {
	public BodyPart part;
	public boolean bloodLoss;
	public boolean destroyed;

	public IsAliveResult(BodyPart part, boolean bloodLoss, boolean destroyed) {
		this.part = part;
		this.bloodLoss = bloodLoss;
		this.destroyed = destroyed;
	}
}