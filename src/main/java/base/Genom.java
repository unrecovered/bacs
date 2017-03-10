package base;

import java.util.List;

/**
 * Информация о клетке, которую мы хотим создать.
 * Created by ilychevva
 */
public class Genom {
    private String color;

    private int strength;

    private int mutagen;

    private int breedTrigger;

    private List<Command> actions;

    public int getBreedTrigger() {
        return breedTrigger;
    }

    public void setBreedTrigger(int breedTrigger) {
        this.breedTrigger = breedTrigger;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getMutagen() {
        return mutagen;
    }

    public void setMutagen(int mutagen) {
        this.mutagen = mutagen;
    }

    public List<Command> getActions() {
        return actions;
    }

    public void setActions(List<Command> actions) {
        this.actions = actions;
    }
}
