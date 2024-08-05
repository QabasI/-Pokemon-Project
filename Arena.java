/*
Authour Name : Qabas Imbewa
File Name    : Arena.java
Description  : does stuff
*/



import java.util.*;
import java.io.*;

/*
TO-DO
YAY
*/

public class Arena {

    private static ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>(0);
    private static ArrayList<Pokemon> goodGuys = new ArrayList<Pokemon>(0);
    private static final int USER = 1, ENEMY = 0;

    public static void main(String [] args) throws IOException{
        load();
        choosePlayers();

        int battleNum = 1;

        while(pokemons.size() > 0 && goodGuys.size() > 0){
            System.out.println("BATTLE "+battleNum);
            battle();
            healHP(goodGuys);
            battleNum++;
        }

        if(pokemons.size() > 0){
            System.out.println("You lost. ");
        }else{
            System.out.println("You won! You have been declared Trainer Supreme! "); // wtf
        }

    }

    public static void load()  throws IOException{
        Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
        int pokNum = inFile.nextInt();

        System.out.println("You have "+pokNum+" pokemons.");
       
        while(inFile.hasNextLine() && pokemons.size() < pokNum){
            String [] pok = inFile.nextLine().split(",");

            if(pok.length != 1){ // avoids doing anything at the start (first line is # of pokemons) and at a new line character
                Pokemon a = new Pokemon(pok);
                pokemons.add(a);
            }
            
        }

        inFile.close();

        for(Pokemon p: pokemons){
            System.out.println(Integer.toString(pokemons.indexOf(p)+1) + ". " + p); 
        }
    }

    public static void choosePlayers(){
        Scanner scanner = new Scanner(System.in);
        boolean valid = false;
        while(valid == false){
            System.out.println("Choose a player: ");
            int player = scanner.nextInt();

            if(player-1 < pokemons.size()){
                valid = true;
            }

            if(valid == false){
                System.out.println("That was an invalid player. Try again. ");
            }else{
                if(goodGuys.contains(pokemons.get(player-1)) == false && valid){
                    goodGuys.add(pokemons.get(player-1));
                }else{
                    System.out.println("You already chose that player. ");
                }
            }

            if(goodGuys.size() < 4){
                valid = false;
            }
            
        }
        
        for(Pokemon p : goodGuys){
            if(pokemons.contains(p)){
                pokemons.remove(p);
            }
        }

    }

    public static void displayTeam(){
        String displayTeam = "";

        for(Pokemon p : goodGuys){
            displayTeam += "\n" + (goodGuys.indexOf(p)+1) + ". " + p;
        }

       System.out.println("This is your chosen team:\n"+displayTeam);
    }

    public static void battle(){
        Pokemon enemy = getRandomEnemy();
        Pokemon firstPlayer = pickFirstPlayer();

        System.out.println("\nThe enemy you are against is "+enemy.getName()+".\n");


        // randomly deciding who goes first
        Random random = new Random();
        int turn = random.nextInt(2); // if 0, player goes first. if 1, computer goes first

        double roundNum = 1;

        while(enemy.isAwake() && goodGuys.size() > 0){

            if(roundNum == Math.floor(roundNum)){
                System.out.println("\nROUND " + Math.floor(roundNum) + "\n");
            }
            
            if(turn == USER){
                System.out.println("\nUSER TURN:\n");
                if(firstPlayer.isStunned()){
                    System.out.println("Your player is stunned. Your turn was automatically passed. ");
                    firstPlayer.unstun();
                }else{
                    firstPlayer = userTurn(firstPlayer, enemy);
                }
            }else{
                System.out.println("\nENEMY TURN:\n");
                if(firstPlayer.isStunned()){
                    System.out.println("The enemy is stunned. Their turn was automatically passed. ");
                    enemy.unstun();
                }else{
                    AITurn(enemy, firstPlayer);
                }
            }

            if(firstPlayer.isAwake() == false){
                goodGuys.remove(firstPlayer);
                System.out.println("Your player is knocked out! In the next turn, you must retreat!");
            }
            if(enemy.isAwake() == false){
                pokemons.remove(enemy);
                System.out.println("You defeated the enemy!");
            }

            // HEALING AND DISPLAYING STATS
            if(roundNum != Math.floor(roundNum)){
                heal(enemy);
                System.out.println("\nCurrent Pokemon's stats:\n"+firstPlayer.getStats()+"\n");
                System.out.println("Current Enemy's stats:\n"+enemy.getStats()+"\n");
            }

            roundNum += 0.5;
            turn = USER + ENEMY - turn;
        } 
    }

    public static void heal(Pokemon enemy){
        for(Pokemon p : goodGuys){
            p.heal();
        }
        enemy.heal();
    }

    public static Pokemon userTurn(Pokemon firstPlayer, Pokemon enemy){
        if(firstPlayer.isAwake()){
            String action = firstPlayer.pickAction(firstPlayer.canAfford(), canRetreat());
            if(action.equals("ATTACK")){
                int attackType = firstPlayer.pickAttack();
                firstPlayer.attack(enemy, attackType);
            }else if(action.equals("RETREAT")){
                return retreat(firstPlayer);
            }else{
                System.out.println("Your turn was passed. ");
            }
        }else{
            System.out.println("Your player was knocked out. Please pick another to take its place. ");
            return retreat(firstPlayer);
        }
       
        return firstPlayer;
    }

    public static void AITurn(Pokemon enemy, Pokemon firstPlayer){
        if(enemy.canAfford()){
            enemy.attack(firstPlayer, enemy.pickRandomAttack());
        }else{
            System.out.println("The enemy could not afford any attacks. Their turn was automatically passed. ");
        }
    }

    public static boolean canRetreat(){
        if(goodGuys.size() > 1){
            return true;
        }
        return false;
    }
    
    public static Pokemon getRandomEnemy(){
        Collections.shuffle(pokemons);
        Pokemon enemy = pokemons.get(0);
        return enemy;
    }

    public static Pokemon pickFirstPlayer(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you need to see your team? [y/n] ");
        String choice = scanner.nextLine();

        if(choice.equals("y")){
            displayTeam();
        }

        System.out.println("Pick the starting player: ");

        boolean valid = false;

        Pokemon firstPlayer = goodGuys.get(0);

        while(valid == false){
            int player = scanner.nextInt();
            if(player - 1 < goodGuys.size()){
                firstPlayer = goodGuys.get(player - 1);
                valid = true;
            }else{
                System.out.println("That was an invalid player. Try again. ");
            }
            
        }
    
        return firstPlayer;
    } 

    public static Pokemon retreat(Pokemon currentPlayer){
        Scanner scanner = new Scanner(System.in);

        ArrayList<Pokemon> canChoose = new ArrayList<Pokemon>(0);

        int list = 1;

        for(Pokemon p : goodGuys){
            if(p != currentPlayer){
                canChoose.add(p);
            }
        }

        for(Pokemon p : canChoose){
            System.out.println(list+". "+p);
            list++;
        }

        System.out.println("Pick the new starting player: ");

        boolean valid = false;

        Pokemon firstPlayer = canChoose.get(0); // temporary

        while(valid == false){
            int player = scanner.nextInt();
            if(player - 1 < canChoose.size()){
                firstPlayer = canChoose.get(player - 1);
                valid = true;
            }else{
                System.out.println("That was an invalid player. Try again. ");
            }
        }

        return firstPlayer;
    } 

    public static void healHP(ArrayList<Pokemon> goodGuys){
        for(Pokemon p : goodGuys){
            p.healHP();
        }

        System.out.println("The battle is over. All remaining Pokemon on your team have gained 20 HP.");
    }
    
}

class Pokemon {
    private String name, type, resistance, weakness;
    private int hp, energy, numAttacks;
    private boolean stunned, disabled = false;
    private ArrayList<Attack> attacks = new ArrayList<Attack>(0);

    public Pokemon(String [] pok){
        name = pok[0];
        energy = 50;
        hp = Integer.parseInt(pok[1]);
        type = pok[2];
        resistance = pok[3];
        weakness = pok[4];
        numAttacks = Integer.parseInt(pok[5]);
        
        int i = 1;

        while(9+i <= pok.length){
            attacks.add(new Attack(pok[5+i],Integer.parseInt(pok[6+i]),Integer.parseInt(pok[7+i]), pok[8+i]));
            i += 4;
        }
        
    }

    public String getName(){
        return name;
    }

    public boolean isStunned(){
        if(stunned){
            return true;
        }
        return false;
    }

    public void unstun(){
        stunned = false;
    }

    public boolean isAwake(){
        if(hp > 0){
            return true;
        }
        return false;
    }

    public void resetEnergy(){
        if(energy > 50){
            energy = 50;
        }
    }

    public void attack(Pokemon enemy, int attackType){
        Attack attack = attacks.get(attackType);

        int actualDamage = attack.damage;

        // RESOLVE ATTACK?
        int TRUE = 1;

        if(attack.attackName.equals(enemy.resistance)){
            actualDamage /= 2;
            System.out.println(enemy.name + " was resistant to " + attack.attackName + ". The damage was cut in half! ");
        }else if(attack.attackName.equals(enemy.weakness)){
            actualDamage *=2;
            System.out.println(enemy.name + " was weak to " + attack.attackName + ". The damage was doubled! ");
        }

        if(disabled){
            actualDamage = Math.max(actualDamage - 10, 0);
        }
        
        energy -= attack.cost;

        Random random = new Random();
        int chance = random.nextInt(2);

        if(attack.hasSpecial()){
            if(attack.special.equals("stun")){
                enemy.hp -= actualDamage;
                if(chance == TRUE){ // if 1, stun
                    enemy.stunned = true;
                    System.out.println(enemy.name + " was stunned!");
                }
            }else if(attack.special.equals("wild card")){
                if(chance == TRUE){ // if 1, attack is successful
                    enemy.hp -= actualDamage;
                }else{
                    System.out.println("Attack was not successful.");
                }
            }else if(attack.special.equals("wild storm")){
                wildStorm(enemy, attack, 0);
            }else if(attack.special.equals("disable")){
                enemy.disabled = true;
                System.out.println(enemy.name + " was disabled! Their attacks will do 10 less damage for the rest of the battle! ");
                enemy.hp -= actualDamage;
            }else if(attack.special.equals("recharge")){
                enemy.hp -= actualDamage;
                energy += 20;
                resetEnergy();
            }
        }else{
            enemy.hp -= actualDamage;
        }

        System.out.printf("%s attacked %s with %s and did %s damage while sustaining %s damage.\n", name, enemy.name, attack.attackName, actualDamage, attack.cost);
        if(enemy.isAwake() == false){
            System.out.println(enemy.name+" was defeated!");
        }
    }

    public void wildStorm(Pokemon enemy, Attack attack, int num){
        Random random = new Random();
        int TRUE = 1;
        int chance = random.nextInt(2);
        if(chance == TRUE){ // if 1, attack is successful
            enemy.hp -= attack.damage;
            wildStorm(enemy, attack, num++);
        }else{
            if(num > 0){
                System.out.println(num + "wild storm attacks were executed.");
            }else{
                System.out.println("The attack was unsuccessful. ");
            }
            
        }
    }
    public String pickAction(boolean attack, boolean canRetreat){
        Scanner scanner = new Scanner(System.in);

        if(attack && canRetreat){
            System.out.println("Pick an action [Attack (0), Retreat (1), or Pass (2)]: ");
        }else if(canRetreat && !attack){
            System.out.println("You cannot afford any attacks. Pick an action [Retreat (1) or Pass (2)]: ");
        }else if(attack && !canRetreat){
            System.out.println("You cannot retreat. Pick an action [Attack (0) or Pass (2)]: ");
        }else{
            System.out.println("You must pass. Please press 2. "); // ok this is gross i know i just don't feel like making it better
        }
        
        while(true){
            int action = scanner.nextInt();

            if(action == 0 && attack){
                return "ATTACK";
            }else if(action == 1 && canRetreat){
                return "RETREAT";
            }else if(action == 2){
                return "PASS";
            }else{
                System.out.println("You did not choose a valid action. Try again. ");
            } 
        }
    }

    public boolean canAfford(){
        // checks to see that the pokemon can afford any attacks at all
        for(Attack a : attacks){
            if(energy - a.cost > 0){
                return true;
            }
        }
        return false;
    }

    public int pickRandomAttack(){
        ArrayList<Attack> availableAttacks = new ArrayList<Attack>(0);

        for(Attack a : attacks){
            if(energy - a.cost > 0){
                availableAttacks.add(a);
            } 
        }

        Collections.shuffle(availableAttacks);

        return attacks.indexOf(availableAttacks.get(0));
    }

    public int pickAttack(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Here are this pokemon's available attacks: ");

        ArrayList<Attack> availableAttacks = new ArrayList<Attack>(0);

        for(Attack a : attacks){
            if(energy - a.cost > 0){
                availableAttacks.add(a);
            } 
        }

        for(int i = 1; i <= availableAttacks.size(); i++){
            System.out.println(i + ". " + availableAttacks.get(i-1));
        }

        System.out.println("Which attack would you like to deploy? ");

        int attack = scanner.nextInt();
        return attacks.indexOf(availableAttacks.get(attack-1));

    }

    public void heal(){
        energy += 10;
        resetEnergy();
    }

    public void healHP(){
        hp += 20;
    }

    public String getStats(){
        String output = String.format("Name: %-10s\nHP:%3s\nEnergy:%3s\n", name, hp, energy);
        return output;
    }

    @Override
    public String toString(){
        String output = String.format("Name: %-10s\n   HP:%3s\n   Type: %-10s\n   Energy:%3s\n   Resistance: %-10s\n   Weakness:%3s\n   Number of Attacks:%3s\n", name, hp, type, energy, resistance, weakness, numAttacks);
        return output;
    }
}

class Attack {
    public String attackName, special;
    public int cost, damage;

    public Attack(String attackName, int cost, int damage, String special){
        this.attackName = attackName;
        this.cost = cost;
        this.damage = damage;
        this.special = special;
    }

    public boolean hasSpecial(){
        if(special.equals(" ")){
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        String output = String.format("Attack name: %s\n   Cost: %s\n   Damage: %s\n   Special: %s\n", attackName, cost, damage, special);
        return output;
    }
}

