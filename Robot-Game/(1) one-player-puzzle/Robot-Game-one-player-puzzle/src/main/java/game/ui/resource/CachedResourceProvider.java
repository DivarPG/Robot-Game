package game.ui.resource;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Декоратор поставщика ресурсов с кэшированием
 * Загруженные ресурсы сохраняются и повторно не создаются
 *
 * @param <R> тип ресурса
 * @param <T> тип дескриптора ресурса
 */
public class CachedResourceProvider <R,T extends ResourceDescriptor> implements ResourceProvider<R,T>{

    /**
     * Исходный поставщик ресурсов
     */
    private final ResourceProvider<R,T> delegate;

    /**
     * Кэш загруженных ресурсов
     */
    private final Map<T,R> cache = new HashMap<>();

    /**
     * Конструктор
     *
     * @param delegate поставщик ресурсов
     */
    public CachedResourceProvider(@NotNull ResourceProvider<R,T> delegate) {
        this.delegate = delegate;
    }

    /**
     * Получить ресурс из кэша либо загрузить его через исходный поставщик
     *
     * @param typeResources дескриптор ресурса
     * @return ресурс
     */
    @Override
    public R get(@NotNull T typeResources) {
        return cache.computeIfAbsent(typeResources, delegate::get);
    }

}
