package game.ui.resource;

import org.jetbrains.annotations.NotNull;

public interface ResourceProvider<R,T> {
    R get(@NotNull T typeResources);
}
