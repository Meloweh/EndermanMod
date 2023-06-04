package treecree.enderscience.capabilities.player;

public class EnderScienceCap implements IEnderScienceCap{

	private int flags;
	private int potionDuration;
	private byte potionID;
	
	@Override
	public int getFlags() {
		return flags;
	}

	@Override
	public void setFlags(int flags) {
		this.flags = flags;
	}

	@Override
	public int getPotionDuration() {
		return potionDuration;
	}
	
	@Override
	public void setPotionDuration(int duration) {
		this.potionDuration = duration;
	}
}
