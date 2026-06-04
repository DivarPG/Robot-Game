package game.ui.resource.gif;

import game.ui.resource.ResourceProvider;

import javax.swing.*;
import java.net.URL;
import java.util.Objects;


public class ClasspathGifResourceProvider implements ResourceProvider<ImageIcon, GifResource> {

    @Override
    public ImageIcon get(GifResource res) {

        URL url = Objects.requireNonNull(getClass().getResource("/gif/" + res.getPath()),
                "Resource not found: /gif/" + res.getPath());
        return new ImageIcon(url);
    }

}

