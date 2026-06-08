package game.ui.resource.audio;

import game.ui.resource.ResourceDescriptor;

/**
 * Перечень звуковых ресурсов
 */
public enum SoundResource  implements ResourceDescriptor {
    MOVE("robot_move.wav"),
    PICK_BATTERY("pickUp_battery.wav"),
    TELEPORT("tp.wav");

    /**
     * Путь к файлу ресурса
     */
    private final String path;

    SoundResource(String path){
        this.path = path;
    }

    @Override
    public String getPath(){
        return path;
    }
}

