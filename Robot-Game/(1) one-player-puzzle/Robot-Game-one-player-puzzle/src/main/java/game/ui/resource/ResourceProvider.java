package game.ui.resource;

import org.jetbrains.annotations.NotNull;

/**
 * Поставщик ресурсов
 *
 * @param <R> тип ресурса
 * @param <T> тип дескриптора ресурса
 */
public interface ResourceProvider<R,T extends ResourceDescriptor> {

    /**
     * Получить ресурс по его дескриптору
     *
     * @param typeResources дескриптор ресурса
     * @return загруженный ресурс
     */
    R get(@NotNull T typeResources);
}
