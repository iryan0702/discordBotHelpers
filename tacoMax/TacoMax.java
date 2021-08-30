/*
TacoMax.java

This program was created to help analyze the upgrades in the Discord game bot "TacoBot" where the player can purchase upgrades to increase the income of a taco shack.
After starting the program, the user can copy-paste the information from the upgrade screens into the command line for parsing.
Afterwards, the program will display the upgrades with the best cost to income boost ratio, along with additional information:
> Boosts which are exceptionally good/bad will be given special colors
> If the player provides a budget, a set of affordable boosts will be colored in green.
*/

// imports
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.text.NumberFormat;

public class TacoMax{

    // static variables for text color codes used within the command line
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args){
        System.out.println("Enter upgrade information: (type \"exit\" on a new line to begin process)");

        // create scanner and begin reading every line from the user until the user inputs "exit"
        Scanner in = new Scanner(System.in);

        ArrayList<Upgrade> upgrades = new ArrayList<>();
        
        String s = in.nextLine();
        int recentCost = -1;
        int recentBoost = -1;
        while(!s.equals("exit")){

            // parse the pasted lines for important descriptions: cost value, boost value, and ID
            if(s.contains("Cost: $")){

                s = s.substring(s.indexOf("Cost: $") + 7);
                s = s.replace(",","");
                
                recentCost = Integer.parseInt(s);

            }else if(s.contains("Boost: +$") && s.contains("/hr")){

                s = s.substring(s.indexOf("Boost: +$") + 9);
                s = s.substring(0, s.indexOf("/hr"));
                s = s.replace(",","");
                
                recentBoost = Integer.parseInt(s);

            }else if(s.contains("ID: ")){

                // If an ID is encountered, create an upgrade object with the appropriate ID, cost, and boost only if
                // a cost and boost was encountered after the last ID was found.
                s = s.substring(s.indexOf("ID: ") + 4);

                if(recentBoost >= 0 && recentCost >= 0){
                    upgrades.add(new Upgrade(s, recentCost, recentBoost));
                }

                recentCost = -1;
                recentBoost = -1;
            }

            s = in.nextLine();
        }

        // prompt user for budget:
        // If a budget is given, a set of affordable upgrades will be highlighted in green (prioritising upgrades with the max value)
        System.out.println("---------------------------------------------");
        System.out.println("What is your budget? (-1 for infinite budget)");
        System.out.println("---------------------------------------------");
        s = in.nextLine();
        int budget = Integer.parseInt(s);

        Collections.sort(upgrades, new UpgradeComparator());

        // Outlier analysis: upgrades with different values will be displayed with different colors
        // lower outlier = blue, lower quartile = green, middle = black, upper quartile = purple, upper outlier = red
        int q1value = upgrades.get(upgrades.size()/4).value;
        int q2value = upgrades.get(upgrades.size()/2).value;
        int q3value = upgrades.get(upgrades.size()*3/4).value;
        int iqr = q3value - q1value;
        int oulierLow = q1value - iqr;
        int outlierHigh = q3value + iqr;

        for(Upgrade u: upgrades){
            if(budget >= 0){ // budget analysis: assign cost color
                if(budget >= u.cost){
                    budget -= u.cost;
                    u.costColor = ANSI_GREEN;
                }else{
                    u.costColor = ANSI_RED;
                }
            }

            if(u.value <= oulierLow){ //assign boost color
                u.valueColor = ANSI_BLUE;
            }else if(u.value >= outlierHigh){
                u.valueColor = ANSI_RED;
            }else if(u.value < q1value){
                u.valueColor = ANSI_GREEN;
            }else if(u.value > q3value){
                u.valueColor = ANSI_PURPLE;
            }
        }

        Collections.reverse(upgrades);
        
        // display all upgrades to user, with the best upgrade on the bottom of the screen
        // the upgrade cost and boost value are also colored appropriately
        System.out.println("vvvvv worst upgrade vvvvv");
        for(Upgrade u: upgrades){
            if(u.name.equals("apprentice") || u.name.equals("cook") || u.name.equals("advertiser") || u.name.equals("greeter") || 
            u.name.equals("sous") || u.name.equals("head") || u.name.equals("executive")){ // hardcoded values to place upgrade name in (), this is to indicate that the upgrade command for these upgrades is "%hire" instead of the usual "%buy"
                System.out.println("(" + u.name + ") – " + u.costColor + NumberFormat.getIntegerInstance().format(u.cost) + "$" + ANSI_RESET + " – value: " + u.valueColor + NumberFormat.getIntegerInstance().format(u.value) + "$" + ANSI_RESET + " for 1$/hr");
            }else{
                System.out.println("[" + u.name + "] – " + u.costColor + NumberFormat.getIntegerInstance().format(u.cost) + "$" + ANSI_RESET + " – value: " + u.valueColor + NumberFormat.getIntegerInstance().format(u.value) + "$" + ANSI_RESET + " for 1$/hr");
            }
        }
        System.out.println("^^^^^ best upgrade ^^^^^");

    }

    // Upgrade class:
    // Stores the name, cost, boost ($/hr increase), and value (as cost per 1$/hr boost) of an upgrade
    // The object can also store two String values for associated color codes to use when displaying the upgrade cost and value
    // Value is calculated when Upgrade is initialized
    public static class Upgrade{
        public String name;
        public int cost;
        public int boost;
        public int value;

        public String costColor = ANSI_RESET;
        public String valueColor = ANSI_RESET;

        public Upgrade(String name, int cost, int boost){
            this.name = name;
            this.cost = cost;
            this.boost = boost;

            if(boost > 0){
                this.value = cost/boost;
            }else{
                this.value = 0;
            }
        }
    }

    // Comparator class for sorting Upgrade objects by value
    public static class UpgradeComparator implements Comparator<Upgrade> {
        @Override
        public int compare(Upgrade a, Upgrade b) {
            return Integer.compare(a.value,b.value);
        }
    }
}