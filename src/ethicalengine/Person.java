package ethicalengine;

/**
 * The type Person.
 */
public class Person extends Character {

    /**
     * The enum Profession.
     */
    public enum Profession {
        /**
         * Doctor profession.
         */
        DOCTOR,
        /**
         * Ceo profession.
         */
        CEO,
        /**
         * Criminal profession.
         */
        CRIMINAL,
        /**
         * Homeless profession.
         */
        HOMELESS,
        /**
         * Unemployed profession.
         */
        UNEMPLOYED,
        /**
         * Unknown profession.
         */
        UNKNOWN,
        /**
         * Driver profession.
         */
        DRIVER,
        /**
         * Cop profession.
         */
        COP,
        /**
         * None profession.
         */
        NONE
    }

    /**
     * The enum Age category.
     */
    public enum AgeCategory {
        /**
         * Baby age category.
         */
        BABY,
        /**
         * Child age category.
         */
        CHILD,
        /**
         * Adult age category.
         */
        ADULT,
        /**
         * Senior age category.
         */
        SENIOR
    }

    private boolean isPregnant;
    private final Profession profession;
    private boolean isYou;

    /**
     * Instantiates a new Person.
     *
     * @param age        the age
     * @param profession the profession
     * @param gender     the gender
     * @param bodyType   the body type
     * @param isPregnant if person is pregnant
     * @throws IllegalArgumentException if person is not female and is pregnant
     * @throws IllegalArgumentException if person is not an adult and has a profession
     */
    public Person(int age, Profession profession, Gender gender, BodyType bodyType, boolean isPregnant){
        this.age = age;
        this.gender = gender;
        this.bodyType = bodyType;
        this.profession = profession;
        this.isPregnant = isPregnant;

        // pregnancy invariancy condition
        if(!gender.equals(Gender.FEMALE) && isPregnant){
            throw new IllegalArgumentException("This Person is not female. They cannot be pregnant");
        }
        // profession invariancy condition
        if((age < 17 || age > 68 ) && profession != Profession.NONE){
            throw new IllegalArgumentException("Only a Person falling in ADULT CATEGORY (i.e 16-68) can have a profession");
        }
    }

    /**
     * Instantiates a new Person from another Person object.
     *
     * @param otherPerson the other person
     */
    Person(Person otherPerson){
        this.age = otherPerson.age;
        this.gender = otherPerson.gender;
        this.bodyType = otherPerson.bodyType;
        this.profession = otherPerson.profession;
        this.isPregnant = otherPerson.isPregnant;
        this.isYou = otherPerson.isYou;
    }

    /**
     * Get age category of person.
     *
     * @return the age category
     */
    public AgeCategory getAgeCategory(){
       if(age >= 0 && age <= 4){
           return AgeCategory.BABY;
       } else if( age > 4 && age <= 16){
           return AgeCategory.CHILD;
       } else if(age > 16 && age <= 68){
           return AgeCategory.ADULT;
       }
     return AgeCategory.SENIOR;
    }

    /**
     * Get profession of person.
     *
     * @return the profession
     */
    public Profession getProfession(){
        if(age > 16 && age < 68){
            return profession;
        }
        return Profession.NONE;
    }

    /**
     * Is person pregnant.
     *
     * @return the boolean
     */
    public boolean isPregnant() {
        return gender == Gender.FEMALE && isPregnant;
    }

    /**
     * Set pregnant status of person.
     *
     * @param pregnant the pregnant
     */
    public void setPregnant(boolean pregnant){
        isPregnant = isPregnant();
    }

    /**
     * Is the current person the user.
     *
     * @return the boolean
     */
    public boolean isYou(){
        return isYou;
    }

    /**
     * Set the current person as you.
     *
     * @param isYou the is you
     */
    public void setAsYou(boolean isYou){
        this.isYou = isYou;
    }

    @Override
    public String toString() {
        return  "- " + (isYou ? "you" + " " : "")  +
                bodyType.toString().toLowerCase() + " " +
                getAgeCategory().toString().toLowerCase() + " " +
                (getAgeCategory().equals(AgeCategory.ADULT) ? profession.toString().toLowerCase() + " " : "")  +
                gender.toString().toLowerCase() + " " +
                (isPregnant ? "pregnant" : "");
    }
}
