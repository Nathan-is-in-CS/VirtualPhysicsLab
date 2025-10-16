public class VirtualPhysicsLab {
    public static void main(String[] args) {
        try {
            // Create experiment for different difficulty levels
            System.out.println("🧪 VIRTUAL PHYSICS LAB - STUDENT LEARNING SYSTEM");
            System.out.println("=" .repeat(60));
            
            // Beginner student
            ProjectileTesting beginnerExp = new ProjectileTesting(
                "Alice Johnson", 20.0, 1.5, 30.0, 
                DifficultyLevel.BEGINNER, 35.0
            );
            
            beginnerExp.startExperiment();
            
            System.out.println("\n" + "-".repeat(60) + "\n");
            
            // Advanced student  
            ProjectileTesting advancedExp = new ProjectileTesting(
                "Bob Chen", 25.0, 2.0, 45.0,
                DifficultyLevel.ADVANCED, 63.8
            );
            
            advancedExp.startExperiment();
            
        } catch (ExperimentException e) {
            System.out.println("❌ Experiment Error: " + e.getMessage());
        }
    }
}