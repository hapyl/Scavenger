package me.hapyl.scavenger.task;

public enum LinkedType {

    GATHER_ITEM("Gatherer", Type.GATHER_ITEM),
    CRAFT_ITEM("Crafter", Type.CRAFT_ITEM),
    KILL_ENTITY("Slayer", Type.KILL_ENTITY),
    BREED_ANIMAL("Farmer", Type.BREED_ANIMAL),
    DIE_FROM_CAUSE("Death", Type.DIE_FROM_CAUSE),
    DIE_FROM_ENTITY("Victim", Type.DIE_FROM_ENTITY),
    ADVANCEMENT_ADVANCER("Advancer", Type.ADVANCEMENT_ADVANCER),
    ;

    private final String name;
    private final Type<?> link;

    LinkedType(String name, Type<?> link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public Type<?> getLink() {
        return link;
    }
}
