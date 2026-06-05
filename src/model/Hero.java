package model;

import enums.HeroType;
import java.util.ArrayList;
import java.util.List;

public class Hero {
    private String heroId;
    private String heroName;
    private int attack;
    private int defense;
    private HeroType heroType;
    private List<Equipment> equipmentList = new ArrayList<>();

    public Hero() {
    }

    public Hero(String heroId, String heroName, HeroType heroType, int attack, int defense) {
        this.heroId = heroId;
        this.heroName = heroName;
        this.heroType = heroType;
        this.attack = attack;
        this.defense = defense;
    }

    public String getHeroId() {
        return heroId;
    }

    public void setHeroId(String heroId) {
        this.heroId = heroId;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public void setHeroType(HeroType heroType) {
        this.heroType = heroType;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }
}
