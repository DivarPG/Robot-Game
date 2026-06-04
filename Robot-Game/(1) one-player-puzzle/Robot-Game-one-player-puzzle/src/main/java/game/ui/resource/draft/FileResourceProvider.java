// не используется


//package game.ui.resource;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//
//public class FileResourceProvider <R, T extends ResourceDescriptor> implements ResourceProvider<R,T>{
//
//    private final String root;
//    private final ResourceLoader<R> loader;
//
//    public FileResourceProvider(@NotNull String root, @NotNull ResourceLoader<R> loader){
//        this.root = root;
//        this.loader = loader;
//    }
//
//    @Override
//    public R get(@NotNull T typeResources) {
//        return loader.load(new File(root+typeResources.getPath()));
//    }
//}
