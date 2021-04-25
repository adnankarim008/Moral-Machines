package ethicalengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;
import ethicalengine.Person.Profession;

/**
 * The type Scenario generator.
 */
public class ScenarioGenerator {

    private Random random;
    private int passengerCountMin;
    private int passengerCountMax;
    private int pedestrianCountMin;
    private int pedestrianCountMax;

    /**
     * Instantiates a new Scenario generator with a random and predefined minimums and maximums.
     */
    public ScenarioGenerator(){
        // set seed to a truly random number
        random = new Random();
        this.passengerCountMin = 1;
        this.passengerCountMax = 5;
        this.pedestrianCountMin = 1;
        this.pedestrianCountMax = 5;
    }

    /**
     * Instantiates a new Scenario generator with a custom seed and predefined minimums and maximums.
     *
     * @param seed the seed
     */
    public ScenarioGenerator (long seed) {
        //this constructor sets the seed with a predefined value
        this(seed,1,5,1,5);
    }

    /**
     * Instantiates a new Scenario generator with a custom seed and minimum maximum as required.
     *
     * @param seed                   the seed
     * @param passengerCountMinimum  the passenger count minimum
     * @param passengerCountMaximum  the passenger count maximum
     * @param pedestrianCountMinimum the pedestrian count minimum
     * @param pedestrianCountMaximum the pedestrian count maximum
     */
    public ScenarioGenerator (long seed, int passengerCountMinimum, int passengerCountMaximum, int pedestrianCountMinimum, int pedestrianCountMaximum){
        random = new Random();
        random.setSeed(seed);

        this.passengerCountMin = passengerCountMinimum;
        this.passengerCountMax = passengerCountMaximum;
        this.pedestrianCountMin = pedestrianCountMinimum;
        this.pedestrianCountMax = pedestrianCountMaximum;
    }

    /**
     * Set minimum passengers for generator.
     *
     * @param min the min
     */
    public void setPassengerCountMin(int min){
            passengerCountMin = min;
    }

    /**
     * Set maximun passengers for generator.
     *
     * @param max the max
     */
    public void setPassengerCountMax(int max){
            passengerCountMax = max;
    }

    /**
     * Set minimum pedestrians for generator.
     *
     * @param min the min
     */
    public void setPedestrianCountMin(int min){
            pedestrianCountMin = min;
    }

    /**
     * Set maximum pedestrians for generator.
     *
     * @param max the max
     */
    public void setPedestrianCountMax(int max){
            pedestrianCountMax = max;
    }

    /**
     * Generate a random person.
     *
     * @return the person
     */
    public Person getRandomPerson(){
        Character.Gender gender = getRandomGender();
        int age = getRandomInt(1,80);

        Person p = new Person(
                age,
                age > 16 && age < 68 ? getRandomProfession() : Profession.NONE,
                gender,
                getRandomBodyType(),
                gender.equals(Character.Gender.FEMALE) ? random.nextBoolean() : false);

        return p;
    }

    /**
     * Generate a random animal.
     *
     * @return the animal
     */
    public Animal getRandomAnimal(){
        String[] speciesList = {"cat", "dog", "wolf"};

        Animal a = new Animal(speciesList[random.nextInt(speciesList.length)]);
        a.setPet(random.nextBoolean());

        return a;
    }

    /**
     * Generate scenario based on current template.
     *
     * @return the scenario
     */
    public Scenario generate(){
        List<Character> passengers = new ArrayList<>();
        List<Character> pedestrians = new ArrayList<>();

        for(int i = 0; i < getRandomInt(passengerCountMin, passengerCountMax); i++){
            int animalSeed = getRandomInt(1,8);
            //1 in 8 chance of passenger being an animal
            if(animalSeed > 1){
                passengers.add(getRandomPerson());
            } else{
                passengers.add(getRandomAnimal());
            }
        }

        for(int i = 0; i < getRandomInt(pedestrianCountMin, pedestrianCountMax); i++){
            int animalSeed = getRandomInt(1,8);
            //1 in 8 chance of passenger being an animal
            if(animalSeed > 1){
                pedestrians.add(getRandomPerson());
            } else{
                pedestrians.add(getRandomAnimal());
            }
        }

        return new Scenario(passengers.toArray(new Character[0]),pedestrians.toArray(new Character[0]), random.nextBoolean());
    }


    private int getRandomInt(int min, int max){
        return random.nextInt(max + 1 - min) + min;
    }

    private Character.Gender getRandomGender(){
        Character.Gender[] list = Character.Gender.values();
        return list[random.nextInt(2)];
    }

    private Profession getRandomProfession(){
        Profession[] list = Profession.values();
        return list[random.nextInt(list.length)];
    }

    private Character.BodyType getRandomBodyType(){
        Character.BodyType[] list = Character.BodyType.values();
        return list[random.nextInt(list.length)];
    }


}


