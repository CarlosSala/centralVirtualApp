package com.example.macscanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class responseServiceNumber {

    @SerializedName("num1")
    @Expose
    private String num1;
    @SerializedName("num2")
    @Expose
    private String num2;
    @SerializedName("num3")
    @Expose
    private String num3;
    @SerializedName("num4")
    @Expose
    private String num4;
    @SerializedName("num5")
    @Expose
    private String num5;
    @SerializedName("num6")
    @Expose
    private String num6;
    @SerializedName("num7")
    @Expose
    private String num7;
    @SerializedName("num8")
    @Expose
    private String num8;
    @SerializedName("num9")
    @Expose
    private String num9;

    /**
     * No args constructor for use in serialization
     *
     */
    public responseServiceNumber() {
    }

    /**
     *
     * @param num9
     * @param num8
     * @param num7
     * @param num6
     * @param num1
     * @param num5
     * @param num4
     * @param num3
     * @param num2
     */
    public responseServiceNumber(String num1, String num2, String num3, String num4, String num5, String num6, String num7, String num8, String num9) {
        super();
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.num6 = num6;
        this.num7 = num7;
        this.num8 = num8;
        this.num9 = num9;
    }

    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getNum3() {
        return num3;
    }

    public void setNum3(String num3) {
        this.num3 = num3;
    }

    public String getNum4() {
        return num4;
    }

    public void setNum4(String num4) {
        this.num4 = num4;
    }

    public String getNum5() {
        return num5;
    }

    public void setNum5(String num5) {
        this.num5 = num5;
    }

    public String getNum6() {
        return num6;
    }

    public void setNum6(String num6) {
        this.num6 = num6;
    }

    public String getNum7() {
        return num7;
    }

    public void setNum7(String num7) {
        this.num7 = num7;
    }

    public String getNum8() {
        return num8;
    }

    public void setNum8(String num8) {
        this.num8 = num8;
    }

    public String getNum9() {
        return num9;
    }

    public void setNum9(String num9) {
        this.num9 = num9;
    }

}