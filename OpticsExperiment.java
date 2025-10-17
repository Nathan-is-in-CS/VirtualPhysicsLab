/**
 * OpticsExperiment Class - Concrete implementation of Experiment
 * 
 * Simulates lens optics using the lens equation
 * Students analyze image formation with converging and diverging lenses
 * 
 * Learning Outcomes:
 * 1. Apply lens equation (1/f = 1/do + 1/di)
 * 2. Calculate magnification
 * 3. Determine image characteristics
 * 4. Understand optical instrument principles
 */
public class OpticsExperiment extends Experiment {
    // Optical parameters
    private double focalLength; // cm
    private double objectDistance; // cm
    private double targetImageDistance; // cm
    
    // Calculated results
    private double imageDistance; // cm
    private double magnification;
    private String imageType; // Real/Virtual, Upright/Inverted, Magnified/Diminished
    
    // Accuracy parameters
    private double distanceAccuracyThreshold;
    
    /**
     * Constructor for OpticsExperiment
     * @param studentName Name of student
     * @param focalLength Focal length of lens in cm
     * @param objectDistance Object distance from lens in cm
     * @param diffLevel Difficulty level
     * @param targetImageDistance Expected image distance
     * @throws ExperimentException if parameters are invalid
     */
    public OpticsExperiment(String studentName, double focalLength, double objectDistance,
                           DifficultyLevel diffLevel, double targetImageDistance) throws ExperimentException {
        super(
            "Lens Optics & Image Formation",
            ExperimentType.OPTICS,
            studentName,
            diffLevel.getDefaultAttempts(),
            diffLevel,
            diffLevel == DifficultyLevel.BEGINNER ? 0 : 15,
            diffLevel != DifficultyLevel.BEGINNER,
            new String[]{
                "Apply the thin lens equation",
                "Calculate image distance correctly",
                "Determine magnification accurately",
                "Predict image characteristics"
            }
        );
        
        // Validate parameters
        if (focalLength <= 0) {
            throw new ExperimentException("Focal length must be positive. Given: " + focalLength);
        }
        if (objectDistance <= 0) {
            throw new ExperimentException("Object distance must be positive. Given: " + objectDistance);
        }
        if (objectDistance == focalLength) {
            throw new ExperimentException("Object at focal point creates image at infinity");
        }
        
        this.focalLength = focalLength;
        this.objectDistance = objectDistance;
        this.targetImageDistance = targetImageDistance;
        
        // Set accuracy threshold based on difficulty
        this.distanceAccuracyThreshold = diffLevel == DifficultyLevel.BEGINNER ? 2.0 : 0.5;
    }
    
    /**
     * Run the optics experiment
     */
    @Override
    protected void runExperiment() throws ExperimentException {
        try {
            System.out.println("\n🔍 Setting up optical apparatus...\n");
            displayOpticalSetup();
            
            // Simulate setup time
            Thread.sleep(600);
            checkTimeLimit();
            
            // Calculate image properties
            System.out.println("\n🔬 Analyzing light path through lens...\n");
            calculateImageProperties();
            
            checkTimeLimit();
            
            // Display results
            System.out.println("\n📊 Optical Analysis:");
            displayOpticalAnalysis();
            
            // Evaluate performance
            double score = calculateScore();
            boolean[] objectives = evaluateLearningObjectives();
            
            finishExperiment(score, objectives);
            
        } catch (InterruptedException e) {
            throw new ExperimentException("Experiment interrupted: " + e.getMessage());
        } catch (TimeExpiredException e) {
            throw new ExperimentException("Experiment timeout: " + e.getMessage());
        }
    }
    
    /**
     * Display optical setup parameters
     */
    private void displayOpticalSetup() {
        System.out.println("📋 Optical Setup:");
        System.out.println("   Lens Type: Converging (Biconvex)");
        System.out.println("   Focal Length: " + focalLength + " cm");
        System.out.println("   Object Distance: " + objectDistance + " cm");
        System.out.println("   Expected Image Distance: " + targetImageDistance + " cm");
    }
    
    /**
     * Calculate image properties using lens equation
     * Thin lens equation: 1/f = 1/do + 1/di
     * Magnification: m = -di/do
     */
    private void calculateImageProperties() throws ExperimentException {
        // Calculate image distance using lens equation
        // 1/di = 1/f - 1/do
        // di = 1 / (1/f - 1/do)
        
        double denominator = (1.0 / focalLength) - (1.0 / objectDistance);
        
        if (Math.abs(denominator) < 0.0001) {
            throw new ExperimentException("Invalid lens configuration: Object too close to focal point");
        }
        
        imageDistance = 1.0 / denominator;
        
        // Calculate magnification
        // m = -di/do (negative indicates inverted image)
        magnification = -imageDistance / objectDistance;
        
        // Determine image type
        determineImageType();
    }
    
    /**
     * Determine image characteristics
     */
    private void determineImageType() {
        StringBuilder type = new StringBuilder();
        
        // Real or Virtual
        if (imageDistance > 0) {
            type.append("Real, ");
        } else {
            type.append("Virtual, ");
        }
        
        // Upright or Inverted
        if (magnification > 0) {
            type.append("Upright, ");
        } else {
            type.append("Inverted, ");
        }
        
        // Magnified or Diminished
        if (Math.abs(magnification) > 1) {
            type.append("Magnified");
        } else if (Math.abs(magnification) < 1) {
            type.append("Diminished");
        } else {
            type.append("Same size");
        }
        
        imageType = type.toString();
    }
    
    /**
     * Display optical analysis results
     */
    private void displayOpticalAnalysis() {
        System.out.printf("   Calculated Image Distance: %.3f cm%n", imageDistance);
        System.out.printf("   Distance Error: %.3f cm%n", Math.abs(imageDistance - targetImageDistance));
        System.out.printf("   Magnification: %.3fx%n", magnification);
        System.out.printf("   Magnification Type: " + (magnification < 0 ? "Inverted" : "Upright") + "%n");
        System.out.printf("   Image Characteristics: %s%n", imageType);
    }
    
    /**
     * Calculate performance score
     * @return score (0-100)
     */
    private double calculateScore() {
        // Image distance accuracy (max 50 points)
        double distanceError = Math.abs(imageDistance - targetImageDistance);
        double distanceAccuracy = Math.max(0, 50 - (distanceError / Math.abs(targetImageDistance)) * 50);
        
        // Magnification accuracy (max 30 points)
        double expectedMagnification = -targetImageDistance / objectDistance;
        double magError = Math.abs(magnification - expectedMagnification);
        double magnificationAccuracy = Math.max(0, 30 - (magError * 10));
        
        // Lens equation application (max 20 points)
        // Verify: 1/f = 1/do + 1/di
        double lensCheck = (1.0 / focalLength) - (1.0 / objectDistance) - (1.0 / imageDistance);
        double lensAccuracy = Math.abs(lensCheck) < 0.001 ? 20 : 0;
        
        double totalScore = distanceAccuracy + magnificationAccuracy + lensAccuracy;
        return Math.min(100, totalScore);
    }
    
    /**
     * Evaluate learning objectives
     * @return boolean array of objectives met
     */
    private boolean[] evaluateLearningObjectives() {
        boolean[] achieved = new boolean[4];
        
        // Objective 1: Apply lens equation
        double lensCheck = (1.0 / focalLength) - (1.0 / objectDistance) - (1.0 / imageDistance);
        achieved[0] = Math.abs(lensCheck) < 0.001;
        
        // Objective 2: Calculate image distance correctly
        double expectedDistance = 1.0 / ((1.0 / focalLength) - (1.0 / objectDistance));
        double distanceError = Math.abs(imageDistance - expectedDistance);
        achieved[1] = distanceError < 0.01;
        
        // Objective 3: Calculate magnification accurately
        double expectedMagnification = -imageDistance / objectDistance;
        double magError = Math.abs(magnification - expectedMagnification);
        achieved[2] = magError < 0.01;
        
        // Objective 4: Predict image characteristics
        achieved[3] = Math.abs(imageDistance - targetImageDistance) <= distanceAccuracyThreshold;
        
        return achieved;
    }
    
    // ==================== GETTERS ====================
    
    public double getImageDistance() { return imageDistance; }
    public double getMagnification() { return magnification; }
    public String getImageType() { return imageType; }
    public double getFocalLength() { return focalLength; }
    public double getObjectDistance() { return objectDistance; }
}