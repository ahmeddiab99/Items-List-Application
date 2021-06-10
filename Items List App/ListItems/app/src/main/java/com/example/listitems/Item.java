package com.example.listitems;

public class Item{
    public int ID;
    public int LISTID;
    public int NAME;

    public Item(){
        ID = 0;
        LISTID = 0;
        NAME = 0;
    }

    public Item(int id, int listId, int name){
        setId(id);
        setListId(listId);
        setName(name);
    }

    public void setId(int id){
        ID = id;
    }
    public void setListId(int listId){
        LISTID = listId;
    }
    public void setName(int name){
        NAME = name;
    }
    public int getId(){
        return ID;
    }
    public int getListId(){
        return LISTID;
    }
    public int getName(){
        return NAME;
    }

    public String toString(){
        return ID + "\t\t" + LISTID + "\t\t" + NAME;
    }
}
