package game.ui.resource.gif;

import game.ui.resource.ResourceDescriptor;

public enum GifResource implements ResourceDescriptor {

    PORTAL("portal.gif"),

    DIGGING("digging.gif");

    private final String path;

    GifResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}