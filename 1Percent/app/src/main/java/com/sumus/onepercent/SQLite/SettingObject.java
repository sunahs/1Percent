package com.sumus.onepercent.SQLite;

/**
 * Created by SUNAH on 2017-03-07.
 */

public class SettingObject {
    int set_id;
    int set_push, set_vibe;

    public SettingObject(int set_id, int set_push, int set_vibe) {
        this.set_id = set_id;
        this.set_push = set_push;
        this.set_vibe = set_vibe;
    }

    public SettingObject(int set_push, int set_vibe) {
        this.set_push = set_push;
        this.set_vibe = set_vibe;
    }

    public int getSet_id() {
        return set_id;
    }

    public void setSet_id(int set_id) {
        this.set_id = set_id;
    }

    public int getSet_push() {
        return set_push;
    }

    public void setSet_push(int set_push) {
        this.set_push = set_push;
    }

    public int getSet_vibe() {
        return set_vibe;
    }

    public void setSet_vibe(int set_vibe) {
        this.set_vibe = set_vibe;
    }

    @Override
    public String toString() {
        return "SettingObject{" +
                "set_id=" + set_id +
                ", set_push=" + set_push +
                ", set_vibe=" + set_vibe +
                '}';
    }
}
