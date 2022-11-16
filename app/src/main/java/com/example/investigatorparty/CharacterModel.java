package com.example.investigatorparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Entity: Represents a table within the database.
 *
 * Get / Set field values
 * Created by Steve on 1/27/2018.
 */

@Entity
public class CharacterModel {
    @PrimaryKey(autoGenerate = true)
    private int characterId;

    private String name = "unnamed";
    private String image = "undefined";
    private int age = 0;
    private String profession = "undefined";
    private String birthplace = "undefined";
    private int sanityPoints = 0;
    private int magicPoints = 0;
    private int hitPoints = 0;

    public CharacterModel(String name)
    {
        this.name = name;
    }


    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public int getSanityPoints() {
        return sanityPoints;
    }

    public void setSanityPoints(int sanityPoints) {
        this.sanityPoints = sanityPoints;
    }

    public int getMagicPoints() {
        return magicPoints;
    }

    public void setMagicPoints(int magicPoints) {
        this.magicPoints = magicPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
}
