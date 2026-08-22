package model;

public enum Achievement {
    KNOWLEDGE_EXPLORER("Knowledge Explorer", "Complete all six Learning Notes topics"),
    WATER_BEGINNER("Water Beginner", "Answer your first question correctly"),
    HYDROGEN_BOND_EXPERT("Hydrogen Bond Expert", "Complete The Water Molecule"),
    MASTER_CHEMIST("Master Chemist", "Complete Universal Solvent"),
    HEAT_MASTER("Heat Master", "Complete Specific Heat Capacity"),
    COOLING_HERO("Cooling Hero", "Complete Vaporisation"),
    ICE_SCIENTIST("Ice Scientist", "Complete Maximum Density"),
    COHESION_CHAMPION("Cohesion Champion", "Complete Cohesion"),
    WATER_GENIUS("Water Genius", "Finish with 100% quiz accuracy");
    private final String title, description;
    Achievement(String title, String description) { this.title=title; this.description=description; }
    public String title(){ return title; } public String description(){ return description; }
}
