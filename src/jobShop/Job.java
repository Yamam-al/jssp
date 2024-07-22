package jobShop;

import java.util.List;

public class Job {
    private int id;
    private List<Operation> operations;

    public Job(int id, List<Operation> operations) {
        this.id = id;
        this.operations = operations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
    public boolean isCompleted() {
        for (Operation operation : operations) {
            if (!operation.isCompleted()) {
                return false;
            }
        }
        return true;
    }
}
