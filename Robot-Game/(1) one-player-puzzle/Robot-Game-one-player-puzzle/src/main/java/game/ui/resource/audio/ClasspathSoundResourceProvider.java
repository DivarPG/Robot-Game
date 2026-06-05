package game.ui.resource.audio;

import game.ui.resource.ResourceProvider;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.Objects;

public class ClasspathSoundResourceProvider implements ResourceProvider<Clip, SoundResource> {

//    private static final String ROOT = "src/main/resources/audio/";
//
//    @Override
//    public Clip get(@NotNull SoundResource res) {
//
//        try {
//            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(ROOT + res.getPath()));
//
//            Clip clip = AudioSystem.getClip();
//            clip.open(stream);
//
//            return clip;
//
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//            throw new IllegalStateException("Cannot load sound: " + res, e);
//        }
//    }

    @Override
    public Clip get(@NotNull SoundResource res) {

        try {

            URL url = Objects.requireNonNull(getClass().getResource("/audio/" + res.getPath()));

            AudioInputStream stream = AudioSystem.getAudioInputStream(url);

            Clip clip = AudioSystem.getClip();
            clip.open(stream);

            return clip;

        } catch (Exception e) {
            throw new IllegalStateException("Cannot load sound: " + res, e);
        }
    }

}