package game.ui.resource;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CachedResourceProvider <R,T extends ResourceDescriptor> implements ResourceProvider<R,T>{

    private final ResourceProvider<R,T> delegate;
    private final Map<T,R> cache = new HashMap<>();

    public CachedResourceProvider(@NotNull ResourceProvider<R,T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public R get(@NotNull T typeResources) {
        return cache.computeIfAbsent(typeResources, delegate::get);
    }

}
