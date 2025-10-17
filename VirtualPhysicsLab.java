import java.util.Scanner;

public class VirtualPhysicsLab {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("🧪 VIRTUAL PHYSICS LAB - STUDENT LEARNING SYSTEM");
        System.out.println("=".repeat(60));

        try {
            System.out.print("👤 Enter your name: ");
            String studentName = sc.nextLine();

            // Difficulty selection
            System.out.println("\nSelect Difficulty Level:");
            for (DifficultyLevel level : DifficultyLevel.values()) {
                System.out.println("  " + level.ordinal() + " - " + level.name() +
                        " (" + level.getDescription() + ")");
            }
            System.out.print("Enter difficulty number: ");
            int diffChoice = sc.nextInt();
            sc.nextLine(); // consume newline

            DifficultyLevel diffLevel = DifficultyLevel.values()[Math.max(0, Math.min(diffChoice, DifficultyLevel.values().length - 1))];

            // Experiment selection
            System.out.println("\nSelect an Experiment:");
            System.out.println("  1 - Projectile Motion");
            System.out.println("  2 - Circuit Analysis");
            System.out.println("  3 - Optics (Lens Equation)");
            System.out.print("Enter your choice: ");
            int expChoice = sc.nextInt();

            Experiment experiment = null;

            switch (expChoice) {
                case 1 -> {
                    System.out.println("\nPROJECTILE MOTION EXPERIMENT");
                    System.out.print("Enter initial velocity (m/s): ");
                    double velocity = sc.nextDouble();
                    System.out.print("Enter launch height (m): ");
                    double height = sc.nextDouble();
                    System.out.print("Enter launch angle (degrees): ");
                    double angle = sc.nextDouble();
                    System.out.print("Enter target range (m): ");
                    double targetRange = sc.nextDouble();

                    experiment = new ProjectileTesting(
                        studentName, velocity, height, angle,
                        diffLevel, targetRange
                    );
                }

                case 2 -> {
                    System.out.println("\nCIRCUIT EXPERIMENT");
                    System.out.print("Enter supply voltage (V): ");
                    double voltage = sc.nextDouble();
                    System.out.print("Enter resistance (Ω): ");
                    double resistance = sc.nextDouble();
                    System.out.print("Enter expected current (A): ");
                    double targetCurrent = sc.nextDouble();

                    experiment = new CircuitExperiment(
                        studentName, voltage, resistance,
                        diffLevel, targetCurrent
                    );
                }

                case 3 -> {
                    System.out.println("\nOPTICS EXPERIMENT");
                    System.out.print("Enter focal length (cm): ");
                    double focalLength = sc.nextDouble();
                    System.out.print("Enter object distance (cm): ");
                    double objectDistance = sc.nextDouble();
                    System.out.print("Enter expected image distance (cm): ");
                    double targetImageDistance = sc.nextDouble();

                    experiment = new OpticsExperiment(
                        studentName, focalLength, objectDistance,
                        diffLevel, targetImageDistance
                    );
                }

                default -> {
                    System.out.println("Invalid experiment choice.");
                    System.exit(0);
                }
            }

            System.out.println("\n" + "-".repeat(60));
            experiment.startExperiment();

        } catch (ExperimentException e) {
            System.out.println("❌ Experiment Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Input Error: Please check your entries.");
        } finally {
            sc.close();
        }
    }
}
