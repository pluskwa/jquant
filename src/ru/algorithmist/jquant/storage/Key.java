package ru.algorithmist.jquant.storage;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class Key {

    private String[] data;

    public Key(String[] data) {
        this.data = data;
    }

    public String[] getData() {
        return data;
    }

    public String toString(){
        StringBuilder res = new StringBuilder();
        for(int i=0; i<data.length; i++){
            if (i!=0) res.append('\'');
            res.append(data[i]);
        }
        return res.toString();
    }
}
