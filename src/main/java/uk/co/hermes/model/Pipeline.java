package uk.co.hermes.model;

import org.json.JSONObject;

public class Pipeline {
    private int id;
    private String name;
    private boolean isPaused;
    private boolean isPublic;
    private String teamName;

    public Pipeline(int id, String name, boolean isPaused, boolean isPublic, String teamName) {
        this.id = id;
        this.name = name;
        this.isPaused = isPaused;
        this.isPublic = isPublic;
        this.teamName = teamName;
    }

    public Pipeline(JSONObject obj) {
        if(obj != null &&
                obj.has("id") &&
                obj.has("name") &&
                obj.has("paused") &&
                obj.has("public") &&
                obj.has("team_name")) {
            this.id = obj.getInt("id");
            this.name = obj.getString("name");
            this.isPaused = obj.getBoolean("paused");
            this.isPublic = obj.getBoolean("public");
            this.teamName = obj.getString("team_name");

        } else {throw new RuntimeException("Empty pipeline object");}
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getTeamName() {
        return teamName;
    }
}
