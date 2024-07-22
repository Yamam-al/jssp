package jobShop;

import java.util.ArrayList;

public class Operation {
    private int id;
    private ArrayList<Machine> possibleMachines;
    private int processingTime;
    private int priority;
    private boolean isCompleted;

    public Operation(int id, int priority, ArrayList<Machine> possibleMachines, int processingTime) {
        this.id = id;
        this.possibleMachines = possibleMachines;
        this.processingTime = processingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Machine> getPossibleMachines() {
        return possibleMachines;
    }

    public void setPossibleMachines(ArrayList<Machine> possibleMachines) {
        this.possibleMachines = possibleMachines;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void markAsCompleted() {
        isCompleted = true;
    }
}
