public record Permission(String name, String resource, String description) {
    public Permission(String name, String resource, String description) {
        this.name = name.toLowerCase().replaceAll(" ", "");
        this.description = description.isEmpty() ? "-" : description;
        this.resource = resource.toLowerCase();
    }
    String format() {
        return String.format("%s on %s: %s",this.name,this.resource,this.description);
    }
    boolean matches(String namePattern, String resourcePattern) {
        return name.matches(namePattern) && resource.matches(resourcePattern);
    }
}