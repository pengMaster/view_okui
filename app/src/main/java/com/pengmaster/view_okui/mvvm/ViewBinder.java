package com.pengmaster.view_okui.mvvm;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-09 15:52
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ViewBinder {


    public   void  binder (final EditText editText, final ViewModel.TextAttr textAttr){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = textAttr.getText();
                if(!s.toString().equals(text)){
                    textAttr.setText(s.toString());
                }
            }
        });

        textAttr.setOnChange(new ViewModel.OnChange() {
            @Override
            public void onChange(String newText) {
                if (!newText.equals(editText.getText())) {
                    editText.setText(newText);
                    System.out.println("改变："+newText);
                }
            }
        });
    }
}
