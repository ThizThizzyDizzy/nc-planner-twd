package net.ncplanner.plannerator.planner.file;
public enum FileFormat{
    PNG("Image", "PNG Image (.png)", "png"),
    HELLRAGE_REACTOR("JSON", "Hellrage Reactor File (.json)", "json"),
    NCPF("NCPF", "NuclearCraft Planner File", "ncpf"),
    ALL_PLANNER_FORMATS("NuclearCraft Planner File", "NuclearCraft Planner File", "ncpf", "json"),
    ALL_CONFIGURATION_FORMATS("NuclearCraft Configuration File", "NuclearCraft Configuration File", "ncpf", "cfg");//TODO hellrage .json
    public final String name;
    public final String description;
    public final String[] extensions;
    private FileFormat(String name, String description, String... extensions){
        this.name = name;
        this.description = description;
        this.extensions = extensions;
    }
}