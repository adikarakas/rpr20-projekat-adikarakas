package ba.unsa.etf.rpr;

import java.util.ArrayList;

public class League {
    private String name;
    private int numberOfClubs;
    private boolean isScheduleRandom;
    private ArrayList<Club> clubs = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();
    private ArrayList<String> managers = new ArrayList<>();

    public League(String name, int numberOfClubs, boolean isScheduleRandom) {
        this.name=name;
        this.numberOfClubs=numberOfClubs;
        this.isScheduleRandom=isScheduleRandom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Club> getClubs() {
        return clubs;
    }

    public void setClubs(ArrayList<Club> clubs) {
        this.clubs = clubs;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public ArrayList<String> getManagers() {
        return managers;
    }

    public void setManagers(ArrayList<String> managers) {
        this.managers = managers;
    }

    public int getNumberOfClubs() {
        return numberOfClubs;
    }

    public void setNumberOfClubs(int numberOfClubs) {
        this.numberOfClubs = numberOfClubs;
    }

    public boolean isScheduleRandom() {
        return isScheduleRandom;
    }

    public void setScheduleRandom(boolean scheduleRandom) {
        isScheduleRandom = scheduleRandom;
    }

    public void addClub (Club c) {
        clubs.add(c);
    }

//    public static void registerMatch(Club c1, Club c2, ArrayList<Goal> goals) {

  //  }
}
