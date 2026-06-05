package game.ui.resource.image;

import game.ui.resource.ResourceProvider;
import org.jetbrains.annotations.NotNull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ClasspathImageResourceProvider implements ResourceProvider<BufferedImage, ImageResource> {

//    private static final String ROOT = "src/main/resources/image/";
//
//    @Override
//    public BufferedImage get(@NotNull ImageResource res) {
//
//        BufferedImage image = null;
//        try {
//            image = ImageIO.read(new File(ROOT + res.getPath()));
//        } catch (IOException e) {
//            throw new IllegalStateException("Cannot load image: " + res, e);
//        }
//        return image;
//    }

    @Override
    public BufferedImage get(@NotNull ImageResource res) {

        String path = "/image/" + res.getPath();

        try (InputStream stream = Objects.requireNonNull( getClass().getResourceAsStream(path),
                "Resource not found: " + path)) {
            return ImageIO.read(stream);

        } catch (IOException e) {
            throw new IllegalStateException("Cannot load image: " + res, e);
        }
    }

}
