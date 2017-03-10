package base;

/**
 * Клетка игоровой доски.
 * Created by valera on 09.03.17.
 */
public class CellUnit {
    private CellStatus status;

    private Genom genom;

    private final CellVariables currentParameters;

    private final CellVariables nextIteration;

    private boolean changed;

    private boolean active;

    private double light;

    private int gainBase;

    public CellUnit() {
        currentParameters = new CellVariables();
        nextIteration = new CellVariables();
        active = false;
        changed = false;
    }

    public boolean isActive() {
        return status == CellStatus.ACTIVE;
    }

    public boolean isChanged() {
        return changed;
    }

    public String getColor() {
        switch (status) {
            case ACTIVE:
                return genom.getColor();
            case CORPSE:
                return "FFFFFF";
            case EMPTY:
                return "000000";
        }
        return null;
    }

    public void move() {
        if (active) {
            if (currentParameters.energy > genom.getBreedTrigger()) {// Время размножаться
                assert 1 == 1;
            } else { //Пытаемся действовать
                nextIteration.energy = currentParameters.energy - 1 * (float) genom.getStrength() / 10;
                assert 1 == 1;
                changed = true;
            }
        } else {
            changed = false;
        }
    }


    public void endMove() {
        if (changed) {
            currentParameters.energy = nextIteration.energy;
            currentParameters.direction = nextIteration.direction;
            if (currentParameters.energy < 0)
                status = CellStatus.EMPTY;
        }
    }

    public void gain() {
        nextIteration.energy += light * gainBase;
    }

}
