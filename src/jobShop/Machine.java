package jobShop;

public class Machine {
    private int id;
    private String name;
    private boolean available;
    private int remainingTime;

    public Machine(int id, String name) {
        this.id = id;
        this.name = name;
        this.available = true;
        this.remainingTime = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void decrementRemainingTime() {
        if (remainingTime > 0) {
            remainingTime--;
        }
        if (remainingTime == 0) {
            setAvailable(true);
        }
    }
}
