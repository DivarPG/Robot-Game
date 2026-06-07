package game.ui.resource.image;

import game.ui.resource.ResourceDescriptor;

/**
* Список типизированных расположений изображений
*/
public enum ImageResource implements ResourceDescriptor {

    // изображение робота
    ROBOT("robot/Robot.png"),
    ROBOT_PICK_BATTERY("robot/Robot_pick_battery.png"),

    // изображение батарейки
    BATTERY_FULL("battery/Battery_green.png"),
    BATTERY_MEDIUM("battery/Battery_yellow.png"),
    BATTERY_LOW("battery/Battery_red.png"),

    // изображение стены
    WALL_VERTICAL("wall_vertical.png"),
    WALL_HORIZONTAL("wall_horizontal.png"),

    PORTAL("field/Portal.png"),

    EXIT("exit.png");

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