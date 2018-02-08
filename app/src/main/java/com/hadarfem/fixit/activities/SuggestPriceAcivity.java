package com.hadarfem.fixit.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.hadarfem.fixit.dal.bid.FirebaseBidDao;
import com.hadarfem.fixit.dal.interfaces.IBidDao;
import com.hadarfem.fixit.interfaces.IBidActivity;

import com.hadarfem.fixit.R;
import com.hadarfem.fixit.dal.models.Bid;
import com.hadarfem.fixit.dal.models.Problem;
import com.hadarfem.fixit.validation.BidValidator;
import com.hadarfem.fixit.validation.IValidator;
import com.hadarfem.fixit.validation.ValidationResult;

import java.util.Date;
import java.util.UUID;

public class SuggestPriceAcivity extends AppCompatActivity implements IBidActivity {

    private IBidActivity bidActivity;
    private IValidator<Bid> bidValidator;
    private IBidDao bidDao;
    Bid bid;

    public SuggestPriceAcivity(){
    bidValidator = new BidValidator();
    injectDependencies();
    }

    TextView city_title;
    TextView category_title;
    TextView problem_title;
    EditText bid_price;
    EditText description_title;
    ImageView cameraImageView;
    String bidderUserName;
    String costumerUserName;
    String pictureUrlUser;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_price_acivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Problem currProblem = new Problem();
        currProblem.setCity(getIntent().getStringExtra("PROBLEM_CITY"));
        currProblem.setCategory(getIntent().getStringExtra("PROBLEM_CATEGORY"));
        currProblem.setTitle(getIntent().getStringExtra("PROBLEM_TITLE"));
        currProblem.setDescription(getIntent().getStringExtra("PROBLEM_DESCRIPTION"));
        currProblem.setPictureBase64(getIntent().getStringExtra("PROBLEM_PICTURE"));
        bidderUserName = getIntent().getStringExtra("BIDDER_USERNAME");
        costumerUserName = getIntent().getStringExtra("COSTUMER_USERNAME");
        pictureUrlUser = getIntent().getStringExtra("URL_USERNAME");


        city_title = (TextView) findViewById(R.id.cityView);
        category_title = (TextView) findViewById(R.id.categoryView);
        problem_title = (TextView) findViewById(R.id.problemBidTitleText);
        bid_price = (EditText) findViewById(R.id.priceText);
        description_title = (EditText) findViewById(R.id.descriptionProblemBidText);
        cameraImageView = (ImageView) findViewById(R.id.cameraProblemImage);

        city_title.setText(currProblem.getCity());
        category_title.setText(currProblem.getCategory());
        problem_title.setText(currProblem.getTitle());
        description_title.setText(currProblem.getDescription());
        description_title.setEnabled(false);


        if (currProblem.getPictureBase64() != null) {
            byte[] imageBytes = Base64.decode(currProblem.getPictureBase64(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            cameraImageView.setImageBitmap(decodedImage);
        }

        findViewById(R.id.sendBidButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBid();
            }
        });

        findViewById(R.id.clearBidButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

    }

        public void sendBid() {
        price = bid_price.getText().toString();

        bid = new Bid().setId(UUID.randomUUID().toString()).setDate(new Date()).setTitle(problem_title.getText().toString()).
                setPrice(price).setBidderUserName(bidderUserName).setCostumerUserName(costumerUserName).setPictureUrl(pictureUrlUser);


        ValidationResult validationResult = bidValidator.validate(bid);

        if (!validationResult.isValid()) {
            new AlertDialog.Builder(SuggestPriceAcivity.this)
                    .setTitle(getString(R.string.invalid_problem_title))
                    .setMessage(validationResult.getValidationString())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return;
        }

        saveBid(bid);

        new AlertDialog.Builder(SuggestPriceAcivity.this)
                .setTitle(SuggestPriceAcivity.this.getString(R.string.success_title))
                .setMessage(SuggestPriceAcivity.this.getString(R.string.bid_success_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

        clear();

    }

    private void injectDependencies() {
        bidDao = new FirebaseBidDao();
    }

    @Override
    public void saveBid(Bid bid) {
        bidDao.addBid(bid);
    }

    public void clear() {
        EditText editTextBid = (EditText) findViewById(R.id.priceText);

        if (editTextBid != null) {
            editTextBid.setText("");
        }

    }

}
