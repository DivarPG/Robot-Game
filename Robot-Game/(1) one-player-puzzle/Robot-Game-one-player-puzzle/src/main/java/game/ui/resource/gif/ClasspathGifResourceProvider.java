package game.ui.resource.gif;

import game.ui.resource.ResourceProvider;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.net.URL;
import java.util.Objects;

/**
 * Поставщик GIF-ресурсов из classpath
 */
public class ClasspathGifResourceProvider implements ResourceProvider<ImageIcon, GifResource> {

    /**
     * Загрузить GIF-ресурс
     *
     * @param res дескриптор GIF-ресурса
     * @return загруженное изображение
     */
    @Override
    public ImageIcon get(@NotNull GifResource res) {

        URL url = Objects.requireNonNull(getClass().getResource("/gif/" + res.getPath()),
                "Resource not found: /gif/" + res.getPath());
        return new ImageIcon(url);
    }

}

