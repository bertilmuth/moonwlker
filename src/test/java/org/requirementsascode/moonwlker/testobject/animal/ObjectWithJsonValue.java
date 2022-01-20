package org.requirementsascode.moonwlker.testobject.animal;

public class ObjectWithJsonValue {
    private final String someString;
    private final OrphanAnimal orphanAnimal;

    public ObjectWithJsonValue(String someString, OrphanAnimal orphanAnimal) {
        this.someString = someString;
        this.orphanAnimal = orphanAnimal;
    }

    public String getSomeString() {
        return someString;
    }

    public OrphanAnimal getOrphanAnimal() {
        return orphanAnimal;
    }
}