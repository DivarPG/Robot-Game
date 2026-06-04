package game.ui.resource.gif;

import game.ui.resource.ResourceDescriptor;
import org.jetbrains.annotations.NotNull;

public enum GifResource implements ResourceDescriptor {

    PORTAL("portal.gif");

    private final String path;

    GifResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}