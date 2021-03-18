package tootipay.wallet.home_module;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;


import tootipay.wallet.EditProfileActivity;
import tootipay.wallet.LoyalityPointsActivity;
import tootipay.wallet.NotificationActivity;
import tootipay.wallet.ReferToFriendActivity;
import tootipay.wallet.SendMoney.ConvertRatesActivity;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.beneficiary_list_module.BeneficiaryListActivity;
import tootipay.wallet.dialogs.AlertDialog;
import tootipay.wallet.home_module.fragments.MainHistoryFragment;
import tootipay.wallet.MoneyTransferModuleV.MoneyTransferMainLayout;

import tootipay.wallet.menumodules.SettingsActivity;
import tootipay.wallet.R;
import tootipay.wallet.databinding.ActivityNewDashboardBinding;


import androidx.core.content.ContextCompat;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tootipay.wallet.home_module.fragments.HomeFragment;
import tootipay.wallet.home_module.viewmodel.HomeViewModel;
import tootipay.wallet.interfaces.OnDecisionMade;
import tootipay.wallet.menumodules.ChangePinActivity;
import tootipay.wallet.my_bank_cards.MyCardAndBankActivity;
import tootipay.wallet.qrcodemodule.QrCodeActivity;
import tootipay.wallet.swift_transfer_module.SwiftTransferActivity;
import tootipay.wallet.utils.BitmapHelper;


public class NewDashboardActivity extends TootiBaseActivity<ActivityNewDashboardBinding>
        implements OnDecisionMade {

    int[] btnIds;
    int[] textIds;

    public HomeViewModel homeViewModel;
    boolean isLogout = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_dashboard;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        moveToFragment(new HomeFragment());
//        HomeFragment fragment = new HomeFragment();
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_frame, fragment).commit();

        //  navController = Navigation.findNavController(this, R.id.dashboard);
        btnIds = new int[]{binding.inculdeLayout.homeImg.getId()
                , binding.inculdeLayout.historyImg.getId(), binding.inculdeLayout.accountImg.getId()
                , binding.inculdeLayout.myBankImg.getId()};

        textIds = new int[]{binding.inculdeLayout.homeTv.getId()
                , binding.inculdeLayout.historyTv.getId(), binding.inculdeLayout.accountTv.getId()
                , binding.inculdeLayout.myBankTv.getId()};

        binding.settings.setOnClickListener(view -> startActivity(new
                Intent(getApplicationContext(), SettingsActivity.class)));

        binding.editProfile.setOnClickListener(v -> {
            if (sessionManager.getISKYCApproved() || sessionManager.getIsDocumentsUploaded()) {
                startActivity(new Intent(this, EditProfileActivity.class));
            } else {
                onMessage(getString(R.string.need_to_complete_kyc));
            }

        });

        binding.myBeneList.setOnClickListener(v -> {
            startActivity(new Intent(this, BeneficiaryListActivity.class));
        });

        binding.payQrCode.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), WalletViaQRCodeActivity.class)));

        binding.billPaymentTv.setOnClickListener(view ->

               // startActivity(new Intent(getApplicationContext(), BillPaymentMainActivity.class))
                startActivity(new Intent(getApplicationContext(), LoyalityPointsActivity.class))
        );

        binding.inculdeLayout.notifiacaitonBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
        });

        binding.changePin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ChangePinActivity.class));
        });


        binding.inculdeLayout.refreshBalance.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), WalletViaQRCodeActivity.class));
        });


        binding.refertofriends.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ReferToFriendActivity.class));
        });


        binding.icLoyalityPoint.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoyalityPointsActivity.class));
        });

        binding.myQrCodeLayout.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), QrCodeActivity.class));
        });

        binding.inculdeLayout.send.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MoneyTransferMainLayout.class));
        });

        binding.inculdeLayout.historyLayout.setOnClickListener(view -> {
            changeImageColor(binding.inculdeLayout.historyImg.getId());
            binding.inculdeLayout.historyTv.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.inculdeLayout.myBankTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.homeTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.accountTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            moveToFragment(new MainHistoryFragment());
            //   startActivity(new Intent(getApplicationContext(), MainHistoryActivity.class));
        });


        binding.inculdeLayout.homeLayout.setOnClickListener(view -> {
            changeImageColor(binding.inculdeLayout.homeImg.getId());
            binding.inculdeLayout.historyTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.myBankTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.accountTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.homeTv.setTextColor(getResources().getColor(R.color.colorPrimary));
            moveToFragment(new HomeFragment());
        });

        binding.inculdeLayout.myOrderLayout.setOnClickListener(view -> {
            changeImageColor(binding.inculdeLayout.myBankImg.getId());
            binding.inculdeLayout.historyTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.myBankTv.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.inculdeLayout.homeTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.accountTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            moveToFragment(new MyCardAndBankActivity());
        });


        binding.inculdeLayout.ourRatesLayout.setOnClickListener(view -> {
            changeImageColor(binding.inculdeLayout.accountImg.getId());
            binding.inculdeLayout.historyTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.myBankTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            binding.inculdeLayout.accountTv.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.inculdeLayout.homeTv.setTextColor(getResources().getColor(R.color.colorDarkGray));
            moveToFragment(new ConvertRatesActivity());
        });

        binding.inculdeLayout.drawerImgBtn.setOnClickListener(v -> {
            binding.drawerLayout.isDrawerOpen(Gravity.START);
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        });


        binding.logout.setOnClickListener(v -> {
            isLogout = true;
            AlertDialog alertDialog = new AlertDialog(getString(R.string.logout_txt)
                    , getString(R.string.logout_message), this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            alertDialog.show(transaction, "");
        });


        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            binding.versionNumber.setText(getString(R.string.version).concat(" ")
                    .concat(String.valueOf(info.versionName)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.swiftTransfer.setOnClickListener(v -> {
            startActivity(new Intent(this, SwiftTransferActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sessionManager.getCustomerImage() != null) {
            Glide.with(this)
                    .load(BitmapHelper.decodeImage(sessionManager.getCustomerImage()))
                    .placeholder(R.drawable.user_profile_home)
                    .into(binding.profileImage);
        }

    }

    public void showHumBurger() {
        binding.inculdeLayout.drawerImgBtn.setVisibility(View.VISIBLE);
    }

    public void hideHumBurger(int id) {
        binding.inculdeLayout.drawerImgBtn.setVisibility(View.GONE);
        binding.inculdeLayout.refreshBalance.setVisibility(View.GONE);
        binding.inculdeLayout.notifiacaitonBtn.setVisibility(View.GONE);
        if (id == ClassChangerHelper.HOME) {
            changeImageColor(binding.inculdeLayout.homeImg.getId());
            changeTextColor(binding.inculdeLayout.homeTv.getId());
            binding.inculdeLayout.drawerImgBtn.setVisibility(View.VISIBLE);
            binding.inculdeLayout.refreshBalance.setVisibility(View.VISIBLE);
            binding.inculdeLayout.notifiacaitonBtn.setVisibility(View.VISIBLE);
        } else if (id == ClassChangerHelper.HISTORY) {
            changeImageColor(binding.inculdeLayout.historyImg.getId());
            changeTextColor(binding.inculdeLayout.historyTv.getId());
        } else if (id == ClassChangerHelper.SAVE_BANK) {
            changeImageColor(binding.inculdeLayout.accountImg.getId());
            changeTextColor(binding.inculdeLayout.accountTv.getId());
        } else if (id == ClassChangerHelper.MY_BANK) {
            changeImageColor(binding.inculdeLayout.myBankImg.getId());
            changeTextColor(binding.inculdeLayout.myBankTv.getId());
        } else if (id == binding.inculdeLayout.ourRatesLayout.getId()) {
            changeImageColor(binding.inculdeLayout.accountImg.getId());
            changeTextColor(binding.inculdeLayout.accountTv.getId());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        walletBalance = "";
        clearUserData();
    }

    public void changeImageColor(int id) {
        for (int btnId : btnIds) {
            ImageView imageView = (ImageView) findViewById(btnId);
            if (btnId == id) {
                imageView.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                imageView.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorDarkGray), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }


    public void changeTextColor(int id) {
        for (int txtIds : textIds) {
            TextView textView = findViewById(txtIds);
            if (txtIds == id) {
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                textView.setTextColor(getResources().getColor(R.color.colorDarkGray));
            }
        }
    }

    static final int TIME_INTERVAL = 2000;
    long mBackPressed;

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            showExitDialog();
            return;
        } else {
            showExitDialog();
        }
        mBackPressed = System.currentTimeMillis();
    }


    public void showExitDialog() {
        isLogout = true;
        AlertDialog alertDialog = new AlertDialog(getString(R.string.exit)
                , getString(R.string.are_sure_exit), this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        alertDialog.show(transaction, "");
    }

    public void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment).commit();
    }


    public void showUserName(String userName) {
        binding.userEmailTv.setText(userName);
    }


    public void showWalletBalance(String balance) {
        binding.walletBalanceMain.setText(getString(R.string.pound).concat(balance));
    }

    @Override
    public void onProceed() {
        if (isLogout) {
            finish();
        }
    }

    @Override
    public void onCancel() {

    }
}
