package se.iuh.tuan03.Bai1.Adapter;

public class Adapter
implements JSONData{

    XMLData xml;

    Adapter(XMLData xml){

        this.xml=xml;
    }

    public String getJSON(){

        return "{data:"+xml.getXML()+"}";
    }
}
