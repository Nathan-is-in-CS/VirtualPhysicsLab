// Finals/DifficultyLevel.java

public enum DifficultyLevel {
    BEGINNER(1, "1st Year - Introductory"),
    INTERMEDIATE(2, "2nd-3rd Year - Intermediate"), 
    ADVANCED(3, "3rd-4th Year - Advanced"),
    GRADUATE(4, "Graduate Level");
    
    private final int level;
    private final String description;
    
    DifficultyLevel(int level, String description) {
        this.level = level;
        this.description = description;
    }
    
    public int getLevel() { return level; }
    public String getDescription() { return description; }

    int getDefaultAttempts() {
        return switch (this) {
            case BEGINNER -> 5;
            case INTERMEDIATE -> 3;
            case ADVANCED -> 3;
            case GRADUATE -> 2; 
        };
    }
}