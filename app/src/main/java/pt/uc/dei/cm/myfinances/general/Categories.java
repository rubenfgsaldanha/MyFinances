package pt.uc.dei.cm.myfinances.general;

import android.graphics.Color;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Categories {

    @PrimaryKey(autoGenerate = true)
    private int catId;

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "color")
    private int color;


    public Categories(int catId, String label, int color){
        this.catId=catId;
        this.color=color;
        this.label=label;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
