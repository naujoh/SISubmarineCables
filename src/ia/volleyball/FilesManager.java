package ia.volleyball;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class FilesManager {
    private RandomAccessFile master_f;
    private RandomAccessFile index_f;
    private StringBuffer str_b;
    private String masterf_path;
    private String indexf_path;
    private long _regsize;
    private Register reg;

    public FilesManager(String master_path, String index_path){
        this.reg = new Register();
        this.masterf_path = master_path;
        this.indexf_path = index_path;
    }

    public FilesManager(Register register, String master_path, String index_path){
        this.reg = register;
        this.masterf_path = master_path;
        this.indexf_path = index_path;
    }

    public void writeData(boolean overwrite) throws IOException{
        int logic_address = 1;
        this.master_f = new RandomAccessFile(this.masterf_path, "rw");
        this.index_f = new RandomAccessFile(this.indexf_path, "rw");

        if(!overwrite) {
            if (this.master_f.length() != 0) {
                setRegisterSize(this.master_f);
                this.master_f.seek(this.master_f.length());
                this.index_f.seek(this.index_f.length());
                logic_address = ((int) (this.master_f.length() / this._regsize)) + 1;
            }
        }

        this.str_b = new StringBuffer(this.reg.getKey());  //KEY
        this.str_b.setLength(7);
        this.index_f.writeChars(this.str_b.toString());    //KEY
        this.index_f.writeInt(logic_address);              //LOGIC_ADD
        this.master_f.writeChars(this.str_b.toString());
        this.str_b = new StringBuffer(this.reg.getCity()); //CITY
        this.str_b.setLength(50);
        this.master_f.writeChars(this.str_b.toString());
        this.str_b = new StringBuffer(this.reg.getCountry()); //COUNTRY
        this.str_b.setLength(50);
        this.master_f.writeChars(this.str_b.toString());

        for(int i = 0; i < reg.getCONNECTIONS_NUMBER(); i++){
            str_b = new StringBuffer(this.reg.connected_key[i]);
            this.str_b.setLength(7);
            this.master_f.writeChars(this.str_b.toString()); //KEY_CONNECTED_CITY
            master_f.writeDouble(this.reg.weight[i]); //WEIGHT
            this.str_b = new StringBuffer(this.reg.wire_name[i]);
            this.str_b.setLength(60);
            this.master_f.writeChars(this.str_b.toString()); //WIRE_NAME
        }
        master_f.close();
        index_f.close();
    }

    public void writeData(Register r) throws IOException{
        int logic_address = 1;
        this.master_f = new RandomAccessFile(this.masterf_path, "rw");
        this.index_f = new RandomAccessFile(this.indexf_path, "rw");

        //if(!overwrite) {
        if (this.master_f.length() != 0) {
            setRegisterSize(this.master_f);
            this.master_f.seek(this.master_f.length());
            this.index_f.seek(this.index_f.length());
            logic_address = ((int) (this.master_f.length() / this._regsize)) + 1;
        }
        //}
        this.str_b = new StringBuffer(r.getKey());
        this.str_b.setLength(7);
        this.index_f.writeChars(this.str_b.toString());     //KEY
        this.index_f.writeInt(logic_address);               //LOGIC ADDRESS
        writeDataFromRegisterObject(r);
        master_f.close();
        index_f.close();
    }

    public void updateData(){

    }

    public void deleteData(){

    }

    public ArrayList<Register> readSequential() throws IOException{
        ArrayList<Register> data_reg = new ArrayList<>();
        this.master_f = new RandomAccessFile(masterf_path, "r");
        while(master_f.getFilePointer() != master_f.length()){
            data_reg.add( setDataRegObject(this.reg));
            this.reg = new Register();
        }
        return data_reg;
    }

    public Register getRegisterFromFile(String key, boolean local_reg) throws IOException{
        Register r;
        String aux_key = "";
        long overflow;
        int logic_address;
        this.master_f = new RandomAccessFile(masterf_path, "r");
        this.index_f = new RandomAccessFile(indexf_path, "r");
        while(index_f.getFilePointer() != index_f.length()){
            for(int i = 0; i < 7; i++) { aux_key += index_f.readChar(); }
            if(aux_key.equals(key)){
                logic_address = index_f.readInt();
                setRegisterSize(master_f);
                overflow = (logic_address - 1) * this._regsize;
                master_f.seek(overflow);
                if(!local_reg) {
                    r = new Register();
                    return setDataRegObject(r);
                }else{
                    this.reg = new Register();
                    setDataRegObject(this.reg);
                }
                break;
            }
            aux_key = "";
            index_f.readInt();
        }
        return this.reg;
    }

    public void addConnection(Register r, String key) throws IOException{
        String aux_key = "";
        long overflow;
        int logic_address;
        this.index_f = new RandomAccessFile(indexf_path, "r");
        this.master_f= new RandomAccessFile(masterf_path, "rw");
        while(this.index_f.getFilePointer() != this.index_f.length()){
            for(int i = 0; i < 7; i++) { aux_key += index_f.readChar(); }
            if(aux_key.trim().equals(key)){
                logic_address = this.index_f.readInt();
                setRegisterSize(master_f);
                overflow = (logic_address - 1) * this._regsize;
                this.master_f.seek(overflow);
                writeDataFromRegisterObject(r);
                break;
            }
            aux_key = "";
            index_f.readInt();
        }
    }

    public void fileSeqToSeqIndex(boolean overwrite) throws IOException {
        String read;
        FileReader fr = new FileReader("register.txt");
        BufferedReader bf = new BufferedReader(fr);
        this.reg = new Register();

        while ((read = bf.readLine()) != null) {
            StringTokenizer tokens = new StringTokenizer(read, ",");
            while (tokens.hasMoreTokens()) {
                this.reg.setKey(tokens.nextToken());
                this.reg.setCity(tokens.nextToken());
                this.reg.setCountry(tokens.nextToken());

                for (int i = 0; i < this.reg.getCONNECTIONS_NUMBER(); i++) {
                    if (tokens.hasMoreTokens()) {
                        this.reg.connected_key[i] = tokens.nextToken();
                        this.reg.weight[i] = Double.parseDouble(tokens.nextToken());
                        this.reg.wire_name[i] = tokens.nextToken();
                    } else {
                        this.reg.connected_key[i] = "NULL";
                        this.reg.weight[i] = -1;
                        this.reg.wire_name[i] = "NULL";
                    }
                }
                writeData(overwrite);
            }
        }
    }

    private void setRegisterSize(RandomAccessFile file) throws IOException {
        int i;
        for (i = 0; i < 7; i++) { file.readChar(); }  //KEY
        for (i = 0; i < 50; i++) { file.readChar(); } //CITY
        for (i = 0; i < 50; i++) { file.readChar(); } //COUNTRY
        for(int j = 0; j < this.reg.getCONNECTIONS_NUMBER(); j++){
            for (i = 0; i < 7; i++) { file.readChar(); } //CONNECTED_CITY_KEY
            file.readDouble();  //WEIGHT
            for (i = 0; i < 60; i++) { file.readChar(); } //CABLE_NAME
        }
        this._regsize = file.getFilePointer();
    }

    private Register setDataRegObject(Register r) throws IOException{
        int i;
        r.setKey("");
        r.setCity("");
        r.setCountry("");
        for(i = 0; i < 7; i++) { r.setKey(r.getKey() + master_f.readChar()); }
        for(i = 0; i < 50; i++) { r.setCity(r.getCity() + master_f.readChar()); }
        for(i = 0; i < 50; i++) { r.setCountry(r.getCountry() + master_f.readChar()); }
        for(int j = 0; j < r.getCONNECTIONS_NUMBER(); j++){
            r.connected_key[j] = "";
            r.wire_name[j] = "";
            for(i = 0; i < 7; i++) { r.connected_key[j] += master_f.readChar(); }
            r.weight[j] = master_f.readDouble();
            for(i = 0; i < 60; i++) { r.wire_name[j] += master_f.readChar(); }
        }
        return r;
    }

    public boolean keyExists(String key)throws IOException{
        this.index_f = new RandomAccessFile(indexf_path, "r");
        String aux_key = "";
        if(this.index_f.length() != 0){
            while(index_f.getFilePointer() != index_f.length()){
                for(int i = 0; i < 7; i++) { aux_key += index_f.readChar(); }
                if(aux_key.trim().equals(key))
                    return true;
                aux_key = "";
                index_f.readInt();
            }
        }
        return false;
    }

    public void writeDataFromRegisterObject(Register r) throws IOException{
        this.str_b = new StringBuffer(r.getKey());
        this.str_b.setLength(7);
        this.master_f.writeChars(this.str_b.toString());    //KEY
        this.str_b = new StringBuffer(r.getCity());
        this.str_b.setLength(50);
        this.master_f.writeChars(this.str_b.toString());    //CITY
        this.str_b = new StringBuffer(r.getCountry());
        this.str_b.setLength(50);
        this.master_f.writeChars(this.str_b.toString());             //COUNTRY

        for (int i = 0; i < r.getCONNECTIONS_NUMBER(); i++) {
            str_b = new StringBuffer(r.connected_key[i]);
            this.str_b.setLength(7);
            this.master_f.writeChars(this.str_b.toString()); //KEY_CONNECTED_KEY
            master_f.writeDouble(r.weight[i]);               //WEIGHT
            this.str_b = new StringBuffer(r.wire_name[i]);
            this.str_b.setLength(60);
            this.master_f.writeChars(this.str_b.toString()); //WIRE_NAME
        }
    }

    public String getCityName(String key){
        Register r = null;
        try{

            r=getRegisterFromFile(key, false);

        }catch(Exception e){
            System.out.println("Ocurrio un error al obtener la llave");
        }
        return r.getCity().trim();
    }

    public Register getReg() {
        return reg;
    }
}
