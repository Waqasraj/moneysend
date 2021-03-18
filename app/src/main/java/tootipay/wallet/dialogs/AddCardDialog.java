package tootipay.wallet.dialogs;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import tootipay.wallet.R;
import tootipay.wallet.base.BaseDialogFragment;
import tootipay.wallet.databinding.ActivityAddCardsBinding;
import tootipay.wallet.interfaces.OnCardDetailsSubmit;
import tootipay.wallet.utils.NumberFormatter;

public class AddCardDialog extends BaseDialogFragment<ActivityAddCardsBinding> {

    private static final Pattern CODE_PATTERN = Pattern
            .compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
    OnCardDetailsSubmit onCardDetailsSubmit;


    public AddCardDialog(OnCardDetailsSubmit onCardDetailsSubmit) {
        this.onCardDetailsSubmit = onCardDetailsSubmit;
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_NoActionBar_FullScreenDialog;
    }

    @Override
    protected void injectView() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_cards;
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            cancelUpload();
        });
        setCardTextWatcher();
        binding.cardDetails.setVisibility(View.GONE);
        binding.cardDetailsText.setVisibility(View.GONE);
        binding.addCardBtn.setOnClickListener(v -> {
            if (isValidate()) {
                onCardDetailsSubmit.onCardDetailsSSubmit(NumberFormatter.removeSlash(
                        binding.numberTxt.getText().toString()),
                        binding.expireTxt.getText().toString()
                        , binding.cvv.getText().toString());
                cancelUpload();
            }
        });
    }


    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.txtNumber.getText().toString())
                || binding.txtNumber.getText().length() < 19) {
            onMessage(getString(R.string.enter_valid_card_number));
            return false;
        } else if (TextUtils.isEmpty(binding.expireTxt.getText())
                || binding.expireTxt.getText().length() < 5) {
            onMessage(getString(R.string.plz_enter_valid_expire_date));
            return false;
        } else if (TextUtils.isEmpty(binding.cvv.getText().toString())) {
            onMessage(getString(R.string.plz_enter_valid_cvv));
            return false;
        } else if( binding.cvv.getText().length() < 3) {
            onMessage(getString(R.string.plz_enter_valid_cvv));
            return false;
        }
        return true;
    }


    /***
     * this method used set textWatcher on card editText
     */
    private void setCardTextWatcher() {

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtCardholdername.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.numberTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //  cardType = getCreditCardType(s.toString());
                if (binding.numberTxt.getText().toString().length() == 19) {
                    binding.txtExpireMonthyear.requestFocus();
                }
                binding.txtNumber.setText(binding.numberTxt.getText());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // do with text
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && !CODE_PATTERN.matcher(s).matches()) {
                    String input = s.toString();
                    String numbersOnly = keepNumbersOnly(input);
                    String code = formatNumbersAsCode(numbersOnly);
                    binding.numberTxt.removeTextChangedListener(this);
                    binding.numberTxt.setText(code);

                    try {
                        binding.numberTxt.setSelection(code.length());
                    }catch (IndexOutOfBoundsException e ) {
                        Log.e("afterTextChanged: ",e.getLocalizedMessage());
                    }

                    binding.numberTxt.addTextChangedListener(this);
                }
            }

            private String keepNumbersOnly(CharSequence s) {
                return s.toString().replaceAll("[^0-9]", ""); // Should of
                // course be
                // more robust
            }

            private String formatNumbersAsCode(CharSequence s) {
                int groupDigits = 0;
                String tmp = "";
                int sSize = s.length();
                for (int i = 0; i < sSize; ++i) {
                    tmp += s.charAt(i);
                    ++groupDigits;
                    if (groupDigits == 4) {
                        tmp += "-";
                        groupDigits = 0;
                    }
                }
                return tmp;
            }
        });


        binding.expireTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (binding.expireTxt.getText().length() == 5) {
//                    binding.cvv.requestFocus();
//                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    formatCardExpiringDate(editable);
                    binding.txtExpireMonthyear.setText(binding.expireTxt.getText());
                } catch (NumberFormatException e) {
                    editable.clear();
                    binding.txtExpireMonthyear.setText("");
                    //Toast message here.. Wrong date formate

                }
            }
        });

    }


    //Make sure for  binding.expireTxt to be accepting Numbers only
    boolean isSlash = false; //class level initialization

    private void formatCardExpiringDate(Editable s) {
        String input = s.toString();
        String mLastInput = "";

        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        Calendar expiryDateDate = Calendar.getInstance();

        try {
            expiryDateDate.setTime(formatter.parse(input));
        } catch (java.text.ParseException e) {
            if (s.length() == 2 && !mLastInput.endsWith("/") && isSlash) {
                isSlash = false;
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    binding.expireTxt.setText(binding.expireTxt.getText().toString().substring(0, 1));
                    binding.expireTxt.setSelection(binding.expireTxt.getText().toString().length());
                } else {
                    s.clear();
                    binding.expireTxt.setText("");
                    binding.expireTxt.setSelection(binding.expireTxt.getText().toString().length());
                    Toast.makeText(getContext(), getString(R.string.enter_valid_moth), Toast.LENGTH_LONG).show();
                }
            } else if (s.length() == 2 && !mLastInput.endsWith("/") && !isSlash) {
                isSlash = true;
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    binding.expireTxt.setText(binding.expireTxt.getText().toString() + "/");
                    binding.expireTxt.setSelection(binding.expireTxt.getText().toString().length());
                } else if (month > 12) {
                    binding.expireTxt.setText("");
                    binding.expireTxt.setSelection(binding.expireTxt.getText().toString().length());
                    s.clear();
                    Toast.makeText(getContext(), getString(R.string.enter_valid_moth), Toast.LENGTH_LONG).show();
                }


            } else if (s.length() == 1) {

                int month = Integer.parseInt(input);
                if (month > 1 && month < 12) {
                    isSlash = true;
                    binding.expireTxt.setText("0" + binding.expireTxt.getText().toString() + "/");
                    binding.expireTxt.setSelection(binding.expireTxt.getText().toString().length());
                }
            }

            mLastInput = binding.expireTxt.getText().toString();
            return;
        }
    }
}