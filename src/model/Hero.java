package model;
import enums.HeroType;
import java.util.List;

public class Hero {
    private String heroId;
    private String heroName;
    private int attack;
    private int defense;
    private HeroType heroType;
    private List<Equipment> equipmentList;
}
