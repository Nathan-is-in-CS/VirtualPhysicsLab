/**
 * CircuitExperiment Class - Concrete implementation of Experiment
 * 
 * Simulates DC circuit analysis using Ohm's Law and Power calculations
 * Students analyze voltage, current, resistance, and power in circuits
 * 
 * Learning Outcomes:
 * 1. Apply Ohm's Law (V = IR)
 * 2. Calculate electrical power (P = VI)
 * 3. Understand circuit behavior
 * 4. Predict circuit performance
 */
public class CircuitExperiment extends Experiment {
    // Circuit parameters
    private double voltage; // Volts
    private double resistance; // Ohms
    private double targetCurrent; // Amperes
    private double duration; // seconds
    
    // Calculated results
    private double current; // Amperes
    private double power; // Watts
    private double energyConsumed; // Joules
    
    // Accuracy parameters
    private double currentAccuracyThreshold;
    
    /**
     * Constructor for CircuitExperiment
     * @param studentName Name of student
     * @param voltage Supply voltage in volts
     * @param resistance Circuit resistance in ohms
     * @param diffLevel Difficulty level
     * @param targetCurrent Expected current value
     * @throws ExperimentException if parameters are invalid
     */
    public CircuitExperiment(String studentName, double voltage, double resistance,
                            DifficultyLevel diffLevel, double targetCurrent) throws ExperimentException {
        super(
            "Ohm's Law Verification & Power Analysis",
            ExperimentType.ELECTRICITY,
            studentName,
            diffLevel.getDefaultAttempts(),
            diffLevel,
            diffLevel == DifficultyLevel.BEGINNER ? 0 : 12,
            diffLevel != DifficultyLevel.BEGINNER,
            new String[]{
                "Understand Ohm's Law (V = IR)",
                "Calculate electrical current correctly",
                "Calculate power consumption (P = VI)",
                "Predict circuit behavior under load"
            }
        );
        
        // Validate parameters
        if (voltage <= 0) {
            throw new ExperimentException("Voltage must be positive. Given: " + voltage);
        }
        if (resistance <= 0) {
            throw new ExperimentException("Resistance must be positive. Given: " + resistance);
        }
        if (targetCurrent <= 0) {
            throw new ExperimentException("Target current must be positive. Given: " + targetCurrent);
        }
        
        this.voltage = voltage;
        this.resistance = resistance;
        this.targetCurrent = targetCurrent;
        this.duration = 60; // 60 seconds for circuit to stabilize
        
        // Set accuracy threshold based on difficulty
        this.currentAccuracyThreshold = diffLevel == DifficultyLevel.BEGINNER ? 0.5 : 0.1;
    }
    
    /**
     * Run the circuit experiment
     */
    @Override
    protected void runExperiment() throws ExperimentException {
        try {
            System.out.println("\n⚡ Initializing circuit...\n");
            displayCircuitParameters();
            
            // Simulate circuit startup time
            Thread.sleep(800);
            checkTimeLimit();
            
            // Calculate circuit values
            System.out.println("\n🔌 Applying voltage to circuit...\n");
            calculateCircuitValues();
            
            checkTimeLimit();
            
            // Display analysis
            System.out.println("\n📊 Circuit Analysis:");
            displayCircuitAnalysis();
            
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
     * Display circuit parameters
     */
    private void displayCircuitParameters() {
        System.out.println("📋 Circuit Configuration:");
        System.out.println("   Supply Voltage: " + voltage + " V");
        System.out.println("   Resistance: " + resistance + " Ω");
        System.out.println("   Expected Current: " + targetCurrent + " A");
        System.out.println("   Circuit Type: DC (Direct Current)");
    }
    
    /**
     * Calculate circuit values using Ohm's Law
     * I = V / R
     * P = V * I
     */
    private void calculateCircuitValues() throws ExperimentException {
        // Apply Ohm's Law: I = V / R
        if (resistance == 0) {
            throw new ExperimentException("Division by zero: Resistance cannot be zero");
        }
        current = voltage / resistance;
        
        // Calculate power: P = V * I
        power = voltage * current;
        
        // Calculate energy consumed over duration
        // Energy = Power * Time
        energyConsumed = power * (duration / 3600.0); // Convert seconds to hours for Wh
    }
    
    /**
     * Display circuit analysis results
     */
    private void displayCircuitAnalysis() {
        System.out.printf("   Calculated Current: %.3f A%n", current);
        System.out.printf("   Current Error: %.3f A%n", Math.abs(current - targetCurrent));
        System.out.printf("   Power Consumption: %.3f W%n", power);
        System.out.printf("   Energy Used (60s): %.6f Wh%n", energyConsumed);
        System.out.printf("   Power Factor: 1.0 (Resistive load)%n");
    }
    
    /**
     * Calculate performance score
     * @return score (0-100)
     */
    private double calculateScore() {
        // Current accuracy (max 50 points)
        double currentError = Math.abs(current - targetCurrent);
        double currentAccuracy = Math.max(0, 50 - (currentError / targetCurrent) * 50);
        
        // Power calculation accuracy (max 30 points)
        double expectedPower = voltage * targetCurrent;
        double powerError = Math.abs(power - expectedPower);
        double powerAccuracy = Math.max(0, 30 - (powerError / expectedPower) * 30);
        
        // Ohm's Law application (max 20 points)
        // Correct if I = V/R relationship holds
        double calculatedFromOhm = voltage / resistance;
        double ohmAccuracy = Math.abs(current - calculatedFromOhm) < 0.001 ? 20 : 0;
        
        double totalScore = currentAccuracy + powerAccuracy + ohmAccuracy;
        return Math.min(100, totalScore);
    }
    
    /**
     * Evaluate learning objectives
     * @return boolean array of objectives met
     */
    private boolean[] evaluateLearningObjectives() {
        boolean[] achieved = new boolean[4];
        
        // Objective 1: Understanding Ohm's Law
        achieved[0] = voltage > 0 && resistance > 0 && current > 0;
        
        // Objective 2: Calculate current correctly
        double expectedCurrent = voltage / resistance;
        double currentError = Math.abs(current - expectedCurrent);
        achieved[1] = currentError < 0.001;
        
        // Objective 3: Calculate power correctly
        double expectedPower = voltage * current;
        double powerError = Math.abs(power - expectedPower);
        achieved[2] = powerError < 0.01;
        
        // Objective 4: Predict circuit behavior
        achieved[3] = Math.abs(current - targetCurrent) <= currentAccuracyThreshold;
        
        return achieved;
    }
    
    // ==================== GETTERS ====================
    
    public double getCurrent() { return current; }
    public double getPower() { return power; }
    public double getVoltage() { return voltage; }
    public double getResistance() { return resistance; }
    public double getEnergyConsumed() { return energyConsumed; }
}