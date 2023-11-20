package moneylink.wallet.mywalletmoduleV.fragments;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import moneylink.wallet.R;

public class MoneyAddWalletActivity extends AppCompatActivity {

    TextView wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_add_wallet);


        wallet = findViewById(R.id.money_tv);



        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyWalletBalanceFragment.class));
            }
        });
    }
}