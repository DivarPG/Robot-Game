package game.ui.resource.audio;

import game.ui.resource.ResourceProvider;

import javax.sound.sampled.Clip;

public class SoundPlayer {

   // private static final ResourceProvider<Clip, SoundResource> soundResourceProvider = new CachedResourceProvider<>(new ClasspathSoundResourceProvider());

    private final ResourceProvider<Clip, SoundResource> soundResourceProvider; //= new ClasspathSoundResourceProvider();

    public SoundPlayer(ResourceProvider<Clip, SoundResource> soundResourceProvider){
        this.soundResourceProvider = soundResourceProvider;
    }

    public void playSound(SoundResource sound) {

        Clip clip = soundResourceProvider.get(sound);

        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

}
