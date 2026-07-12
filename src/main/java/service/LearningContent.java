package service;

import java.util.List;

/** Curriculum content adapted from the SB015 notes supplied with this project. */
public final class LearningContent {
    public record Topic(String number, String graphicId, String title, String keyIdea, List<String> facts, String importance) {
        public Topic { facts = List.copyOf(facts); }
    }

    private LearningContent() { }

    public static List<Topic> topics() {
        return List.of(
                new Topic("01", "molecule", "Structure of water", "H₂O is a polar, bent molecule with a bond angle of 104.5°.",
                        List.of("One oxygen atom forms covalent bonds with two hydrogen atoms.",
                                "Oxygen carries a partial negative charge (δ−); hydrogen carries a partial positive charge (δ+).",
                                "A partially positive hydrogen atom forms a hydrogen bond with the partially negative oxygen atom of a neighbouring water molecule.",
                                "One water molecule can hydrogen-bond with a maximum of four other water molecules."),
                        "Water's polarity and hydrogen bonding produce its biologically important properties."),
                new Topic("02", "solvent", "Universal solvent", "Polar water dissolves many ionic and polar substances.",
                        List.of("The oxygen (δ−) region points towards positively charged Na⁺.",
                                "The hydrogen (δ+) regions point towards negatively charged Cl⁻.",
                                "Water molecules surround and separate the ions.",
                                "The sphere of water molecules around a dissolved ion is called a hydration shell."),
                        "Water provides an aqueous medium for cellular reactions and transports dissolved substances."),
                new Topic("03", "heat", "High specific heat capacity", "Water requires 4.2 J to change the temperature of 1 g by 1°C.",
                        List.of("A large amount of heat is absorbed to break hydrogen bonds as water warms.",
                                "A large amount of heat is released as hydrogen bonds form when water cools.",
                                "Consequently, water changes temperature relatively slowly."),
                        "Differential heating drives sea breeze during the day and land breeze at night: land changes temperature faster, while the sea changes temperature slowly."),
                new Topic("04", "vaporisation", "High latent heat of vaporisation", "About 580 cal is required to vaporise 1 g of water.",
                        List.of("Evaporation requires energy to break hydrogen bonds.",
                                "The escaping molecules carry heat away from the remaining surface.",
                                "After exercise, sweat absorbs heat from the body as it evaporates."),
                        "Evaporative cooling lowers a boy's body heat after exercise, prevents overheating and maintains optimum body temperature."),
                new Topic("05", "density", "Maximum density at 4°C", "Liquid water reaches its greatest density at 4°C.",
                        List.of("Above 4°C, water expands as it warms and contracts as it cools.",
                                "From 4°C to 0°C, water expands as an open crystalline lattice forms.",
                                "Ice is less dense than liquid water and therefore floats.",
                                "A frozen surface insulates the water beneath it."),
                        "Aquatic organisms can survive in liquid water below floating ice during winter."),
                new Topic("06", "cohesion", "Cohesion", "Cohesion is the attraction between water molecules through hydrogen bonds.",
                        List.of("Hydrogen bonds hold neighbouring water molecules together.",
                                "At the upper surface, air means there are no water molecules above, so attraction is unequal and the net force is inward.",
                                "Inside the body of water, each molecule is attracted equally in all directions.",
                                "Cohesion gives water high surface tension.",
                                "Surface tension measures how difficult it is to stretch or break a liquid surface."),
                        "Cohesion supports continuous water columns and contributes to biological water transport."));
    }
}
