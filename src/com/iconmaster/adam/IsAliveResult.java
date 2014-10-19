package com.iconmaster.adam;

/**
 *
 * @author iconmaster
 */
public class IsAliveResult {
	public BodyPart part;
	public boolean bloodLoss;

	public IsAliveResult(BodyPart part, boolean bloodLoss) {
		this.part = part;
		this.bloodLoss = bloodLoss;
	}
}
