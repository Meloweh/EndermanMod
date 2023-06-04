package treecree.enderscience.references;

import net.minecraft.util.ResourceLocation;

public class ResourceReference {
	
	public final static String RESOURCE_DOMAIN = "ResourceDomain";
	public final static String RESOURCE_PATH = "ResourcePath";
	
	public static final String getResourceString(final ResourceLocation resource) {
		return resource.getResourceDomain() + ":" + resource.getResourcePath();
	}
	
	public static final ResourceLocation getResourceLocationFromString(final String resourceString) {
		
		final int preDoublePointIndex = resourceString.indexOf(':');
		final int postDoublePointIndex = preDoublePointIndex + 1;
		
		return new ResourceLocation(resourceString.substring(0, preDoublePointIndex), resourceString.substring(postDoublePointIndex));//resource.getResourceDomain() + ":" + resource.getResourcePath();
	}
	
	public static final boolean resourceEqual(final ResourceLocation res1, final ResourceLocation res2) {
		return res1.getResourceDomain().equals(res2.getResourceDomain()) && res1.getResourcePath().equals(res2.getResourcePath());
	}
}
