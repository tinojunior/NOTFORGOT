package com.example.notforgotto_do;

public class ExampleItem
{
    private int CheckBoxRes;
    private String mText1;
    private String mText2;

    public  ExampleItem(int check_box_res, String text1, String text2)
    {
        CheckBoxRes = check_box_res;
        mText1 = text1;
        mText2 = text2;
    }
    public int getCheckBoxRes()
    {
        return CheckBoxRes;
    }
    public String getmText1()
    {
        return  mText1;
    }
    public  String getmText2()
    {
        return  mText2;
    }

}
