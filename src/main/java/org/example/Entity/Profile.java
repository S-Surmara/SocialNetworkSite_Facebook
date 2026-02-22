package org.example.Entity;

import java.util.List;

public class Profile {
    private String pfp;
    private String bio;
    private List<String> interests;

    public Profile(String pfp, String bio, List<String> interests) {
        this.pfp       = pfp;
        this.bio       = bio;
        this.interests = interests;
    }

    public String       getPfp()       { return pfp; }
    public String       getBio()       { return bio; }
    public List<String> getInterests() { return interests; }

    public void setPfp(String pfp)                   { this.pfp = pfp; }
    public void setBio(String bio)                   { this.bio = bio; }
    public void setInterests(List<String> interests) { this.interests = interests; }
}

