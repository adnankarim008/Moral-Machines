package ethicalengine;

/**
 * The type Animal.
 */
public class Animal extends Character{

    private String species;
    private boolean isPet;

    /**
     * Instantiates a new Animal.
     *
     * @param species the species
     */
    public Animal(String species){
        this.species = species;
    }

    /**
     * Instantiates a new Animal from another animal object.
     *
     * @param otherAnimal the animal object to use
     */
    Animal(Animal otherAnimal){
        this.species = otherAnimal.species;
    }

    /**
     * Gets species.
     *
     * @return the species
     */
    public String getSpecies() {
        return species;
    }

    /**
     * Sets species.
     *
     * @param species the species
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * Returns true if animal is pet.
     *
     * @return is pet or not
     */
    public boolean isPet() {
        return isPet;
    }

    /**
     * Sets if the animal is a pet.
     *
     * @param pet is pet or not
     */
    public void setPet(boolean pet) {
        isPet = pet;
    }

    @Override
    public String toString() {
        return "- " +  species + (isPet ? " is pet" : "");
    }
}
