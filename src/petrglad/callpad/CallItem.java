package petrglad.callpad;

public class CallItem {
    public String name;
    public String phoneNo;

    public CallItem(String name, String phone) {
        this.name = name;
        this.phoneNo = phone;
    }

    @Override
    public String toString() {
        return "CallItem [name=" + name + ", phone=" + phoneNo + "]";
    }    
}
