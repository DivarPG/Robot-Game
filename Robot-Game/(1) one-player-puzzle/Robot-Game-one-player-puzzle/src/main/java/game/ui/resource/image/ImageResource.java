package game.ui.resource.image;

import game.ui.resource.ResourceDescriptor;

/**
* Перечень графических ресурсов
*/
public enum ImageResource implements ResourceDescriptor {


    ROBOT("robot/Robot.png"),
    ROBOT_PICK_BATTERY("robot/Robot_pick_battery.png"),


    BATTERY_FULL("battery/Battery_green.png"),
    BATTERY_MEDIUM("battery/Battery_yellow.png"),
    BATTERY_LOW("battery/Battery_red.png"),


    WALL_VERTICAL("wall_vertical.png"),
    WALL_HORIZONTAL("wall_horizontal.png"),

    PORTAL("field/Portal.png"),

    EXIT("exit.png");

    /**
     * Путь к файлу ресурса.
     */
    private final String path;

    ImageResource(String path){
        this.path = path;
    }

    // дилема !!!
    // без интерфейса хорошо что он пакетный , но интерфейс
    // четка заявит что должен быть метод получения пути в формате строки

    @Override
    public String getPath(){
        return path;
    }
}