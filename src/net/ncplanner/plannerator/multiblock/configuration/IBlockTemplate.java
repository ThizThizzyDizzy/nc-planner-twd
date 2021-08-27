package net.ncplanner.plannerator.multiblock.configuration;

import java.util.ArrayList;
import net.ncplanner.plannerator.simplelibrary.image.Image;

public interface IBlockTemplate {
    String getName();
    ArrayList<String> getLegacyNames();
    String getDisplayName();
    Image getDisplayTexture();
}
