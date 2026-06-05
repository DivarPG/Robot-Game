package game.ui.utils;

import game.ui.resource.CachedResourceProvider;
import game.ui.resource.ResourceProvider;
import game.ui.resource.audio.ClasspathSoundResourceProvider;
import game.ui.resource.audio.SoundResource;


import javax.sound.sampled.Clip;

public final class SoundActivator {

   // private static final ResourceProvider<Clip, SoundResource> soundResourceProvider = new CachedResourceProvider<>(new ClasspathSoundResourceProvider());

    private static final ResourceProvider<Clip, SoundResource> soundResourceProvider = new ClasspathSoundResourceProvider();

    public static void playSound(SoundResource sound) {

        Clip clip = soundResourceProvider.get(sound);

        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

}
