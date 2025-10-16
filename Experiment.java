public abstract class Experiment {
    // Core experiment properties
    private final String expName;
    private final ExperimentType expType;
    private boolean isRunning;
    private final double duration; // Duration in minutes
    
    // Student and learning management
    private final String studentName;
    private final int attemptsMax;
    private final DifficultyLevel diffLevel;
    private int expCount; // Current attempt number
    private boolean hasStudentLearnt;
    
    // Timing management  
    private final boolean timedDuration; // Whether experiment has time limit
    private long startTime; // When experiment started
    // Learning analytics
    private double bestScore;
    private final String[] learningObjectives;
    private boolean[] objectivesMet;
    
    // Constructor
    public Experiment(String expName, ExperimentType expType, String studentName, 
                     int attemptsMax, DifficultyLevel diffLevel, double duration, 
                     boolean timedDuration, String[] learningObjectives) throws ExperimentException {
        
        validateInputs(expName, studentName, attemptsMax, duration);
        
        this.expName = expName;
        this.expType = expType;
        this.studentName = studentName;
        this.attemptsMax = attemptsMax;
        this.diffLevel = diffLevel;
        this.duration = duration;
        this.timedDuration = timedDuration;
        this.learningObjectives = learningObjectives;
        
        // Initialize state
        this.isRunning = false;
        this.expCount = 0;
        this.hasStudentLearnt = false;
        this.bestScore = 0.0;
        this.objectivesMet = new boolean[learningObjectives.length];
    }
    
    // Input validation
    private void validateInputs(String expName, String studentName, int attemptsMax, double duration) 
            throws ExperimentException {
        if (expName == null || expName.trim().isEmpty()) {
            throw new ExperimentException("Experiment name cannot be empty");
        }
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new ExperimentException("Student name cannot be empty");
        }
        if (attemptsMax <= 0) {
            throw new ExperimentException("Maximum attempts must be positive");
        }
        if (duration < 0) {
            throw new ExperimentException("Duration cannot be negative");
        }
    }
    
    // Start experiment with proper checks
    public final void startExperiment() throws ExperimentException {
        if (isRunning) {
            throw new ExperimentException("Experiment is already running!");
        }
        
        if (expCount >= attemptsMax) {
            throw new MaxAttemptsExceededException(
                "Maximum attempts (" + attemptsMax + ") reached. Contact instructor for reset.");
        }
        
        // Start the experiment
        isRunning = true;
        expCount++;
        startTime = System.currentTimeMillis();
        System.out.println("🚀 Starting Experiment: " + expName);
        System.out.println("👤 Student: " + studentName);
        System.out.println("📊 Attempt: " + expCount + "/" + attemptsMax);
        System.out.println("🎯 Level: " + diffLevel.getDescription());
        if (timedDuration && duration > 0) {
            System.out.println("⏱️  Time Limit: " + duration + " minutes");
        }
        
        // Call the specific experiment implementation
        runExperiment();
    }
    
    // Abstract method - each experiment implements differently
    protected abstract void runExperiment() throws ExperimentException;
    
    // Check if time has expired (call this during experiment)
    protected boolean checkTimeLimit() throws TimeExpiredException {
        if (timedDuration && duration > 0) {
            long elapsedMinutes = (System.currentTimeMillis() - startTime) / (1000 * 60);
            if (elapsedMinutes >= duration) {
                throw new TimeExpiredException("Time limit of " + duration + " minutes exceeded!");
            }
        }
        return false;
    }
    
    // End experiment and evaluate learning
    protected final void finishExperiment(double score, boolean[] objectivesAchieved) {
        isRunning = false;
        
        // Update best score
        if (score > bestScore) {
            bestScore = score;
        }
        
        // Update learning objectives
        for (int i = 0; i < objectivesMet.length && i < objectivesAchieved.length; i++) {
            if (objectivesAchieved[i]) {
                objectivesMet[i] = true;
            }
        }
        
        // Check if student has learned (all objectives met + good score)
        hasStudentLearnt = allObjectivesMet() && (bestScore >= getPassingScore());
        
        displayResults(score, objectivesAchieved);
    }
    
    // Display experiment results
    private void displayResults(double currentScore, boolean[] objectivesAchieved) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🎯 EXPERIMENT RESULTS");
        System.out.println("=".repeat(50));
        System.out.println("📊 Current Score: " + String.format("%.1f", currentScore) + "%");
        System.out.println("🏆 Best Score: " + String.format("%.1f", bestScore) + "%");
        System.out.println("🎯 Attempt: " + expCount + "/" + attemptsMax);
        
        // Show learning objectives progress
        System.out.println("\n📚 Learning Objectives Progress:");
        for (int i = 0; i < learningObjectives.length; i++) {
            String status = objectivesMet[i] ? "✅" : "❌";
            String current = (i < objectivesAchieved.length && objectivesAchieved[i]) ? " (achieved this attempt)" : "";
            System.out.println(status + " " + learningObjectives[i] + current);
        }
        
        // Overall learning status
        System.out.println("\n🎓 Learning Status: " + 
            (hasStudentLearnt ? "✅ MASTERED!" : "📚 Continue practicing"));
        
        // Encouragement and next steps
        if (hasStudentLearnt) {
            System.out.println("🌟 Excellent work! You've mastered this experiment.");
        } else if (expCount >= attemptsMax) {
            System.out.println("📝 You've used all attempts. Review concepts and try again later.");
        } else {
            int remaining = attemptsMax - expCount;
            System.out.println("💪 " + remaining + " attempts remaining. You can do this!");
        }
    }
    
    // Helper methods
    private boolean allObjectivesMet() {
        for (boolean met : objectivesMet) {
            if (!met) return false;
        }
        return true;
    }
    
    // Get passing score based on difficulty level
    private double getPassingScore() {
        return switch (diffLevel) {
            case BEGINNER -> 70.0;
            case INTERMEDIATE -> 75.0;
            case ADVANCED -> 80.0;
            case GRADUATE -> 85.0;
        };
    }
    
    // Getters and Setters with proper encapsulation
    public String getExpName() { return expName; }
    public ExperimentType getExpType() { return expType; }
    public boolean isRunning() { return isRunning; }
    public String getStudentName() { return studentName; }
    public int getExpCount() { return expCount; }
    public int getAttemptsMax() { return attemptsMax; }
    public boolean hasStudentLearnt() { return hasStudentLearnt; }
    public double getBestScore() { return bestScore; }
    public DifficultyLevel getDifficultyLevel() { return diffLevel; }
    
    // Reset experiment (instructor only)
    public void resetExperiment() throws ExperimentException {
        if (isRunning) {
            throw new ExperimentException("Cannot reset while experiment is running");
        }
        this.expCount = 0;
        this.hasStudentLearnt = false;
        this.bestScore = 0.0;
        this.objectivesMet = new boolean[learningObjectives.length];
        System.out.println("🔄 Experiment reset for student: " + studentName);
    }
}