package ethicalengine;

/**
 * The type Character.
 */
public abstract class Character {

    /**
     * The enum Gender.
     */
    public enum Gender   {
        /**
         * Female gender.
         */
        FEMALE,
        /**
         * Male gender.
         */
        MALE ,
        /**
         * Unknown gender.
         */
        UNKNOWN }

    /**
     * The enum Body type.
     */
    public enum BodyType {
        /**
         * Average body type.
         */
        AVERAGE,
        /**
         * Athletic body type.
         */
        ATHLETIC,
        /**
         * Overweight body type.
         */
        OVERWEIGHT,
        /**
         * Unspecified body type.
         */
        UNSPECIFIED }

    /**
     * The Age.
     */
    protected int age;
    /**
     * The Gender.
     */
    protected Gender gender;
    /**
     * The Body type.
     */
    protected BodyType bodyType;

    /**
     * Instantiates a new Character.
     */
    Character(){
        this.gender = Gender.UNKNOWN;
        this.bodyType = BodyType.UNSPECIFIED;
    }

    /**
     * Instantiates a new general Character.
     *
     * @param age      age of character
     * @param gender   gender of character
     * @param bodyType body type of character
     */
    Character(int age, Gender gender, BodyType bodyType){
        // age invariancy condition
        if(age < 0 ){
            throw new IllegalArgumentException("Age must be greater than or equal to 0");
        }
        this.age = age;
        this.gender = gender;
        this.bodyType = bodyType;
    }

    /**
     * Instantiates a new Character from another character.
     *
     * @param character the character
     */
    Character (Character character){
        this.age = character.age;
        this.gender = character.gender;
        this.bodyType = character.bodyType;
    }

    /**
     * Gets age of character.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets age of character.
     *
     * @param age the age
     */
    public void setAge(int age) {
        if(age < 0 ){
            throw new IllegalArgumentException("Age must be greater than or equal to 0");
        }
        this.age = age;
    }

    /**
     * Gets gender of character.
     *
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets gender of character.
     *
     * @param gender the gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Gets body type of character.
     *
     * @return the body type
     */
    public BodyType getBodyType() {
        return bodyType;
    }

    /**
     * Sets body type of character.
     *
     * @param bodyType the body type
     */
    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }


}




