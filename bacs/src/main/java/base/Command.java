package base;

/**
 * Created by ilychevva
 */
public enum Command {
    NOOP(0, "No operations"),
    GAIN(23, "Gain the sunlight"),
    MOVE(20, "Move to another cell"),
    TURN_LEFT(21, "Turn left"),
    TURN_RIGHT(26, "Turn right"),
    EAT(22, "FOOD !!!"),
    ATTACK(24, "Kill'em'All"),
    OBSERVE(25, "Look Out");

    private int code;
    private String name;

    Command(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Command fromCode(int code) {
        for (Command c: Command.values())
            if (c.code == code)
                return c;
        return null;
    }
}
