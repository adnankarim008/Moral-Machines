package ethicalengine;

import java.util.*;

/**
 * The type Scenario.
 */
public class Scenario {

    /**
     * The Passengers.
     */
    List<Character> passengers;
    /**
     * The Pedestrians.
     */
    List<Character> pedestrians;
    /**
     * The Is legal crossing.
     */
    boolean isLegalCrossing;

    /**
     * Instantiates a new Scenario.
     *
     * @param passengers      the passengers as array
     * @param pedestrians     the pedestrians as array
     * @param isLegalCrossing the is legal crossing
     */
    public Scenario(Character[] passengers, Character[] pedestrians, boolean isLegalCrossing){
        checkIsYouConstraint(passengers,pedestrians);
        this.passengers = new ArrayList<>(Arrays.asList(passengers));
        this.pedestrians = new ArrayList<>(Arrays.asList(pedestrians));
        this.isLegalCrossing = isLegalCrossing;
    }

    private void checkIsYouConstraint(Character[] passengers, Character[] pedestrians) {
        int countIsYou = 0;
        for (Character character : passengers){
            if(character.getClass() != Person.class)
                continue;

            Person passenger = (Person)character;
            if (passenger.isYou()){
                countIsYou++;
            }
        }
        for (Character character : pedestrians){
            if(character.getClass() != Person.class)
                continue;

            Person pedestrian = (Person)character;
            if (pedestrian.isYou()){
                countIsYou++;
            }
        }
        if (countIsYou > 1){
            throw new IllegalArgumentException("Each scenario must have only one instance of Person for which isYou() returns true");
        }
    }

    /**
     * Check if the user is present amongst the passengers.
     *
     * @return status
     */
    public boolean hasYouInCar(){
        boolean hasYouInCar = false;
        for (Character character : passengers) {
            if(character.getClass() != Person.class)
                continue;

            Person passenger = (Person)character;
            if (passenger.isYou()) {
                hasYouInCar = true;
                break;
            }
        }
        return hasYouInCar;
    }

    /**
     * Check if the user is present amongst the pedestrians.
     *
     * @return status
     */
    public boolean hasYouInLane(){
        boolean hasYouInLane = false;
        for (Character character : pedestrians) {
            if(character.getClass() != Person.class)
                continue;

            Person passenger = (Person)character;
            if (passenger.isYou()) {
                hasYouInLane = true;
                break;
            }
        }
        return  hasYouInLane;
    }

    /**
     * Gets passengers of scenario.
     *
     * @return the passengers
     */
    public List<Character> getPassengers() {
        return passengers;
    }

    /**
     * Gets pedestrians of scenario.
     *
     * @return the pedestrians
     */
    public List<Character> getPedestrians() {
        return pedestrians;
    }

    /**
     * Is the crossing on a green light.
     *
     * @return the boolean
     */
    public boolean isLegalCrossing() {
        return isLegalCrossing;
    }

    /**
     * Sets the crossing status of the current scenario.
     *
     * @param legalCrossing the legal crossing
     */
    public void setLegalCrossing(boolean legalCrossing) {
        isLegalCrossing = legalCrossing;
    }

    /**
     * Get total passengers for scenario.
     *
     * @return the int
     */
    public int getPassengerCount(){
        return passengers.size();
    }

    /**
     * Get total pedestrians for scenario.
     *
     * @return the int
     */
    public int getPedestrianCount(){
        return pedestrians.size();
    }

    @Override
    public String toString() {
        String passengerString = "";
        String pedestrianString = "";
        for(Character c : passengers){
            passengerString += c + "\n";
        }

        for(Character c : pedestrians){
            pedestrianString += c + "\n";
        }

        return  "=============================" +"\n" +
                "# Scenario " + "\n" +
                "=============================" + "\n" +
                "Legal Crossing: " + (isLegalCrossing ? "yes" : "no") + "\n" +
                "Passengers  (" + passengers.size() +")" + "\n" +
                    passengerString +
                "Pedestrians (" + pedestrians.size() + ")" + "\n" +
                    pedestrianString;
    }


}
