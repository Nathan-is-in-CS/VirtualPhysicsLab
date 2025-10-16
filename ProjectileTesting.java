class ProjectileTesting extends Experiment {
    // Physics parameters
    private final double velocity;
    private final double height;
    private final double angle;
    
    // Results
    private double flightTime;
    private double range;
    private double maxHeight;
    
    // Target values for scoring (what they should achieve)
    private final double targetRange;
    private final double rangeAccuracyThreshold;
    
    public ProjectileTesting(String studentName, double velocity, double height, double angle,
                           DifficultyLevel diffLevel, double targetRange) throws ExperimentException {
        super(
            "Projectile Motion Analysis",
            ExperimentType.MECHANICS,
            studentName,
            diffLevel == DifficultyLevel.BEGINNER ? 5 : 3, // More attempts for beginners
            diffLevel,
            diffLevel == DifficultyLevel.BEGINNER ? 0 : 15, // No time limit for beginners
            diffLevel != DifficultyLevel.BEGINNER, // Timed for non-beginners
            new String[]{
                "Understand projectile motion principles",
                "Apply kinematic equations correctly", 
                "Analyze the effect of launch angle on range",
                "Predict optimal angle for maximum range"
            }
        );
        
        this.velocity = velocity;
        this.height = height;
        this.angle = angle;
        this.targetRange = targetRange;
        this.rangeAccuracyThreshold = diffLevel == DifficultyLevel.BEGINNER ? 5.0 : 2.0;
    }
    
    @Override
    protected void runExperiment() throws ExperimentException {
        System.out.println("\n🎯 Analyzing projectile motion...");
        System.out.println("Initial velocity: " + velocity + " m/s");
        System.out.println("Launch height: " + height + " m");
        System.out.println("Launch angle: " + angle + "°");
        System.out.println("Target range: " + targetRange + " m");
        
        try {
            // Simulate thinking time and check time limit
            Thread.sleep(1000);
            checkTimeLimit();
            
            // Calculate projectile motion
            calculateTrajectory();
            
            // Check time limit again
            checkTimeLimit();
            
            // Evaluate performance
            double score = calculateScore();
            boolean[] objectives = evaluateLearningObjectives();
            
            // Finish experiment
            finishExperiment(score, objectives);
            
        } catch (InterruptedException e) {
            throw new ExperimentException("Experiment interrupted");
        }
    }
    
    private void calculateTrajectory() {
        double angleRad = Math.toRadians(angle);
        double g = 9.81;
        
        // Calculate flight time
        double vy = velocity * Math.sin(angleRad);
        flightTime = (vy + Math.sqrt(vy * vy + 2 * g * height)) / g;
        
        // Calculate range
        double vx = velocity * Math.cos(angleRad);
        range = vx * flightTime;
        
        // Calculate maximum height
        maxHeight = height + (vy * vy) / (2 * g);
        
        System.out.println("\n📊 Results:");
        System.out.println("Flight time: " + String.format("%.2f", flightTime) + " seconds");
        System.out.println("Range achieved: " + String.format("%.2f", range) + " meters");
        System.out.println("Maximum height: " + String.format("%.2f", maxHeight) + " meters");
    }
    
    private double calculateScore() {
        double rangeError = Math.abs(range - targetRange);
        double accuracyPercent = Math.max(0, 100 - (rangeError / targetRange) * 100);
        
        // Bonus points for optimal angle (around 45° for max range)
        double optimalAngle = 45.0;
        double angleError = Math.abs(angle - optimalAngle);
        double angleBonus = Math.max(0, 10 - angleError / 2);
        
        return Math.min(100, accuracyPercent + angleBonus);
    }
    
    private boolean[] evaluateLearningObjectives() {
        boolean[] achieved = new boolean[4];
        
        // Objective 1: Understanding principles (based on reasonable values)
        achieved[0] = velocity > 0 && angle >= 0 && angle <= 90;
        
        // Objective 2: Correct application (reasonable flight time and range)
        achieved[1] = flightTime > 0 && range > 0;
        
        // Objective 3: Analyze angle effect (close to target)
        double rangeError = Math.abs(range - targetRange);
        achieved[2] = rangeError <= rangeAccuracyThreshold;
        
        // Objective 4: Optimal angle understanding (close to 45°)
        achieved[3] = Math.abs(angle - 45.0) <= 10.0;
        
        return achieved;
    }
    
    // Getters for results
    public double getFlightTime() { return flightTime; }
    public double getRange() { return range; }
    public double getMaxHeight() { return maxHeight; }
}