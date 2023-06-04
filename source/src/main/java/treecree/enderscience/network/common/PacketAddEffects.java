package treecree.enderscience.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketAddEffects implements IMessage {
	public static final int SOUND = 1;
	public static final int PARTICLES = 2;

	public static final int EFFECT_TELEPORT = 1;
	public static final int EFFECT_PARTICLES = 2;
	public static final int EFFECT_SOUND_EVENT = 100;

	public int effectType;
	public int flags;
	public float x;
	public float y;
	public float z;
	public int particleCount;
	public double offset;
	public double velocity;

	public int soundEventId;
	public float pitch;
	public float volume;
	public boolean repeat;

	public PacketAddEffects() {
	}

	public PacketAddEffects(int id, int flags, double x, double y, double z) {
		this(id, flags, x, y, z, 32, 0.2f, 2.0f);
	}

	public PacketAddEffects(int id, int flags, double x, double y, double z, int particleCount) {
		this(id, flags, x, y, z, particleCount, 0.2f, 2.0f);
	}

	public PacketAddEffects(int id, int flags, double x, double y, double z, int particleCount, float offset,
			float velocity) {
		this.effectType = id;
		this.flags = flags;
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.particleCount = particleCount;
		this.offset = offset;
		this.velocity = velocity;
	}

	public PacketAddEffects(int soundId, float pitch, float volume, boolean repeat, boolean stop, float x, float y,
			float z) {
		this.effectType = EFFECT_SOUND_EVENT;
		this.soundEventId = soundId;
		this.flags = stop ? 1 : 0;
		this.pitch = pitch;
		this.volume = volume;
		this.repeat = repeat;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.effectType);
		buf.writeByte(this.flags);
		buf.writeFloat((float) this.x);
		buf.writeFloat((float) this.y);
		buf.writeFloat((float) this.z);

		if (this.effectType == EFFECT_SOUND_EVENT) {
			buf.writeShort((short) this.soundEventId);
			buf.writeFloat(this.pitch);
			buf.writeFloat(this.volume);
			buf.writeBoolean(this.repeat);
		} else {
			buf.writeShort(this.particleCount);
			buf.writeFloat((float) this.offset);
			buf.writeFloat((float) this.velocity);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.effectType = buf.readByte();
		this.flags = buf.readByte();
		this.x = buf.readFloat();
		this.y = buf.readFloat();
		this.z = buf.readFloat();

		if (this.effectType == EFFECT_SOUND_EVENT) {
			this.soundEventId = buf.readShort();
			this.pitch = buf.readFloat();
			this.volume = buf.readFloat();
			this.repeat = buf.readBoolean();
		} else {
			this.particleCount = buf.readShort();
			this.offset = buf.readFloat();
			this.velocity = buf.readFloat();
		}
	}
}
