
package ia.volleyball;

import java.util.HashMap;

public class Data {

    HashMap<String, Data> s;
    public String key;
    public String pKey;
    public String pCity;
    public double f;
    public double g;
    public String wire;
    
    
    //BestFirsMethod
    public Data(String key, String pKey, String pCity, double f) {
        this.key = key;
        this.pKey = pKey;
        this.pCity = pCity;
        this.f = f;
    }

    public Data(String pKey, String pCity, double f) {
        this.pKey = pKey;
        this.pCity = pCity;
        this.f = f;
    }

    //A*
    public Data(String key, String pKey, String pCity, String wire, double f, double g) {
        this.key = key;
        this.pKey = pKey;
        this.pCity = pCity;
        this.f = f;
        this.g = g;
        this.wire = wire;
    }

    
    //GrafosO
    public Data(HashMap<String, Data> s, String pKey, String pCity, double f) {
        this.s = s;
        this.pKey = pKey;
        this.f = f;
    }

    public Data(String pKey, String pCity) {
        this.pKey = pKey;
        this.pCity = pCity;
    }

    public Data(String pKey, double f) {
        this.pKey = pKey;
        this.f = f;
    }
}
