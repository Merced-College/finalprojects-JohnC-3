package src;
// John Chiero
// 4/21/2026
// Scene class - Represents one menu/screen in the text-based UI

public class Scene {
    private int sceneID;
    private String prompt;       // Text shown to the user
    private String[] choices;    // Menu options (for choice-based scenes)
    private int[] nextIDs;       // Which scene to go to after each choice

    public Scene(int sceneID, String prompt, String[] choices, int[] nextIDs) {
        this.sceneID = sceneID;
        this.prompt = prompt;
        this.choices = choices;
        this.nextIDs = nextIDs;
    }

    // Getters and Setters
    public int getSceneID() { return sceneID; }
    public void setSceneID(int sceneID) { this.sceneID = sceneID; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String[] getChoices() { return choices; }
    public void setChoices(String[] choices) { this.choices = choices; }

    public int[] getNextIDs() { return nextIDs; }
    public void setNextIDs(int[] nextIDs) { this.nextIDs = nextIDs; }
}