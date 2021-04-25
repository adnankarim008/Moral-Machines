
import ethicalengine.*;
import ethicalengine.Character;
import ethicalengine.Person.Profession;
import ethicalengine.Person.AgeCategory;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.io.*;
import java.util.*;


/**
 * The main EthicalEngine.
 */
public class EthicalEngine {

    /**
     * The constant scanner.
     */
    public static Scanner scanner;
    private static Boolean userConsent = false;
    private static Audit myAudit;
    private static Boolean configMode = false;

    /**
     * The entry point of application.
     *
     * @param args the input command line arguments
     */
// co-ordinates program flow
    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        List<String> argsList = Arrays.asList(args);

        if(argsList.contains("-h") || argsList.contains("--help")){
            System.out.println("EthicalEngine - COMP90041 - Final Project");
            System.out.println();
            System.out.println("Usage: java EthicalEngine [arguments]");
            System.out.println();
            System.out.println("Arguments:");
            System.out.println("\t-c or --config\t\t\tOptional: path to config file");
            System.out.println("\t-h or --help\t\t\tPrint Help (this message) and exit");
            System.out.println("\t-r or --results\t\t\tOptional: path to results log file");
            System.out.println("\t-i or --interactive\t\tOptional: launches interactive mode");
            return;
        }


        if(argsList.contains("-c") || argsList.contains("--config"))
        {
            try{
                configMode = true;
                int argsC = argsList.indexOf("-c");
                int argsConfig = argsList.indexOf("--config");

                String configLocation = argsC > -1 ? argsList.get(argsC+1) : argsList.get(argsConfig+1);

                BufferedReader br = new BufferedReader(new FileReader(configLocation));
                String line="";
                String[] headerList;

                Boolean currentScenarioLegality = false;
                List<Character> currentCar = new ArrayList<>();
                List<Character> currentPed = new ArrayList<>();
                List<Scenario> scenarioList = new ArrayList<>();

                int currentLine = 0;
                while ((line = br.readLine()) != null) {
                    try{
                        currentLine++;
                        if(currentLine == 1){
                            headerList = line.split(",");
                            continue;
                        }

                        String[] columns = line.split(",");


                        if(columns[0].startsWith("scenario")){
                            if(!currentCar.isEmpty() && !currentPed.isEmpty()){
                                scenarioList.add(new Scenario(currentCar.toArray(new Character[0]), currentPed.toArray(new Character[0]), currentScenarioLegality));
                                currentCar = new ArrayList<>();
                                currentPed = new ArrayList<>();
                            }

                            currentScenarioLegality = columns[0].split(":")[1].equals("green");
                            continue;
                        }

                        if(columns.length != 10)
                            throw new InvalidDataFormatException();

                        int age = 0;
                        Profession profession = Profession.NONE;
                        Character.Gender gender =  Character.Gender.UNKNOWN;
                        Character.BodyType bodyType = Character.BodyType.UNSPECIFIED;

                        try{ profession = columns[4].equals("") ? Profession.NONE : Profession.valueOf(columns[4].toUpperCase()); }
                        catch (IllegalArgumentException e){
                            System.out.println("WARNING: invalid characteristic in config file in line " + currentLine);
                        }

                        try{ gender = columns[1].equals("") ? Character.Gender.UNKNOWN : Character.Gender.valueOf(columns[1].toUpperCase()); }
                        catch (IllegalArgumentException e){
                            System.out.println("WARNING: invalid characteristic in config file in line " + currentLine);
                        }

                        try{ bodyType = columns[3].equals("") ?  Character.BodyType.UNSPECIFIED : Character.BodyType.valueOf(columns[3].toUpperCase()); }
                        catch (IllegalArgumentException e){
                            System.out.println("WARNING: invalid characteristic in config file in line " + currentLine);
                        }

                        try{ age = Integer.parseInt(columns[2]); }
                        catch (NumberFormatException e){
                            System.out.println("WARNING: invalid number format in config file in line " + currentLine);
                        }

                        if(columns[0].equals("person")){
                            Person p = new Person(
                                    age,
                                    profession,
                                    gender,
                                    bodyType,
                                    Boolean.parseBoolean(columns[5])
                            );
                            p.setAsYou(Boolean.parseBoolean(columns[6]));

                            if(columns[9].equals("passenger"))
                                currentCar.add(p);
                            else if(columns[9].equals("pedestrian"))
                                currentPed.add(p);

                        } else if (columns[0].equals("animal")) {
                            Animal a = new Animal(columns[7]);
                            a.setPet(Boolean.parseBoolean(columns[8]));
                            a.setAge(age);
                            a.setGender(gender);

                            if(columns[9].equals("passenger"))
                                currentCar.add(a);
                            else if(columns[9].equals("pedestrian"))
                                currentPed.add(a);
                        }
                    }
                    catch (InvalidDataFormatException e){
                        System.out.println("WARNING: invalid data format in config file in line " + currentLine);
                        continue;
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                scenarioList.add(new Scenario(currentCar.toArray(new Character[0]), currentPed.toArray(new Character[0]), currentScenarioLegality));

                myAudit = new Audit(scenarioList.toArray(new Scenario[0]));

            } catch (FileNotFoundException e){
                System.out.println("ERROR: could not find config file.");
                return;
            } catch (Exception e){

            }

        } else {
            myAudit = new Audit();
        }

        if(argsList.contains("-i") || argsList.contains("--interactive"))
        {
            myAudit.setAuditType("User");

            try (BufferedReader br = new BufferedReader(new FileReader("welcome.ascii"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Boolean correctInput = false;
            while(!correctInput){
                try {
                    System.out.println("Do you consent to have your decisions saved to a file? (yes/no): ");

                    String input = scanner.nextLine();
                    if (input.equals("yes")) {
                        correctInput = true;
                        userConsent = true;
                    }
                    else if(input.equals("no")){
                        correctInput = true;
                        userConsent = false;
                    } else throw new InvalidInputException();
                } catch(InvalidInputException e){
                    System.out.print("Invalid response. ");
                    continue;
                }
            }
            if (configMode) {
                //myAudit.run();
            } else {
                myAudit.run(10);
            }
            myAudit.getUserDecision(scanner, userConsent);

            return;
        }
        else{
            myAudit.setAuditType("Algorithm");
            if (configMode) {
                myAudit.run();
                myAudit.printStatistic();
            } else {
                myAudit.run(500);
            }
        }

    }

    /**
     * Decide decision.
     *
     * @param scenario the scenario to decide on
     * @return the decision by ehtical engine
     */
    public static Decision decide(Scenario scenario){

        int passengerPoints = 0;
        int pedestrianPoints = 0;

        for(Character character : scenario.getPassengers()){
            if(character.getClass() == Person.class){
                Person p = (Person)character;

                passengerPoints += getPersonPoints(p);
            }
            else if(character.getClass() == Animal.class){
                Animal a = (Animal)character;

                passengerPoints += getAnimalPoints(a);
            }
        }

        for(Character character : scenario.getPedestrians()){
            if(character.getClass() == Person.class){
                Person p = (Person)character;

                pedestrianPoints += getPersonPoints(p);
            }
            else if(character.getClass() == Animal.class){
                Animal a = (Animal)character;

                pedestrianPoints += getAnimalPoints(a);
            }
        }

        if(!scenario.isLegalCrossing())
            pedestrianPoints -= 7;

        if(passengerPoints > pedestrianPoints)
            return Decision.PASSENGERS;
        else
            return Decision.PEDESTRIANS;
    }

    private static int getPersonPoints(Person p){
        int points = 0;

        AgeCategory ageCat = p.getAgeCategory();
        if(ageCat == AgeCategory.BABY)
            points += 8;
        else if(ageCat == AgeCategory.CHILD)
            points += 6;
        else if(ageCat == AgeCategory.ADULT || ageCat == AgeCategory.SENIOR)
            points += 4;

        Character.BodyType bodyType = p.getBodyType();
        if(bodyType == Character.BodyType.AVERAGE)
            points += 0;
        else if(bodyType == Character.BodyType.ATHLETIC)
            points += 3;
        else if(bodyType == Character.BodyType.OVERWEIGHT)
            points += -7;
        else if(bodyType == Character.BodyType.UNSPECIFIED)
            points += 1;

        Profession profession = p.getProfession();
        if(profession == Profession.DOCTOR)
            points += 10;
        else if(profession == Profession.CEO)
            points += 4;
        else if(profession == Profession.CRIMINAL)
            points += -5;
        else if(profession == Profession.HOMELESS)
            points += 1;
        else if(profession == Profession.UNEMPLOYED)
            points += 2;
        else if(profession == Profession.UNKNOWN)
            points += 2;
        else if(profession == Profession.DRIVER)
            points += 3;
        else if(profession == Profession.COP)
            points += 6;
        else if(profession == Profession.NONE)
            points += 2;

        if(p.isPregnant())
            points += 7;

        if(p.isYou())
            points += 5;

        return points;
    }

    private static int getAnimalPoints(Animal animal){
        if(animal.isPet())
            return 2;
        else
            return 1;
    }
}

