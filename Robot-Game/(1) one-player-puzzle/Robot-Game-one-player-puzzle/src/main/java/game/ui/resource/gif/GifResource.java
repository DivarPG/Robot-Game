package game.ui.resource.gif;

import game.ui.resource.ResourceDescriptor;

/**
 * Перечень GIF-ресурсов
 */
public enum GifResource implements ResourceDescriptor {

    PORTAL("portal.gif");

    /**
     * Путь к файлу ресурса
     */
    private final String path;

    GifResource(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}