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
            master_f.writeInt(this.reg.available[i]); //AVAILABLE
            str_b = new StringBuffer(this.reg.connected_key[i]);
            this.str_b.setLength(7);
            this.master_f.writeChars(this.str_b.toString()); //KEY_CONNECTED_CITY
            str_b = new StringBuffer(this.reg.connected_city[i]);
            this.str_b.setLength(50);
            this.master_f.writeChars(this.str_b.toString()); //CONNECTED_CITY
            master_f.writeDouble(this.reg.weight[i]); //WEIGHT
            this.str_b = new StringBuffer(this.reg.wire_name[i]);
            this.str_b.setLength(60);
            this.master_f.writeChars(this.str_b.toString()); //WIRE_NAME
        }
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
            setDataRegObject();
            data_reg.add(this.reg);
            this.reg = new Register();
        }
        return data_reg;
    }

    //TODO Create a way to convert index file to tree
    public Register getRegisterFromFile(String key) throws IOException{
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
                this.reg = new Register();
                setDataRegObject();
                break;
            }
            aux_key = "";
            index_f.readInt();
        }
        return this.reg;
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
                        this.reg.available[i] = Integer.parseInt(tokens.nextToken());
                        this.reg.connected_key[i] = tokens.nextToken();
                        this.reg.connected_city[i] = tokens.nextToken();
                        this.reg.weight[i] = Double.parseDouble(tokens.nextToken());
                        this.reg.wire_name[i] = tokens.nextToken();
                    } else {
                        this.reg.available[i] = 0;
                        this.reg.connected_key[i] = "NULL";
                        this.reg.connected_city[i] = "NULL";
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
            file.readInt(); //CONNECTED
            for (i = 0; i < 7; i++) { file.readChar(); } //CONNECTED_CITY_KEY
            for (i = 0; i < 50; i++) { file.readChar(); } //CONNECTED_CITY
            file.readDouble();  //WEIGHT
            for (i = 0; i < 60; i++) { file.readChar(); } //CABLE_NAME
        }
        this._regsize = file.getFilePointer();
    }

    private void setDataRegObject() throws IOException{
        int i;
        this.reg.setKey("");
        this.reg.setCity("");
        this.reg.setCountry("");
        for(i = 0; i < 7; i++) { this.reg.setKey(this.reg.getKey() + master_f.readChar()); }
        for(i = 0; i < 50; i++) { this.reg.setCity(reg.getCity() + master_f.readChar()); }
        for(i = 0; i < 50; i++) { this.reg.setCountry(this.reg.getCountry() + master_f.readChar()); }
        for(int j = 0; j < this.reg.getCONNECTIONS_NUMBER(); j++){
            this.reg.available[j] = master_f.readInt();
            this.reg.connected_key[j] = "";
            this.reg.connected_city[j] = "";
            this.reg.wire_name[j] = "";
            for(i = 0; i < 7; i++) { this.reg.connected_key[j] += master_f.readChar(); }
            for(i = 0; i < 50; i++) { this.reg.connected_city[j] += master_f.readChar(); }
            this.reg.weight[j] = master_f.readDouble();
            for(i = 0; i < 60; i++) { this.reg.wire_name[j] += master_f.readChar(); }
        }
    }

    public Register getReg() {
        return reg;
    }
}
