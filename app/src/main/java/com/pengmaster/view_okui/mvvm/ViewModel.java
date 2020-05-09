package com.pengmaster.view_okui.mvvm;

import android.widget.EditText;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-09 15:54
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ViewModel {

    private  TextAttr textAttr = new TextAttr();
    private  TextAttr textAttr2 = new TextAttr();

    ViewModel(ViewBinder binder, EditText editText,EditText editText2){
        binder.binder(editText,textAttr);
        binder.binder(editText2,textAttr2);
    }

    public void load(){
        String[] data = Model.getData();
        textAttr.setText(data[0]);
        textAttr2.setText(data[1]);
    }


    class TextAttr{
        private String text;
        private OnChange onChange;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
            onChange.onChange(text);
        }

        public void setOnChange(OnChange onChange) {
            this.onChange = onChange;
        }
    }

    interface OnChange{
        void onChange(String newText);
    }
}
