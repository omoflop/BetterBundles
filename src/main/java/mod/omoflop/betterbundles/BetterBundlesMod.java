package mod.omoflop.betterbundles;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BetterBundlesMod implements ModInitializer {

	public static Tag<Item> BUNDLE_TAG;

	@Override
	public void onInitialize() {
		BUNDLE_TAG = TagRegistry.item(new Identifier("c:bundle"));
	}
}