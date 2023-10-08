package com.whl.spring.cloud.demo.bean;

import java.io.Serial;
import java.io.Serializable;

public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = -7973595611414380771L;

    /**
     * id.
     */
    public long id;

    /**
     * user id.
     */
    public String userId;

    /**
     * commodity code.
     */
    public String commodityCode;

    /**
     * count.
     */
    public int count;

    /**
     * money.
     */
    public int money;

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", userId='" + userId + '\'' + ", commodityCode='"
                + commodityCode + '\'' + ", count=" + count + ", money=" + money + '}';
    }

}
