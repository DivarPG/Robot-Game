package game.model.field.core;

public abstract class SelfActivatingObject extends CellObject {

    //region ДЕЙСТВИЯ

    public abstract void execute(CellObject object);

    //endregion
}
