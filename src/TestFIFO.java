import jobShop.Job;
import jobShop.Machine;
import jobShop.Scheduler;
import helpers.TestDataGenerator;

import java.util.ArrayList;
import java.util.Random;

public class TestFIFO {

    private static final Random random = new Random(3434);
    public static void main(String[] args) {
        // Generiere Testdaten
        TestDataGenerator generator = new TestDataGenerator();
        generator.generateTestData(5, 100, 5,random); // 5 Maschinen, 10 Jobs, 5 Operationen pro JobShop.Job

        ArrayList<Job> jobs = generator.getJobs();
        ArrayList<Machine> machines = generator.getMachines();

        // Initialisiere den JobShop.Scheduler
        Scheduler scheduler = new Scheduler(jobs, machines);

        // Plane die Operationen
        scheduler.schedule();

        // Überprüfe, ob alle Jobs abgeschlossen sind
        if (scheduler.isAllJobsCompleted()) {
            System.out.println("Alle Jobs wurden abgeschlossen in " + scheduler.getScheduledOperatingTime() + " Zeiteinheiten.");
            System.out.println("Verschwendete Zeit: " + scheduler.getWastedTime() + " Zeiteinheiten.");
        } else {
            System.out.println("Es sind noch nicht alle Jobs abgeschlossen.");
        }
    }
}