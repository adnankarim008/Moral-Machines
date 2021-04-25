import ethicalengine.*;
import ethicalengine.Character;
import ethicalengine.Person.Profession;
import ethicalengine.Person.AgeCategory;

import java.io.File;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The type Audit.
 */
public class Audit {
    private String name = "Unspecified";
    //List<Scenario> scenarioList;
    private LinkedHashMap<Scenario, Decision> scenarioListTemp = new LinkedHashMap<>();
    private LinkedHashMap<Scenario, Decision> scenarioListAll = new LinkedHashMap<>();
    private String simulationResult = "Simulation not run";

    /**
     * Instantiates a new Audit.
     */
    public Audit(){}

    /**
     * Instantiates a new Auditu using a list of preconfigured scenarios.
     *
     * @param scenarios the scenarios
     */
    public Audit(Scenario[] scenarios){
        for(Scenario s : scenarios){
            scenarioListAll.put(s,null);
        }
        //this.scenarioList = Arrays.asList(scenarios);
    }

    /**
     * Gets the statistics for current simulations in the class.
     */
    public void run(){
        if(!name.equals("User")) {
            scenarioListTemp.putAll(scenarioListAll);

            for(Map.Entry<Scenario, Decision> e : scenarioListTemp.entrySet()){
                e.setValue(EthicalEngine.decide(e.getKey()));
            }
        }

        HashMap<String, Double> results = new HashMap<>();
        Dictionary table = new Hashtable();



        simulationResult = "";

        simulationResult += "===========================================\n";
        simulationResult += "# " + name + " Audit\n";
        simulationResult += "===========================================\n";
        simulationResult += "- % SAVED AFTER " + scenarioListTemp.size() + " RUNS\n";


        results.put("female", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && x.getGender() == ethicalengine.Character.Gender.FEMALE));
        results.put("male", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && x.getGender() == ethicalengine.Character.Gender.MALE));

        results.put("adult", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getAgeCategory() == AgeCategory.ADULT));
        results.put("senior", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getAgeCategory() == AgeCategory.SENIOR));
        results.put("child", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getAgeCategory() == AgeCategory.CHILD));
        results.put("baby", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getAgeCategory() == AgeCategory.BABY));

        results.put("average", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getBodyType() == ethicalengine.Character.BodyType.AVERAGE));
        results.put("athletic", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getBodyType() == ethicalengine.Character.BodyType.ATHLETIC));
        results.put("overweight", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getBodyType() == ethicalengine.Character.BodyType.OVERWEIGHT));

        results.put("doctor", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getProfession() == Profession.DOCTOR));
        results.put("ceo", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getProfession() == Profession.CEO));
        results.put("cop", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getProfession() == Profession.COP));
        results.put("criminal", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getProfession() == Profession.CRIMINAL));
        results.put("driver",FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getProfession() == Profession.DRIVER));
        results.put("homeless", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getProfession() == Profession.HOMELESS));
        results.put("unemployed", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getProfession() == Profession.UNEMPLOYED));
        results.put("unknown", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).getProfession() == Profession.UNKNOWN));

        results.put("pregnant", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class && ((Person) x).isPregnant()));

        results.put("person", FindAvg(scenarioListTemp, x-> x.getClass() == Person.class));
        results.put("animal", FindAvg(scenarioListTemp, x-> x.getClass() == Animal.class));

        results.put("red", FindAvg(scenarioListTemp, false));
        results.put("green", FindAvg(scenarioListTemp, true));

        List<String> animalsInScenario = findAnimalsInScenario(scenarioListTemp);

        for(String animal : animalsInScenario){
            results.put(animal, FindAvg(scenarioListTemp, x-> x.getClass() == Animal.class && ((Animal) x).getSpecies().equals(animal)));
        }

        //results.put("wolf", FindAvg(scenarioListTemp, x-> x.getClass() == Animal.class && ((Animal) x).getSpecies() == "wolf"));
        //results.put("cat", FindAvg(scenarioListTemp, x-> x.getClass() == Animal.class && ((Animal) x).getSpecies() == "cat"));
        //results.put("dog", FindAvg(scenarioListTemp, x-> x.getClass() == Animal.class && ((Animal) x).getSpecies() == "dog"));

        results.put("pet", FindAvg(scenarioListTemp, x-> x.getClass() == Animal.class && ((Animal) x).isPet()));

        results.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .forEach(x-> {
                        if(!x.getValue().equals(2.0)) simulationResult += x.getKey() + ": " + x.getValue() + "\n";
                    });

        simulationResult += "--\n";
        simulationResult += "average age: " + FindAvgAge(scenarioListTemp) + "\n";

    }

    /**
     * Generates a fixed number of scenarios and runs them automatically if not interactive mode.
     *
     * @param runs number of runs to generate
     */
    public void run(int runs){
        scenarioListAll = new LinkedHashMap<>();

        for (int i = 0; i < runs;i++){
            scenarioListAll.put(new ScenarioGenerator().generate(),null);
            //scenarioList.add((new ScenarioGenerator().generate()));
        }

        if(name.equals("User"))
            return;

        run();

        printStatistic();

        //printToFile("logs/results.log");

    }

    /**
     * Print statistics to file.
     *
     * @param filePath the relative file path to save statistics
     */
    public void printToFile(String filePath){
        try {
            File file = new File(filePath);

            if(filePath.contains("/")){
                String dirName = file.getParentFile().getName();

                if(!Files.exists(FileSystems.getDefault().getPath(dirName))){
                    throw new Exception("ERROR: could not print results. Target directory does not exist.");
                }
            }

            Files.write(file.toPath(), Arrays.asList(simulationResult), StandardCharsets.US_ASCII,
                    Files.exists(file.toPath()) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private List<String> findAnimalsInScenario(LinkedHashMap<Scenario,Decision> scenarioListTemp) {
        List<String> animalsInScenario = new ArrayList<>();
        for(Map.Entry<Scenario, Decision> e : scenarioListTemp.entrySet()){
            e.getKey().getPassengers().stream().filter(x-> x.getClass() == Animal.class)
                    .forEach(x->{
                        if(!animalsInScenario.contains(((Animal) x).getSpecies()))
                            animalsInScenario.add(((Animal) x).getSpecies());
                    });

            e.getKey().getPedestrians().stream().filter(x-> x.getClass() == Animal.class)
                    .forEach(x->{
                        if(!animalsInScenario.contains(((Animal) x).getSpecies()))
                            animalsInScenario.add(((Animal) x).getSpecies());
                    });

        }
        return animalsInScenario;
    }

    /**
     * Get user decision.
     *
     * @param scanner     the scanner reference from main object to make sure to use one object only
     * @param userConsent the user consent for printing to file
     */
    public void getUserDecision(Scanner scanner, Boolean userConsent){

        int count = 0;
        for(Map.Entry<Scenario, Decision> e : scenarioListAll.entrySet()){

            if(count > 0 && count % 3 == 0){
                run();
                printStatistic();
                if(userConsent) printToFile("user.log");

                Boolean correctInput = false;
                while(!correctInput){
                    try {
                        List<String> yesInputs = Arrays.asList("yes");
                        List<String>  noInputs = Arrays.asList("no");

                        System.out.println();
                        System.out.println("Would you like to continue? (yes/no)");

                        String input = scanner.nextLine();
                        if (yesInputs.contains(input)) {
                            correctInput = true;
                        }
                        else if(noInputs.contains(input)){
                            correctInput = true;
                            return;
                        } else throw new InvalidInputException();

                        scenarioListTemp.put(e.getKey(),e.getValue());

                    } catch(InvalidInputException ex){
                        System.out.print("Invalid response. ");
                        continue;
                    }
                }


            }

            //System.out.println(e.getKey());
            System.out.print(e.getKey());

            Boolean correctInput = false;
            while(!correctInput){
                try {
                    List<String> correctCarInputs = Arrays.asList("passenger", "passengers", "1");
                    List<String>  correctPedInputs = Arrays.asList("pedestrian", "pedestrians", "2");

                    System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2]");

                    String input = scanner.nextLine();
                    if (correctCarInputs.contains(input)) {
                        correctInput = true;
                        e.setValue(Decision.PASSENGERS);
                    }
                    else if(correctPedInputs.contains(input)){
                        correctInput = true;
                        e.setValue(Decision.PEDESTRIANS);
                    } else throw new InvalidInputException();

                    scenarioListTemp.put(e.getKey(),e.getValue());

                } catch(InvalidInputException ex){
                    System.out.print("Invalid response. ");
                    continue;
                }
            }
            count++;
        }

        run();
        printStatistic();
        if(userConsent) printToFile("user.log");

        System.out.println("That’s all. Press Enter to quit.");
        scanner.nextLine();
        return;

    }

    private double FindAvg(LinkedHashMap<Scenario, Decision> scenarioList, Predicate<? super Character> predicate){
        Double avg = 0.0;
        Double total = 0.0;
        int runs = scenarioList.size();
        for(Map.Entry<Scenario, Decision> e : scenarioList.entrySet()) {
            double car = (double) e.getKey().getPassengers().stream().filter(predicate).count();
            double ped = (double) e.getKey().getPedestrians().stream().filter(predicate).count();
            total += car + ped;

            if (car == 0 && ped == 0) {
                runs--;
            } else if (e.getValue() == Decision.PASSENGERS) {
                avg += car;
            } else if (e.getValue() == Decision.PEDESTRIANS) {
                avg += ped;
            }
        }

        if(runs == 0)
            return 2;

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.valueOf(df.format(avg / total));
    }

    private double FindAvg(LinkedHashMap<Scenario, Decision> scenarioList, Boolean legality){
        Double avg = 0.0;
        Double total = 0.0;
        total += scenarioList.size();
        for(Map.Entry<Scenario, Decision> e : scenarioList.entrySet()) {
            if(e.getKey().isLegalCrossing() == legality)
                avg += 1;
            else{
                total -= 1;
            }
        }


        if(total == 0)
            return 2;

        DecimalFormat df = new DecimalFormat("#.#");
        return Double.valueOf(df.format(avg / total));
    }

    private Double FindAvgAge(LinkedHashMap<Scenario, Decision> scenarioList){
        Double count = 0.0;
        Double total = 0.0;
        for(Map.Entry<Scenario, Decision> e : scenarioList.entrySet()) {
            total += e.getKey().getPedestrians().stream().filter(x-> x.getClass() == Person.class).count()
                    + e.getKey().getPassengers().stream().filter(x-> x.getClass() == Person.class).count();

            count += e.getKey().getPedestrians().stream().filter(x-> x.getClass() == Person.class).collect(Collectors.summingInt(x-> x.getAge()))
                    + e.getKey().getPassengers().stream().filter(x-> x.getClass() == Person.class).collect(Collectors.summingInt(x-> x.getAge()));

        }
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.valueOf(df.format(count / total));
    }

    /**
     * Set audit type.
     *
     * @param name the name of the audit type
     */
    public void setAuditType(String name){
        this.name = name;
    }

    /**
     * Gets audit type.
     *
     * @return the audit type
     */
    public String getAuditType() { return name; }


    public String toString() { return simulationResult; }

    /**
     * Print statistic to command line.
     */
    public void printStatistic(){
        System.out.print(simulationResult);
    }

}
