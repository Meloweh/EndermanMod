package treecree.enderscience.capabilities.player;

public final class CapabilityFlags {

	public static final int INTEGER_BITS_IN_SUM = 32;

	public static final int getPartitionWithoutShell(final int mask, final int partitionBegin, final int partitionEnd) {

		final int leftBits = INTEGER_BITS_IN_SUM - partitionEnd;
		final int rightBits = (partitionBegin - 1 > 0) ? partitionBegin - 1 : 0;

		final int clearedToPartitionEnd = (mask << leftBits) >>> leftBits;
		final int clearedToPartitionBegin = (mask >>> rightBits) << rightBits;

		final int infixStrip = clearedToPartitionEnd & clearedToPartitionBegin;

		return infixStrip;
	}
	
	public static final int setPartitionWithoutShell(final int mask, final int partitionBegin, final int partitionEnd, final int value) {

		final int leftBits = INTEGER_BITS_IN_SUM - partitionEnd;
		final int rightBits = (partitionBegin - 1 > 0) ? partitionBegin - 1 : 0;

		final int clearedToPartitionBegin = ((mask >>> rightBits) + value) << rightBits;
		final int clearedToPartitionEnd = (mask << leftBits) >>> leftBits;

		final int infixStrip = clearedToPartitionEnd & clearedToPartitionBegin;

		return infixStrip;
	}

	public static final int getShellWithoutPartition(final int mask, final int partitionBegin, final int partitionEnd) {
		return mask - getPartitionWithoutShell(mask, partitionBegin, partitionEnd);
	}

	// 0 bits spend

	// Button press/release handle
	public static final int FLAG_BTN_ALL_EMPTY = 0;
	public static final int FLAG_BTN_PRESSED = 1;
	public static final int FLAG_BTN_RELEASED = 2;
	public static final int FLAG_SERVER_NO_ACTION = 3;

	// 3 bit wide mask therefore define length
	public static final int PARTITION_BTN_BEGIN = 0;
	public static final int PARTITION_BTN_END = 2;

	// button never pressed flag
	public static final int setFlagBtnNone(final int mask) {
		return getShellWithoutPartition(mask, PARTITION_BTN_BEGIN, PARTITION_BTN_END);
	}

	public static final boolean isFlagBtnNone(final int mask) {
		return mask == getShellWithoutPartition(mask, PARTITION_BTN_BEGIN, PARTITION_BTN_END);
	}

	// button currently pressing flag
	public static final int setFlagBtnPressing(final int mask) {
		return getShellWithoutPartition(mask, PARTITION_BTN_BEGIN, PARTITION_BTN_END) + FLAG_BTN_PRESSED;
	}

	public static final boolean isFlagBtnPressing(final int mask) {
		return mask == getShellWithoutPartition(mask, PARTITION_BTN_BEGIN, PARTITION_BTN_END) + FLAG_BTN_PRESSED;
	}

	// button just released flag
	public static final int setFlagBtnReleased(final int mask) {
		return getShellWithoutPartition(mask, PARTITION_BTN_BEGIN, PARTITION_BTN_END) + FLAG_BTN_RELEASED;
	}

	public static final boolean isFlagBtnReleased(final int mask) {
		return mask == getShellWithoutPartition(mask, PARTITION_BTN_BEGIN, PARTITION_BTN_END) + FLAG_BTN_RELEASED;
	}

	// temp value in server to stop it from sending packages to client
	public static final int setFlagBtnNoAction(final int mask) {
		return getShellWithoutPartition(mask, PARTITION_BTN_BEGIN, PARTITION_BTN_END) + FLAG_SERVER_NO_ACTION;
	}

	public static final boolean isFlagBtnNoAction(final int mask) {
		return mask == getShellWithoutPartition(mask, PARTITION_BTN_BEGIN, PARTITION_BTN_END) + FLAG_SERVER_NO_ACTION;
	}

	// 2 bits spend

	//cooldown boolean for particle slowdown
	public static final int FLAG_HAS_PARTICLE_COOLDOWN_FALSE = 0;
	public static final int FLAG_HAS_PARTICLE_COOLDOWN_TRUE = 4;

	public static final int PARTITION_PARTICLE_COOLDOWN_BEGIN = 3;
	public static final int PARTITION_PARTICLE_COOLDOWN_END = 3;
	
	//set boolean to true for particle cooldown
	public static final int setFlagParticleCooldownTrue(final int mask) {
		return getShellWithoutPartition(mask, PARTITION_PARTICLE_COOLDOWN_BEGIN, PARTITION_PARTICLE_COOLDOWN_END) + FLAG_HAS_PARTICLE_COOLDOWN_TRUE;
	}
	
	//set boolean to false
	public static final int setFlagParticleCooldownFalse(final int mask) {
		return getShellWithoutPartition(mask, PARTITION_PARTICLE_COOLDOWN_BEGIN, PARTITION_PARTICLE_COOLDOWN_END);
	}
	
	//return whether the particle cooldown flag has been set to true
	public static final boolean getFlagParticleCooldownBoolean(final int mask) {
		return mask == getShellWithoutPartition(mask, PARTITION_PARTICLE_COOLDOWN_BEGIN, PARTITION_PARTICLE_COOLDOWN_END) + FLAG_HAS_PARTICLE_COOLDOWN_TRUE;
	}
	
	//3 bits spend
	
	public static final int FLAG_TELEPORT_EFFECTS_TRUE = 8;
	public static final int FLAG_TELEPORT_EFFECTS_FALSE = 0;
	
	public static final int FLAG_TELEPORT_EFFECTS_TRUE_BEGIN = 4;
	public static final int FLAG_TELEPORT_EFFECTS_TRUE_END = 4;
	
	public static final int setFlagTeleportEffectTrue(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TELEPORT_EFFECTS_TRUE_BEGIN, FLAG_TELEPORT_EFFECTS_TRUE_END) + FLAG_TELEPORT_EFFECTS_TRUE; 
	}
	
	public static final int setFlagTeleportEffectFalse(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TELEPORT_EFFECTS_TRUE_BEGIN, FLAG_TELEPORT_EFFECTS_TRUE_END); 
	}
	
	public static final boolean getFlagTeleportEffectBoolean(final int mask) {
		return mask == getShellWithoutPartition(mask, FLAG_TELEPORT_EFFECTS_TRUE_BEGIN, FLAG_TELEPORT_EFFECTS_TRUE_END) + FLAG_TELEPORT_EFFECTS_TRUE;
	}
	
	//4 bits spend
	
	public static final int FLAG_TARGET_TELEPORT_EFFECTS_TRUE = 16;
	public static final int FLAG_TARGET_TELEPORT_EFFECTS_FALSE = 0;
	
	public static final int FLAG_TARGET_TELEPORT_EFFECTS_TRUE_BEGIN = FLAG_TELEPORT_EFFECTS_TRUE_END + 1;
	public static final int FLAG_TARGET_TELEPORT_EFFECTS_TRUE_END = FLAG_TARGET_TELEPORT_EFFECTS_TRUE_BEGIN;
	
	public static final int setFlagTargetTeleportEffectTrue(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TELEPORT_EFFECTS_TRUE_BEGIN, FLAG_TELEPORT_EFFECTS_TRUE_END) + FLAG_TELEPORT_EFFECTS_TRUE; 
	}
	
	public static final int setFlagTargetTeleportEffectFalse(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TARGET_TELEPORT_EFFECTS_TRUE_BEGIN, FLAG_TARGET_TELEPORT_EFFECTS_TRUE_END); 
	}
	
	public static final boolean getFlagTargetTeleportEffectBoolean(final int mask) {
		return mask == getShellWithoutPartition(mask, FLAG_TARGET_TELEPORT_EFFECTS_TRUE_BEGIN, FLAG_TARGET_TELEPORT_EFFECTS_TRUE_END) + FLAG_TARGET_TELEPORT_EFFECTS_TRUE;
	}
	
	//5 bits spend
	
	public static final int FLAG_TO_PLAYER_TELEPORT_TRUE = 32;
	public static final int FLAG_TO_PLAYER_TELEPORT_FALSE = 0;
	
	public static final int FLAG_TO_PLAYER_TELEPORT_BEGIN = FLAG_TARGET_TELEPORT_EFFECTS_TRUE_END + 1;
	public static final int FLAG_TO_PLAYER_TELEPORT_END = FLAG_TO_PLAYER_TELEPORT_BEGIN;
	
	public static final int setFlagToPlayerTeleportTrue(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TO_PLAYER_TELEPORT_BEGIN, FLAG_TO_PLAYER_TELEPORT_END) + FLAG_TO_PLAYER_TELEPORT_TRUE;
	}
	
	public static final int setFlagToPlayerTeleportFalse(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TO_PLAYER_TELEPORT_BEGIN, FLAG_TO_PLAYER_TELEPORT_END);
	}
	
	public static final boolean getFlagToPlayerTeleportBoolean(final int mask) {
		return mask == getShellWithoutPartition(mask, FLAG_TO_PLAYER_TELEPORT_BEGIN, FLAG_TO_PLAYER_TELEPORT_END) + FLAG_TO_PLAYER_TELEPORT_TRUE;
	}
	
	//6 bits spend
	
	public static final int FLAG_TO_BED_TELEPORT_TRUE = 64;
	public static final int FLAG_TO_BED_TELEPORT_FALSE = 0;
	
	public static final int FLAG_TO_BED_TELEPORT_BEGIN = FLAG_TO_PLAYER_TELEPORT_END + 1;
	public static final int FLAG_TO_BED_TELEPORT_END = FLAG_TO_BED_TELEPORT_BEGIN;
	
	public static final int setFlagToBedTeleportTrue(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TO_BED_TELEPORT_BEGIN, FLAG_TO_BED_TELEPORT_END) + FLAG_TO_BED_TELEPORT_TRUE;
	}
	
	public static final int setFlagToBedTeleportFalse(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TO_BED_TELEPORT_BEGIN, FLAG_TO_BED_TELEPORT_END);
	}
	
	public static final boolean getFlagToBedTeleportBoolean(final int mask) {
		return mask == getShellWithoutPartition(mask, FLAG_TO_BED_TELEPORT_BEGIN, FLAG_TO_BED_TELEPORT_END) + FLAG_TO_BED_TELEPORT_TRUE;
	}
	
	//7 bits spend
	
	public static final int FLAG_DEFAULT_RANDOM_TELEPORT_TRUE = 128;
	public static final int FLAG_DEFAULT_RANDOM_TELEPORT_FALSE = 0;
	
	public static final int FLAG_DEFAULT_RANDOM_TELEPORT_BEGIN = FLAG_TO_BED_TELEPORT_END + 1;
	public static final int FLAG_DEFAULT_RANDOM_TELEPORT_END = FLAG_DEFAULT_RANDOM_TELEPORT_BEGIN;
	
	public static final int setFlagDefaultRandomTeleportTrue(final int mask) {
		return getShellWithoutPartition(mask, FLAG_DEFAULT_RANDOM_TELEPORT_BEGIN, FLAG_DEFAULT_RANDOM_TELEPORT_END) + FLAG_DEFAULT_RANDOM_TELEPORT_TRUE;
	}
	
	public static final int setFlagDefaultRandomTeleportFalse(final int mask) {
		return getShellWithoutPartition(mask, FLAG_DEFAULT_RANDOM_TELEPORT_BEGIN, FLAG_DEFAULT_RANDOM_TELEPORT_END);
	}
	
	public static final boolean getFlagDefaultRandomTeleportBoolean(final int mask) {
		return mask == getShellWithoutPartition(mask, FLAG_DEFAULT_RANDOM_TELEPORT_BEGIN, FLAG_DEFAULT_RANDOM_TELEPORT_END) + FLAG_DEFAULT_RANDOM_TELEPORT_TRUE;
	}
	
	//8 bits spend
	
	public static final int FLAG_TEST_TRUE = FLAG_DEFAULT_RANDOM_TELEPORT_TRUE << 1;
	public static final int FLAG_TEST_FALSE = 0;
	
	public static final int FLAG_TEST_BEGIN = FLAG_DEFAULT_RANDOM_TELEPORT_END + 1;
	public static final int FLAG_TEST_END = FLAG_TEST_BEGIN;
	
	public static final int setFlagTestTrue(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TEST_BEGIN, FLAG_TEST_END) + FLAG_TEST_TRUE;
	}
	
	public static final int setFlagTestFalse(final int mask) {
		return getShellWithoutPartition(mask, FLAG_TEST_BEGIN, FLAG_TEST_END);
	}
	
	public static final boolean getFlagTest(final int mask) {
		return mask == getShellWithoutPartition(mask, FLAG_TEST_BEGIN, FLAG_TEST_END) + FLAG_TEST_TRUE;
	}
	
	// 9 bits spend
	/*
	
	public static final int FLAG_MORPH_DURATION_BEGIN = FLAG_TEST_END + 1;
	public static final int FLAG_MORPH_DURATION_END = FLAG_MORPH_DURATION_BEGIN + 8;

	public static final int setFlagMorphDuration(final int mask, final int value) {
		return getShellWithoutPartition(mask, FLAG_MORPH_DURATION_BEGIN, FLAG_MORPH_DURATION_END) + setPartitionWithoutShell(mask, FLAG_MORPH_DURATION_BEGIN, FLAG_MORPH_DURATION_END, value);
	}
	
	public static final int getFlagMorphDuration(final int mask) {
		return getPartitionWithoutShell(mask, FLAG_MORPH_DURATION_BEGIN, FLAG_MORPH_DURATION_END);
	}
	
	public static final int decrementFlagMorphDuration(final int mask) {
		//value 255 should be a infinite loop
		return (!isFlagMorphDurationEmpty(mask) && getFlagMorphDuration(mask) != 255) ? getFlagMorphDuration(mask) - 1 : getFlagMorphDuration(mask);
	}
	
	public static final boolean isFlagMorphDurationEmpty(final int mask) {
		return getFlagMorphDuration(mask) == 0;
	}*/
	
	//16 bits spend //calculation correct?
	
	/*
	
	// now we define x,y,z teleportation 6 bit delta distance memory and another 1 bit for negative signature
	
	public static final int FLAG_DELTA_X_BEGIN = FLAG_TARGET_TELEPORT_EFFECTS_TRUE_END + 1;
	public static final int FLAG_DELTA_X_END = FLAG_DELTA_X_BEGIN-1 + 7; // because only 7 bits in total
	
	public static final int setFlagDeltaX(final int mask, final int value) {
		
		int diff = value;
		
		if(diff > 64) { // 2^6 - 1 = 63
			diff = 64;
		}
		
		boolean isSigned = false;
		
		if(diff < -64) {
			diff = -64;
		}
		
		if(diff < 0) {
			isSigned = true;
		}
		
		if(isSigned) {
			final int unsigned = -diff;
			final int signFlag = getPartitionWithoutShell(255, FLAG_DELTA_X_END, FLAG_DELTA_X_END);
			
			diff = signFlag + unsigned;
		}
		
		return setPartitionWithoutShell(mask, FLAG_DELTA_X_BEGIN, FLAG_DELTA_X_END, diff);
	}
	
	public static final int getFlagDeltaX(final int mask) {
		
		final int unsigned = getPartitionWithoutShell(mask, FLAG_DELTA_X_BEGIN, FLAG_DELTA_X_END - 1);
		final int sign = getPartitionWithoutShell(255, FLAG_DELTA_X_END, FLAG_DELTA_X_END);
		
		int result = unsigned;
		
		if(sign != 0) {
			 result = -unsigned;
		}
		
		return result;//getPartitionWithoutShell(mask, FLAG_DELTA_X_BEGIN, FLAG_DELTA_X_END);
	}
	
	//11 bits spend
	*/
}
