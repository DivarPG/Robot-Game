package game.ui.resource.image;

import game.ui.resource.ResourceProvider;

import java.awt.image.BufferedImage;

public interface WithImageResource {

    ImageResource getImageType();

    BufferedImage getImage( ResourceProvider<BufferedImage,ImageResource> provider);
}
