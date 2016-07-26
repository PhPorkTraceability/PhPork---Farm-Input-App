package uplb.cas.ics.phporktraceability;

/**
 * Created by marmagno on 7/19/2016.
 */
public class CheckItemModel {

    private String _id;
    private String _label;
    private String _label2;
    private boolean _isSelected;

    public CheckItemModel(){}

    public CheckItemModel(String id, String label, String label2){
        _id = id;
        _label = label;
        _label2 = label2;
    }

    public CheckItemModel(String id, String label, String label2,  Boolean isSelected){
        _id = id;
        _label = label;
        _label2 = label2;
        _isSelected = isSelected;
    }

    public String getID(){ return _id; }

    public String getLabel(){ return _label; }

    public String getLabel2(){ return _label2; }

    public Boolean isSelected(){ return _isSelected; }

    public void setID(String id){ _id = id; }

    public void setLabel(String label){ _label = label; }

    public void setLabel2(String label2){ _label2 = label2; }

    public void setSelected(Boolean isSelected) { _isSelected = isSelected; }

}
