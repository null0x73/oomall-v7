package cn.edu.xmu.other.util;

public enum AfterSaleResponseCode {

    WAITING_CHECK(0,"待管理员审核"),
    WAITING_CUSTOMER_SEND(1,"待买家发货"),
    CUSTOMER_SENT(2,"买家已发货"),
    WAITING_SHOP_REFUND(3,"待店家退款"),
    WAITING_SHOP_SENT(4,"待店家发货"),
    SHOP_SENT(5,"店家已发货"),
    REQUEST_DENY(6,"审核不通过"),
    CANCELED(7,"已取消"),
    ENDED(8,"已结束");

    private final int code;
    private final String message;
    AfterSaleResponseCode(int code, String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(){
        return message;
    }
}
