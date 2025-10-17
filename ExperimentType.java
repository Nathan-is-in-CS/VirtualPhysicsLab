public enum ExperimentType {
    MECHANICS("Mechanics"),
    OPTICS("Optics"), 
    ELECTRICITY("Electricity & Magnetism"),
    WAVES("Wave Physics"),
    THERMODYNAMICS("Thermodynamics"),
    MODERN_PHYSICS("Modern Physics");
    
    private final String displayName;
    
    ExperimentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
}