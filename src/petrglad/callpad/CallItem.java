package petrglad.callpad;

public class CallItem {
    public final String name;
    public final String phoneNo;

    public CallItem(String name, String phone) {
        this.name = name;
        this.phoneNo = phone;
    }

    @Override
    public String toString() {
        return "CallItem [name=" + name + ", phone=" + phoneNo + "]";
    }    
}
