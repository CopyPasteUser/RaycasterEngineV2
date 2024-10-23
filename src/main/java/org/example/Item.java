package org.example;

public abstract class Item {
    protected String name;
    protected String description;
    protected int weight; // Gewicht des Items
    protected int value;

    public Item(String name, String description, int weight, int value) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.value = value;
    }

    // Getter und Setter
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }

    // Eine Methode, die definiert, was passiert, wenn das Item benutzt wird
    public abstract void use();
}
