package src;
// John Chiero
// 4/20/2026
// Scene class

public class Scene {
    private int sceneID;
    private String prompt;
    private String[] choices;
    private int[] nextIDs;

    public Scene(int sceneID, String prompt, String[] choices, int[] nextIDs) {
        this.sceneID = sceneID;
        this.prompt = prompt;
        this.choices = choices;
        this.nextIDs = nextIDs;
    }

    public int getSceneID() {
        return sceneID;
    }

    public void setSceneID(int sceneID) {
        this.sceneID = sceneID;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public int[] getNextIDs() {
        return nextIDs;
    }

    public void setNextIDs(int[] nextIDs) {
        this.nextIDs = nextIDs;
    }
}